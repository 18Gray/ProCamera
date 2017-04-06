package com.eighteengray.procamera.fragment;

import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eighteengray.procamera.R;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import static com.eighteengray.procamera.R.id.cameraTextureView;



public class Camera2Fragment extends BaseCameraFragment implements FragmentCompat.OnRequestPermissionsResultCallback, ICameraView
{
    Camera2TextureView camera2TextureView;


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
        Log.d("CameraRecordFragment", "Camera2FragmentOnResumeStart");
        camera2TextureView.openCamera();
        Log.d("CameraRecordFragment", "Camera2FragmentOnResumeEnd");
    }

    @Override
    public void onPause()
    {
        camera2TextureView.closeCamera();
        super.onPause();
    }





    @Override
    public void setFlashMode(int mode)
    {

    }

    @Override
    public void switchCamera(boolean isFront)
    {

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

    }

    @Override
    public void delayShutter()
    {

    }
}
