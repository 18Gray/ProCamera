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
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
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
    public File mFile;

    public IRequestPermission iRequestPermission;
    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int REQUEST_RECORD_PERMISSION = 2;
    public static final int REQUEST_WRITESTORAGE_PERMISSION = 3;

    protected CameraManager manager;
    protected CameraDevice mCameraDevice;
    protected CaptureRequest.Builder mPreviewRequestBuilder;
    protected CaptureRequest mPreviewRequest;
    protected CameraCaptureSession mCaptureSession;
    protected ImageReader mImageReader;
    protected Surface surface;

    //监听，TextureView好了之后，打开相机
    protected final SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height)
        {
            openCameraReal(width, height);
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
            configureCamera(getWidth(), getHeight());
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

    //监听，进入预览状态，预览配置成功后获取预览数据
    protected CameraCaptureSession.StateCallback captureSessionStateCallback = new CameraCaptureSession.StateCallback()
    {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession)
        {
            // The camera is already closed
            if (null == mCameraDevice)
            {
                return;
            }

            // When the session is ready, we start displaying the preview.
            mCaptureSession = cameraCaptureSession;
            createPreviewRequest();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession)
        {

        }
    };

    //监听，获取到数据做处理
    protected final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener()
    {
        @Override
        public void onImageAvailable(ImageReader reader)
        {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }

    };



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
        mFile = new File(context.getExternalFilesDir(null), "pic.jpg");
    }

    public void setAspectRatio(int width, int height)
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

    public void setIRequestPermission(IRequestPermission iRequestPermission)
    {
        this.iRequestPermission = iRequestPermission;
    }


    public void openCamera()
    {
        startBackgroundThread();
        if (isAvailable())
        {
            openCameraReal(getWidth(), getHeight());
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


    //打开相机，预览
    private void openCameraReal(int width, int height)
    {
        checkPermission();

        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        configureCamera(width, height);
        configureTransform(width, height);

        try
        {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS))
            {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, deviceStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private void closeCameraReal()
    {
        try
        {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession)
            {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice)
            {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader)
            {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally
        {
            mCameraOpenCloseLock.release();
        }
    }

    protected void checkPermissionReal(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
        {
            if (iRequestPermission != null)
            {
                iRequestPermission.requestPermission(permission, requestCode);
            }
        }
    }


    protected void createCameraPreviewSession()
    {
        try
        {
//            closePreviewSession();
            SurfaceTexture texture = getSurfaceTexture();
            assert texture != null;
            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            // This is the output Surface we need to start preview.
            surface = new Surface(texture);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), captureSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    protected void closePreviewSession()
    {
        if (mCaptureSession != null)
        {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }


    protected void createPreviewRequest()
    {
        try
        {
            // 创建预览请求
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Auto focus should be continuous for camera preview.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // Flash is automatically enabled when necessary.
            setAutoFlash(mPreviewRequestBuilder);

            // Finally, we start displaying the camera preview.
            mPreviewRequest = mPreviewRequestBuilder.build();
            mCaptureSession.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler);  //到此预览完成，下面会响应拍照或录像点击事件
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }

    }


    private void setAutoFlash(CaptureRequest.Builder requestBuilder)
    {
        if (mFlashSupported)
        {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }



    private static class ImageSaver implements Runnable
    {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        public ImageSaver(Image image, File file)
        {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run()
        {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try
            {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                mImage.close();
                if (null != output)
                {
                    try
                    {
                        output.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
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
    public abstract void checkPermission();
    public abstract void configureCamera(int width, int height);
    public abstract void configureTransform(int width, int height);
}
