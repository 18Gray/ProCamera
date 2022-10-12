package com.eighteengray.procameralibrary.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.location.Location;
import android.view.Surface;
import android.view.WindowManager;

import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.gpufilter.GpuFilterModel;

import java.util.List;

import static com.eighteengray.procameralibrary.camera.Camera2TextureView.ORIENTATIONS;


public class CaptureRequestFactory {
    //创建预览请求，后面的setXXX方法是根据不同情况设置预览参数
    public static CaptureRequest.Builder createPreviewBuilder(CameraDevice cameraDevice, Surface surface) throws CameraAccessException {
        CaptureRequest.Builder previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        previewBuilder.addTarget(surface);
        return previewBuilder;
    }

    //设置预览-自动聚焦模式
    public static void setPreviewBuilderPreview(CaptureRequest.Builder previewBuilder) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
    }

    //设置预览-锁定焦点
    public static void setPreviewBuilderLockfocus(CaptureRequest.Builder previewBuilder) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
    }

    //设置预览-解除锁定焦点
    public static void setPreviewBuilderUnlockfocus(CaptureRequest.Builder previewBuilder) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
    }

    //设置预览-聚焦区域
    public static void setPreviewBuilderFocusRegion(CaptureRequest.Builder previewBuilder, MeteringRectangle[] meteringRectangleArr) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, meteringRectangleArr);
        previewBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, meteringRectangleArr);
        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
    }

    //设置预览-开始手动聚焦
    public static void setPreviewBuilderFocusTrigger(CaptureRequest.Builder previewBuilder) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        previewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);
    }

    //设置预览-闪光灯模式
    public static CaptureRequest setPreviewBuilderFlash(CaptureRequest.Builder previewBuilder, int flashMode) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        switch (flashMode) {
            case Constants.FLASH_AUTO:
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH);
                break;

            case Constants.FLASH_ON:
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
                previewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_SINGLE);
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


    //设置预览-Scene模式
    public static CaptureRequest setPreviewBuilderScene(CaptureRequest.Builder previewBuilder, String scene) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, GpuFilterModel.getSceneMode(scene));
        return previewBuilder.build();
    }

    //设置预览-Effect模式
    public static CaptureRequest setPreviewBuilderEffect(CaptureRequest.Builder previewBuilder, String effect) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, GpuFilterModel.getEffectMode(effect));
        return previewBuilder.build();
    }

    // 设置预览-防手抖
    public static CaptureRequest setPreviewBuilderSteady(CaptureRequest.Builder previewBuilder, boolean antiShake) throws CameraAccessException {
        if(antiShake){
            previewBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CameraMetadata.LENS_OPTICAL_STABILIZATION_MODE_ON);
        } else {
            previewBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CameraMetadata.LENS_OPTICAL_STABILIZATION_MODE_OFF);
        }
        return previewBuilder.build();
    }


    //创建拍照请求
    public static CaptureRequest.Builder createCaptureStillBuilder(CameraDevice cameraDevice, Surface surface) throws CameraAccessException {
        CaptureRequest.Builder captureBuilder = null;
        if(cameraDevice != null) {
            captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(surface);
        }
        return captureBuilder;
    }

    //设置拍照模式-拍静态图片
    public static void setCaptureBuilderStill(CaptureRequest.Builder captureBuilder, WindowManager windowManager, int quality) throws CameraAccessException {
        captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

        int rotation = windowManager.getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
        captureBuilder.set(CaptureRequest.JPEG_QUALITY, (byte) quality);
    }

    //设置拍照模式-连续拍摄
    public static void setCaptureBuilderPrecapture(CaptureRequest.Builder captureBuilder) throws CameraAccessException {
        captureBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
    }


    //设置拍照模式-延时拍摄
    public static void setCaptureBuilderDelay(CaptureRequest.Builder captureBuilder, long nanoseconds) {
        captureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, nanoseconds);
    }


    //创建录像请求
    public static CaptureRequest.Builder createRecordBuilder(CameraDevice cameraDevice, List<Surface> surfaces) throws CameraAccessException {
        CaptureRequest.Builder recordBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
        recordBuilder.addTarget(surfaces.get(0));
        recordBuilder.addTarget(surfaces.get(1));
        return recordBuilder;
    }

    //设置预览模式-录像预览
    public static void setPreviewBuilderRecordPreview(CaptureRequest.Builder previewBuilder) throws CameraAccessException {
        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
//        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

}
