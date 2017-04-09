package com.eighteengray.procameralibrary.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static com.eighteengray.procameralibrary.album.ThumbnaiImageView.TAG;



public class RecordTextureView extends BaseCamera2TextureView
{
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;

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
        buildRecordVideoRequest();
    }


    public void stopRecordVideo()
    {
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
    public void configureCamera(int width, int height, int cameraNum)
    {
        try
        {
            mCameraId = manager.getCameraIdList()[cameraNum];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);

            //设置输出选项
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, mVideoSize);

            //如果屏幕旋转需要调整
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                mMainHandlelr.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                    }
                });
            } else
            {
                mMainHandlelr.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                    }
                });

            }

        } catch (CameraAccessException e)
        {
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

    @Override
    public void configureTransform(int width, int height)
    {
        int rotation = windowManager.getDefaultDisplay().getRotation();
        final Matrix matrix = new Matrix();
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
        if(mMainHandlelr != null)
        {
            mMainHandlelr.post(new Runnable()
            {
                @Override
                public void run()
                {
                    setTransform(matrix);
                }
            });
        }
    }


    @Override
    public void createCameraPreviewSession()
    {
        try
        {
            closePreviewSession();
            configureMediaRecorder();
            initSurface();
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mMediaRecorder.getSurface()), recordSessionStateCallback, mBackgroundHandler);
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

    private String getVideoFilePath(Context context)
    {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".mp4";
    }

    CameraCaptureSession.StateCallback recordSessionStateCallback = new CameraCaptureSession.StateCallback()
    {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession)
        {
            if (null == mCameraDevice)
            {
                return;
            }
            mCaptureSession = cameraCaptureSession;
            try
            {
                mCaptureSession.setRepeatingRequest(CaptureRequestFactory.createPreviewRequest(mCameraDevice, surface), null, mBackgroundHandler);
            } catch (CameraAccessException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession)
        {
        }
    };



    //录像操作
    private void buildRecordVideoRequest()
    {
        try
        {
            mCaptureSession.setRepeatingRequest(CaptureRequestFactory.createRecordRequest(mCameraDevice, mMediaRecorder.getSurface()), recordCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    CameraCaptureSession.CaptureCallback recordCaptureCallback = new CameraCaptureSession.CaptureCallback()
    {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult)
        {
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result)
        {
            mMediaRecorder.start();
            mMainHandlelr.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, "录制开始", Toast.LENGTH_SHORT).show();
                }
            });
        }

    };


    @Override
    public void closeCameraReal()
    {
        try
        {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice)
            {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder)
            {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally
        {
            mCameraOpenCloseLock.release();
        }
    }


}
