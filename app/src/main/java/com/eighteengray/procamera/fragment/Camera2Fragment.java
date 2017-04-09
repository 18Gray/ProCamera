package com.eighteengray.procamera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eighteengray.procamera.R;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import static com.eighteengray.procamera.R.id.cameraTextureView;



public class Camera2Fragment extends BaseCameraFragment implements ICameraView
{
    boolean isCameraOpen = false;
    Camera2TextureView camera2TextureView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        isCameraOpen = isVisibleToUser;
        if(camera2TextureView != null)
        {
            if(isCameraOpen)
            {
                camera2TextureView.openCamera();
            }
            else
            {
                camera2TextureView.closeCamera();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_camera2, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        camera2TextureView = (Camera2TextureView) view.findViewById(cameraTextureView);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        camera2TextureView.openCamera();
    }

    @Override
    public void onPause()
    {
        isCameraOpen = false;
        if(camera2TextureView != null)
        {
            camera2TextureView.closeCamera();
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
        camera2TextureView.reopenCamera(cameraNum);
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
    public void takePicture()
    {
        camera2TextureView.takePicture();
    }

    @Override
    public void delayShutter()
    {

    }
}
