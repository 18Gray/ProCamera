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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;

import com.eighteengray.procameralibrary.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.eighteengray.procameralibrary.album.ThumbnaiImageView.TAG;


public class RecordTextureView extends BaseCamera2TextureView
{
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;
    private Surface mRecorderSurface;
    private boolean mIsRecordingVideo;

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    static
    {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static
    {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }



    CameraCaptureSession.StateCallback recordSessionStateCallback = new CameraCaptureSession.StateCallback()
    {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession)
        {
            mCaptureSession = cameraCaptureSession;
            buildPreviewRequest();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession)
        {
        }
    };

    CameraCaptureSession.StateCallback recordSessionStateCallbackResult = new CameraCaptureSession.StateCallback()
    {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession)
        {
            mCaptureSession = cameraCaptureSession;
            buildPreviewRequest();
            mMainHandlelr.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mIsRecordingVideo = true;
                    Toast.makeText(context, "start record", Toast.LENGTH_SHORT).show();
                    mMediaRecorder.start();
                }
            });
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession)
        {
            mMainHandlelr.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, "record error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    public RecordTextureView(Context context)
    {
        super(context);
    }

    public RecordTextureView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RecordTextureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }



    //******************************************************************************************
    //  public 方法，供外部调用
    //********************************************************************************************

    public void startRecordVideo()
    {
        if (null == mCameraDevice || !isAvailable() || null == mPreviewSize)
        {
            return;
        }
        closePreviewSession();
        configureMediaRecorder();
        buildRecordVideoRequest();
    }


    public void stopRecordVideo()
    {
        // UI
        mIsRecordingVideo = false;
        // Stop recording
        if(mMediaRecorder != null)
        {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
        }
        mMainHandlelr.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, "Video saved: " + mNextVideoAbsolutePath,
                        Toast.LENGTH_SHORT).show();
            }
        });
        mNextVideoAbsolutePath = null;
        createCameraPreviewSession();
    }



    //******************************************************************************************
    //  private 方法，内部调用
    //********************************************************************************************
    @Override
    public void checkPermission()
    {
        checkPermissionReal(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_WRITESTORAGE_PERMISSION);
        checkPermissionReal(Manifest.permission.RECORD_AUDIO, REQUEST_RECORD_PERMISSION);
    }

    @Override
    public void configureCamera(int width, int height)
    {
        try
        {
            mCameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, mVideoSize);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else
            {
                setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }

        } catch (CameraAccessException e)
        {
        }
    }

    @Override
    public void configureTransform(int width, int height)
    {
        int rotation = windowManager.getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, width, height);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation)
        {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) height / mPreviewSize.getHeight(),
                    (float) width / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        setTransform(matrix);
    }


    @Override
    public void createCameraPreviewSession()
    {
        try
        {
            closePreviewSession();
            SurfaceTexture texture = getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            surface = new Surface(texture);

            mCameraDevice.createCaptureSession(Arrays.asList(surface), recordSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    private void buildPreviewRequest()
    {
        try
        {
            // 创建预览请求
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

            mPreviewRequest = mPreviewRequestBuilder.build();
            mCaptureSession.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler);  //到此预览完成，下面会响应拍照或录像点击事件
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private void configureMediaRecorder()
    {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty())
        {
            mNextVideoAbsolutePath = getVideoFilePath(context);
        }
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (mSensorOrientation)
        {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        try
        {
            mMediaRecorder.prepare();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void buildRecordVideoRequest()
    {
        try
        {
            SurfaceTexture texture = getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            List<Surface> surfaces = new ArrayList<>();
            surface = new Surface(texture);
            surfaces.add(surface);

            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mPreviewRequestBuilder.addTarget(surface);

            mRecorderSurface = mMediaRecorder.getSurface();
            surfaces.add(mRecorderSurface);
            mPreviewRequestBuilder.addTarget(mRecorderSurface);

            mCameraDevice.createCaptureSession(surfaces, recordSessionStateCallbackResult , mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    private static Size chooseVideoSize(Size[] choices)
    {
        for (Size size : choices)
        {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080)
            {
                return size;
            }
        }
        return choices[choices.length - 1];
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio)
    {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices)
        {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height)
            {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0)
        {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else
        {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }


    private String getVideoFilePath(Context context)
    {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".mp4";
    }


}
