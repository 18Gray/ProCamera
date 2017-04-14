package com.eighteengray.procameralibrary.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.view.Surface;
import android.view.WindowManager;

import static com.eighteengray.procameralibrary.camera.Camera2TextureView.ORIENTATIONS;


public class CaptureRequestFactory
{

    public static CaptureRequest createPreviewRequest(CameraDevice cameraDevice, Surface surface) throws CameraAccessException
    {
        float valueAF = 5.0f;
        int valueAE = 0;
        long valueAETime = (214735991 - 13231) / 2;
        int valueISO = (10000 - 100) / 2;

        CaptureRequest.Builder previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        previewBuilder.addTarget(surface);
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);
        /*previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
        previewBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_OFF);
        previewBuilder.set(CaptureRequest.LENS_FOCUS_DISTANCE, valueAF);
        previewBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, valueAETime);
        previewBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, valueAE);
        previewBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, valueISO);*/
        return previewBuilder.build();
    }

    public static CaptureRequest createFocusRequest(CameraDevice cameraDevice, Surface surface, MeteringRectangle[] meteringRectangleArr) throws CameraAccessException
    {
        CaptureRequest.Builder focusBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        focusBuilder.addTarget(surface);
        focusBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        focusBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        focusBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, meteringRectangleArr);  //设置聚焦区域
        return focusBuilder.build();
    }

    //点击事件对应的处理方法
    public static CaptureRequest createFlashRequest(CameraDevice cameraDevice, Surface surface, int flashMode) throws CameraAccessException
    {
        CaptureRequest.Builder previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        previewBuilder.addTarget(surface);
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        switch (flashMode)
        {
            case Constants.FLASH_AUTO:
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH);
                previewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_SINGLE);
                break;

            case Constants.FLASH_ON:
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                previewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_STATE_READY);
                break;

            case Constants.FLASH_OFF:
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
                previewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
                break;

            case Constants.FLASH_FLARE:
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
                previewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
                break;
        }
        return previewBuilder.build();
    }




    public static CaptureRequest createCaptureRequest(CameraDevice cameraDevice, Surface surface, WindowManager windowManager) throws CameraAccessException
    {
        CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(surface);
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

        int rotation = windowManager.getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
        return captureBuilder.build();
    }

    public static CaptureRequest createCaptureAgainRequest(CameraDevice cameraDevice, Surface surface) throws CameraAccessException
    {
        CaptureRequest.Builder captureAgaainBuilder = null;
        captureAgaainBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureAgaainBuilder.addTarget(surface);
        captureAgaainBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        return captureAgaainBuilder.build();
    }

    public static CaptureRequest createRecordRequest(CameraDevice cameraDevice, Surface surface) throws CameraAccessException
    {
        CaptureRequest.Builder recordBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
        recordBuilder.addTarget(surface);
        return recordBuilder.build();
    }


}
