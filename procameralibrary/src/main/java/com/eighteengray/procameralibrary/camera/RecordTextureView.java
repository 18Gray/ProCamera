package com.eighteengray.procameralibrary.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Toast;
import com.eighteengray.procameralibrary.dataevent.RecordVideoEvent;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static com.eighteengray.commonutillibrary.SDCardUtils.getSystemPicFile;



public class RecordTextureView extends BaseCamera2TextureView
{
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest.Builder mRecordVideoBuilder;
    private boolean isRecording;

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
        try
        {
            isRecording = true;
            closePreviewSession();
            configureMediaRecorder();

            List<Surface> surfaces = new ArrayList<>();
            surfaces.add(surface);
            surfaces.add(mMediaRecorder.getSurface());
            mRecordVideoBuilder = CaptureRequestFactory.createRecordBuilder(mCameraDevice, surfaces);
            mCameraDevice.createCaptureSession(surfaces, recordSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    public void stopRecordVideo()
    {
        isRecording = false;
        //EventBus发送消息，更新UI
        RecordVideoEvent recordVideoEvent = new RecordVideoEvent();
        recordVideoEvent.setRecording(false);
        EventBus.getDefault().post(recordVideoEvent);

        try
        {
            //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();  报错为：RuntimeException:stop failed
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
        } catch (IllegalStateException e)
        {
            Log.i("Exception", Log.getStackTraceString(e));
        } catch (RuntimeException e)
        {
            Log.i("Exception", Log.getStackTraceString(e));
        } catch (Exception e)
        {
            Log.i("Exception", Log.getStackTraceString(e));
        }

        mMainHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, "Video saved: " + mNextVideoAbsolutePath, Toast.LENGTH_SHORT).show();
            }
        });
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
            mPreviewSize = new Size(getMeasuredWidth(), getMeasuredHeight());

            //如果屏幕旋转需要调整
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            } else
            {
                setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
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
        if(mMainHandler != null)
        {
            mMainHandler.post(new Runnable()
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
            initSurface();
            mCameraDevice.createCaptureSession(Arrays.asList(surface), recordSessionStateCallback, mBackgroundHandler);
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
        mNextVideoAbsolutePath = getVideoFilePath(context);
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
        String picName = SystemClock.currentThreadTimeMillis() + ".mp4";
        File file = new File(getSystemPicFile(context), picName);
        return file.getAbsolutePath();
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
                mPreviewRequestBuilder = CaptureRequestFactory.createPreviewBuilder(mCameraDevice, surface);
                CaptureRequestFactory.setPreviewBuilderRecordPreview(mPreviewRequestBuilder);
                updatePreview(mPreviewRequestBuilder.build(), null);

                if(isRecording)
                {
                    //EventBus给UI发消息，更新按钮。更新完成后，开始录像。
                    RecordVideoEvent recordVideoEvent = new RecordVideoEvent();
                    recordVideoEvent.setRecording(true);
                    EventBus.getDefault().post(recordVideoEvent);
                    mMediaRecorder.start();
                }

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



    //点击事件的处理方法
    public void setFlashMode(int flashMode) throws CameraAccessException
    {
        CaptureRequestFactory.setPreviewBuilderFlash(mPreviewRequestBuilder, flashMode);
        updatePreview(mPreviewRequestBuilder.build(), null);
    }


}
