
package com.eighteengray.procameralibrary.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
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
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.eighteengray.commonutillibrary.SDCardUtils.getSDCardPath;
import static com.eighteengray.commonutillibrary.SDCardUtils.getSystemPicFile;


public class Camera2TextureView extends BaseCamera2TextureView
{
    private int mState = STATE_PREVIEW;
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;

    private int mAfState = CameraMetadata.CONTROL_AF_STATE_INACTIVE;
    private boolean isTrigger = false;

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
        lockFocus();
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
            mPreviewSize = new Size(getWidth(), getHeight());
            String picName = SystemClock.currentThreadTimeMillis() + ".jpg";
            mFile = new File(getSystemPicFile(context), picName);

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
                mPreviewRequestBuilder = CaptureRequestFactory.createPreviewBuilder(mCameraDevice, surface);
                CaptureRequestFactory.setPreviewBuilderPreview(mPreviewRequestBuilder);
                updatePreview(mPreviewRequestBuilder.build(), captureSessionCaptureCallback);
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
            checkState(request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result)
        {
            checkState(request, result);
        }

        private void checkState(CaptureRequest request, CaptureResult result)
        {
            Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
            Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
            switch (mState)
            {
                case STATE_PREVIEW:  //在preview中处理TextureView的触摸事件
                    if (afState == null)
                    {
                        Log.d("Camera2TextureView", "null");
                        return;
                    }
                    //这次的值与之前的一样，忽略掉
                    if (afState.intValue() == mAfState)
                    {
                        Log.d("Camera2TextureView", "same");
                        return;
                    }
                    mAfState = afState.intValue();
                    judgeFocus();  //聚焦视图
                    break;

                case STATE_WAITING_LOCK:
                    if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState
                            || CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState || CaptureResult.CONTROL_AF_STATE_PASSIVE_UNFOCUSED == afState)
                    {
                        if (afState == null)
                        {
                            doStillCapture();
                        }
                        else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                               || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState)
                        {
                            aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                            if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED)
                            {
                                mState = STATE_PICTURE_TAKEN;
                                doStillCapture();
                            } else
                            {
                                tryCaptureAgain();
                            }
                        }
                    }
                    break;

                case STATE_WAITING_PRECAPTURE:
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED)
                    {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;

                case STATE_WAITING_NON_PRECAPTURE:
                    aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE)
                    {
                        mState = STATE_PICTURE_TAKEN;
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
            mCaptureStillBuilder = CaptureRequestFactory.createCaptureStillBuilder(mCameraDevice, mImageReader.getSurface());
            CaptureRequestFactory.setCaptureBuilderStill(mCaptureStillBuilder, windowManager);

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(mCaptureStillBuilder.build(), captureStillCallback, null);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.CaptureCallback captureStillCallback = new CameraCaptureSession.CaptureCallback()
    {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result)
        {
            mMainHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, "saved:" + mFile, Toast.LENGTH_SHORT).show();
                }
            });
            unlockFocus();
        }
    };

    private void tryCaptureAgain()
    {
        try
        {
            CaptureRequestFactory.setCaptureBuilderPrecapture(mCaptureStillBuilder);
            mCaptureSession.capture(mCaptureStillBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
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
            //这里应该发EventBus，让View去处理，先开启线程保存，然后图像显示在界面上，在缩小动画到左边相册，相册显示图像。
            // 上面执行完成后，会回调回来onCaptureCompleted。提示保存成功，并进入预览。
            new Thread(new ImageSaver(reader, mFile)).start();
        }
    };




    //拍照，要先发锁定焦点的preview请求，待captureSessionCaptureCallback回调，进入STATE_WAITING_CAPTURE从而可以调用真的拍照请求。
    private void lockFocus()
    {
        try
        {
            CaptureRequestFactory.setPreviewBuilderLockfocus(mPreviewRequestBuilder);
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    public void unlockFocus()
    {
        try {
            CaptureRequestFactory.setPreviewBuilderUnlockfocus(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);

            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
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



    //点击事件的处理方法
    public void setFlashMode(int flashMode) throws CameraAccessException
    {
        CaptureRequestFactory.setPreviewBuilderFlash(mPreviewRequestBuilder, flashMode);
        updatePreview(mPreviewRequestBuilder.build(), captureSessionCaptureCallback);
    }


    public void focusRegion(float x, float y) throws CameraAccessException
    {
        isTrigger = false;
        try
        {
            mCameraCharacteristics = manager.getCameraCharacteristics(mCameraId);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
        Rect rect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        int areaSize = 200;
        int right = rect.right;
        int bottom = rect.bottom;
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int ll, rr;
        Rect newRect;
        int centerX = (int) x;
        int centerY = (int) y;
        ll = ((centerX * right) - areaSize) / viewWidth;
        rr = ((centerY * bottom) - areaSize) / viewHeight;
        int focusLeft = clamp(ll, 0, right);
        int focusBottom = clamp(rr, 0, bottom);
        newRect = new Rect(focusLeft, focusBottom, focusLeft + areaSize, focusBottom + areaSize);
        MeteringRectangle meteringRectangle = new MeteringRectangle(newRect, 500);
        MeteringRectangle[] meteringRectangleArr = {meteringRectangle};
        CaptureRequestFactory.setPreviewBuilderFocusRegion(mPreviewRequestBuilder, meteringRectangleArr);
        updatePreview(mPreviewRequestBuilder.build(), captureSessionCaptureCallback);
        CaptureRequestFactory.setPreviewBuilderFocusTrigger(mPreviewRequestBuilder);
        mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
    }


    private int clamp(int x, int min, int max)
    {
        if (x < min)
        {
            return min;
        } else if (x > max)
        {
            return max;
        } else
        {
            return x;
        }
    }

    private void judgeFocus()
    {
        switch (mAfState)
        {
            case CameraMetadata.CONTROL_AF_STATE_INACTIVE:
                TextureViewTouchEvent.FocusState focusState0 = new TextureViewTouchEvent.FocusState();
                focusState0.setFocusState(Constants.FOCUS_INACTIVE);
                EventBus.getDefault().post(focusState0);
                break;

            case CameraMetadata.CONTROL_AF_STATE_PASSIVE_SCAN:
                TextureViewTouchEvent.FocusState focusState1 = new TextureViewTouchEvent.FocusState();
                focusState1.setFocusState(Constants.FOCUS_FOCUSING);
                EventBus.getDefault().post(focusState1);
                break;

            case CameraMetadata.CONTROL_AF_STATE_PASSIVE_FOCUSED:
                TextureViewTouchEvent.FocusState focusState2 = new TextureViewTouchEvent.FocusState();
                focusState2.setFocusState(Constants.FOCUS_SUCCEED);
                EventBus.getDefault().post(focusState2);
                break;

            case CameraMetadata.CONTROL_AF_STATE_ACTIVE_SCAN:
                TextureViewTouchEvent.FocusState focusState3 = new TextureViewTouchEvent.FocusState();
                focusState3.setFocusState(Constants.FOCUS_FOCUSING);
                EventBus.getDefault().post(focusState3);
                break;

            case CameraMetadata.CONTROL_AF_STATE_FOCUSED_LOCKED:
                TextureViewTouchEvent.FocusState focusState4 = new TextureViewTouchEvent.FocusState();
                focusState4.setFocusState(Constants.FOCUS_SUCCEED);
                EventBus.getDefault().post(focusState4);
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
                break;

            case CameraMetadata.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED:
                TextureViewTouchEvent.FocusState focusState5 = new TextureViewTouchEvent.FocusState();
                focusState5.setFocusState(Constants.FOCUS_FAILED);
                EventBus.getDefault().post(focusState5);
                break;

            case CameraMetadata.CONTROL_AF_STATE_PASSIVE_UNFOCUSED:
                TextureViewTouchEvent.FocusState focusState6 = new TextureViewTouchEvent.FocusState();
                focusState6.setFocusState(Constants.FOCUS_FAILED);
                EventBus.getDefault().post(focusState6);
                break;
        }
    }

}
