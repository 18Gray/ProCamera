package com.eighteengray.procamera.fragment;

/**
 * Created by lutao on 2017/4/6.
 */

public interface IRecordView
{
    public void setFlashMode(int mode);
    public void switchCamera(boolean isFront);

    public void setGpuFilter();
    public void setRatio();

    public void startRecordVideo();
    public void stopRecordVideo();

}
