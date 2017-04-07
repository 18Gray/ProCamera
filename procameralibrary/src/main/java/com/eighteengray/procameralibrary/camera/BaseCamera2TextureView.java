/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eighteengray.procameralibrary.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;



public abstract class BaseCamera2TextureView extends TextureView
{
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public Context context;
    public WindowManager windowManager;
    public HandlerThread mBackgroundThread;
    public Handler mBackgroundHandler;
    public Handler mMainHandlelr;

    public String mCameraId;
    public Size mPreviewSize;
    public Semaphore mCameraOpenCloseLock = new Semaphore(1);
    public boolean mFlashSupported;
    public int mSensorOrientation;


    protected CameraManager manager;
    protected CameraDevice mCameraDevice;
    protected CaptureRequest.Builder mPreviewRequestBuilder;
    protected CaptureRequest mPreviewRequest;
    protected CameraCaptureSession mCaptureSession;
    protected Surface surface;

    //监听，TextureView好了之后，打开相机
    protected final SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height)
        {
            openCameraReal(width, height, 0);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height)
        {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture)
        {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture)
        {
        }

    };

    //监听，相机打开好后，进入预览
    protected final CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback()
    {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice)
        {
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
            configureTransform(getWidth(), getHeight());
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice)
        {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error)
        {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

    };



    //******************************************************************************************
    //  初始化方法
    //********************************************************************************************

    public BaseCamera2TextureView(Context context)
    {
        this(context, null);
        init(context);
    }

    public BaseCamera2TextureView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        init(context);
    }

    public BaseCamera2TextureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context c)
    {
        this.context = c;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    protected void setAspectRatio(int width, int height)
    {
        if (width < 0 || height < 0)
        {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight)
        {
            setMeasuredDimension(width, height);
        } else
        {
            if (width < height * mRatioWidth / mRatioHeight)
            {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else
            {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }


    //******************************************************************************************
    //  public 方法，供外部调用
    //********************************************************************************************

    public void openCamera()
    {
        startBackgroundThread();
        if (isAvailable())
        {
            openCameraReal(getWidth(), getHeight(), 0);
        } else
        {
            setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public void closeCamera()
    {
        closeCameraReal();
        stopBackgroundThread();
    }

    public void reopenCamera(int cameraNum)
    {
        closeCamera();
        openCameraReal(getWidth(), getHeight(), cameraNum);
    }


    //******************************************************************************************
    //  private 方法，内部调用
    //********************************************************************************************
    //后台线程
    private void startBackgroundThread()
    {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        mMainHandlelr = new Handler(context.getMainLooper());
    }

    private void stopBackgroundThread()
    {
        if(mBackgroundHandler != null)
        {
            mBackgroundThread.quitSafely();
            try
            {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    //打开相机，预览
    private void openCameraReal(int width, int height, int cameraNum)
    {
        checkPermission();
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        configureCamera(width, height, cameraNum);
        configureTransform(width, height);

        try
        {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS))
            {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            Log.d("CameraRecordFragment", "openCameraStart");
            manager.openCamera(mCameraId, deviceStateCallback, mBackgroundHandler);
            Log.d("CameraRecordFragment", "openCameraEnd");
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }


    private void checkPermission()
    {

    }


    protected void closePreviewSession()
    {
        if (mCaptureSession != null)
        {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }



    static class CompareSizesByArea implements Comparator<Size>
    {
        @Override
        public int compare(Size lhs, Size rhs)
        {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }


    //abstract方法
    public abstract void configureCamera(int width, int height, int cameraNum);
    public abstract void configureTransform(int width, int height);
    public abstract void createCameraPreviewSession();
    public abstract void closeCameraReal();
}
