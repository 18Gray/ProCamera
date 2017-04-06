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
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static
    {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


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
            buildPreviewRequest();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession)
        {

        }
    };

    //监听，获取预览数据并显示
    CameraCaptureSession.CaptureCallback captureSessionCaptureCallback = new CameraCaptureSession.CaptureCallback()
    {
        private void process(CaptureResult result)
        {
            switch (mState)
            {
                case STATE_PREVIEW:
                {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }

                case STATE_WAITING_LOCK:
                {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null)
                    {
                        buildCapturePictureRequest();
                    }
                    else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState)
                    {
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED)
                        {
                            mState = STATE_PICTURE_TAKEN;
                            buildCapturePictureRequest();
                        } else
                        {
                            buildPrecaptureSequenceRequest();
                        }
                    }
                    break;
                }

                case STATE_WAITING_PRECAPTURE:
                {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED)
                    {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }

                case STATE_WAITING_NON_PRECAPTURE:
                {
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE)
                    {
                        mState = STATE_PICTURE_TAKEN;
                        buildCapturePictureRequest();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
            @NonNull CaptureRequest request,
            @NonNull CaptureResult partialResult)
        {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
            @NonNull CaptureRequest request,
            @NonNull TotalCaptureResult result)
        {
            process(result);
        }
    };


    CameraCaptureSession.CaptureCallback captureSessionCaptureCallbackResult = new CameraCaptureSession.CaptureCallback()
    {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result)
        {
            mMainHandlelr.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, imagePath, Toast.LENGTH_LONG).show();
                }
            });
            endTakPictureReal();
        }
    };

    //监听，获取到数据做处理
    protected ImageReader mImageReader;
    String imagePath;
    protected final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener()
    {
        @Override
        public void onImageAvailable(ImageReader reader)
        {
            Image image = reader.acquireLatestImage();
            String path = SDCardUtils.getSDCardPath();
            File file = new File(path, "pic.jpg");
            imagePath = file.getAbsolutePath();
            mBackgroundHandler.post(new ImageSaver(image, file));
        }

    };



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
    //  private 方法，内部调用
    //********************************************************************************************
    @Override
    public void configureCamera(int width, int height)
    {
        try
        {
            for (String cameraId : manager.getCameraIdList())
            {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                //设置拍照的图像大小
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null)
                {
                    continue;
                }
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                //设置使用后置摄像头
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT)
                {
                    continue;
                }

                //如果相机旋转了，要交换横竖尺寸
                int displayRotation = windowManager.getDefaultDisplay().getRotation();
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation)
                {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270)
                        {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180)
                        {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        break;
                }
                Point displaySize = new Point();
                windowManager.getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;
                if (swappedDimensions)
                {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }
                if (maxPreviewWidth > MAX_PREVIEW_WIDTH)
                {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }
                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT)
                {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                //设置预览尺寸，这里调节TextureView到指定的宽高，不再做比例适配
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);
                mPreviewSize = new Size(width, height);
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    mMainHandlelr.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                        }
                    });
                } else
                {
                    mMainHandlelr.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                        }
                    });
                }

                // 检查是否支持闪关灯
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
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
            SurfaceTexture texture = getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            surface = new Surface(texture);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), captureSessionStateCallback, null);
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


    private void buildPreviewRequest()
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
            mCaptureSession.setRepeatingRequest(mPreviewRequest, captureSessionCaptureCallback, mBackgroundHandler);  //到此预览完成，下面会响应拍照或录像点击事件
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    private void buildCapturePictureRequest()
    {
        try
        {
            // 获取图片的请求
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mPreviewRequestBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(mPreviewRequestBuilder);
            int rotation = windowManager.getDefaultDisplay().getRotation();
            mPreviewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallbackResult, null);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private void buildPrecaptureSequenceRequest()
    {
        try
        {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    private void setAutoFlash(CaptureRequest.Builder requestBuilder)
    {
        if (mFlashSupported)
        {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio)
    {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices)
        {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w)
            {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight)
                {
                    bigEnough.add(option);
                } else
                {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0)
        {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0)
        {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else
        {
            return choices[0];
        }
    }


    private int getOrientation(int rotation)
    {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }


    private void takePictureReal()
    {
        try
        {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }


    private void endTakPictureReal()
    {
        try
        {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), captureSessionCaptureCallback, mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, captureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
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
                output.flush();
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

}
