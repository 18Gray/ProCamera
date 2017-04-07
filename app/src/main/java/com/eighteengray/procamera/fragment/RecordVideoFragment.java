package com.eighteengray.procamera.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.dialogfragment.ErrorDialogFragment;
import com.eighteengray.procameralibrary.camera.RecordTextureView;



public class RecordVideoFragment extends BaseCameraFragment implements IRecordView
{
    boolean isRecordCameraOpen = false;
    RecordTextureView recordTextureView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        isRecordCameraOpen = isVisibleToUser;
        if(recordTextureView != null && isRecordCameraOpen)
        {
            recordTextureView.openCamera();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recordvideo, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        recordTextureView = (RecordTextureView) view.findViewById(R.id.recordTextureView);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(isRecordCameraOpen)
        {
            recordTextureView.openCamera();
        }
    }

    @Override
    public void onPause()
    {
        isRecordCameraOpen =false;
        if(recordTextureView != null)
        {
            recordTextureView.closeCamera();
        }
        super.onPause();
    }





    @Override
    public void setFlashMode(int mode)
    {

    }

    @Override
    public void switchCamera(int cameraNum)
    {
        recordTextureView.reopenCamera(cameraNum);
    }

    @Override
    public void setGpuFilter()
    {

    }

    @Override
    public void setRatio()
    {

    }

    @Override
    public void startRecordVideo()
    {
        recordTextureView.startRecordVideo();
    }

    @Override
    public void stopRecordVideo()
    {
        recordTextureView.stopRecordVideo();
    }

}
