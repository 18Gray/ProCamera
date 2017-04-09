
package com.eighteengray.procameralibrary.camera;

import android.content.Context;
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
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Toast;
import com.eighteengray.commonutillibrary.SDCardUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class Camera2TextureView extends BaseCamera2TextureView
{
    private int mState = STATE_PREVIEW;
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_CAPTURE = 1;
    private static final int STATE_TRY_CAPTURE_AGAIN = 2;
    private static final int STATE_TRY_DO_CAPTURE = 3;

    public static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static
    {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    public Camera2TextureView(Context context)
    {
        super(context);
    }

    public Camera2TextureView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public Camera2TextureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    //******************************************************************************************
    //  public 方法，供外部调用
    //********************************************************************************************

    public void takePicture()
    {
        takePictureReal();
    }


    //******************************************************************************************
    //  预览方法，内部调用
    //********************************************************************************************
    @Override
    public void configureCamera(int width, int height, int cameraNum)
    {
        try
        {
            mCameraId = manager.getCameraIdList()[cameraNum];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);

            //设置图像输出
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
            initImageReader(largest);
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, largest);

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
            e.printStackTrace();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void initImageReader(Size largest)
    {
        mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/5);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
    }

    private Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio)
    {
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
        } else if (Surface.ROTATION_180 == rotation)
        {
            matrix.postRotate(180, centerX, centerY);
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
            initSurface();
            mState = STATE_PREVIEW;
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), captureSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }



    //监听，进入预览状态，预览配置成功后获取预览数据
    protected CameraCaptureSession.StateCallback captureSessionStateCallback = new CameraCaptureSession.StateCallback()
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
                mCaptureSession.setRepeatingRequest(CaptureRequestFactory.createPreviewRequest(mCameraDevice, surface), captureSessionCaptureCallback, mBackgroundHandler);
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




    //放在中间，预览和拍照的数据获取
    CameraCaptureSession.CaptureCallback captureSessionCaptureCallback = new CameraCaptureSession.CaptureCallback()
    {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult)
        {
            checkState(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result)
        {
            checkState(result);
        }

        private void checkState(CaptureResult result)
        {
            switch (mState)
            {
                case STATE_PREVIEW:
                    // NOTHING
                    break;

                case STATE_WAITING_CAPTURE:
                    int afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState
                            || CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState || CaptureResult.CONTROL_AF_STATE_PASSIVE_UNFOCUSED == afState)
                    {
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED)
                        {
                            mState = STATE_TRY_DO_CAPTURE;
                            doStillCapture();
                        } else
                        {
                            mState = STATE_TRY_CAPTURE_AGAIN;
                            tryCaptureAgain();
                        }
                    }
                    break;

                case STATE_TRY_CAPTURE_AGAIN:
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED)
                    {
                        mState = STATE_TRY_DO_CAPTURE;
                    }
                    break;

                case STATE_TRY_DO_CAPTURE:
                    aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE)
                    {
                        mState = STATE_TRY_DO_CAPTURE;
                        doStillCapture();
                    }
                    break;
            }
        }

    };

    private void doStillCapture()
    {
        try
        {
            mCaptureSession.stopRepeating();
            mCaptureSession.capture(CaptureRequestFactory.createCaptureRequest(mCameraDevice, mImageReader.getSurface(), windowManager), new CameraCaptureSession.CaptureCallback()
            {
            }, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private void tryCaptureAgain()
    {
        try
        {
            mCaptureSession.capture(CaptureRequestFactory.createCaptureAgainRequest(mCameraDevice, mImageReader.getSurface()), captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    protected ImageReader mImageReader;
    protected final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener()
    {
        @Override
        public void onImageAvailable(ImageReader reader)
        {
            new Thread(new ImageSaver(reader)).start();
        }
    };



    //拍照，要先发preview请求，待captureSessionCaptureCallback回调，进入STATE_WAITING_CAPTURE从而可以调用真的拍照请求。
    private void takePictureReal()
    {
        try
        {
            mState = STATE_WAITING_CAPTURE;
            mCaptureSession.setRepeatingRequest(CaptureRequestFactory.createPreviewRequest(mCameraDevice, surface), captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void closeCameraReal()
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
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally
        {
            mCameraOpenCloseLock.release();
        }
    }


}
