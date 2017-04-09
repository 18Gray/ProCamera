package com.eighteengray.procameralibrary.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;
import android.view.WindowManager;

import static com.eighteengray.procameralibrary.camera.Camera2TextureView.ORIENTATIONS;


public class CaptureRequestFactory
{

    public static CaptureRequest createPreviewRequest(CameraDevice cameraDevice, Surface surface) throws CameraAccessException
    {
        CaptureRequest.Builder previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        previewBuilder.addTarget(surface);
        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
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
