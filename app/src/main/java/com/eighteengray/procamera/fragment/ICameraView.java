package com.eighteengray.procamera.fragment;

/**
 * Created by lutao on 2017/4/6.
 */

public interface ICameraView
{
    public void setFlashMode(int mode);
    public void switchCamera(int cameraNum);

    public void setGpuFilter();
    public void setRatio();

    public void takePicture();
    public void delayShutter();

}
