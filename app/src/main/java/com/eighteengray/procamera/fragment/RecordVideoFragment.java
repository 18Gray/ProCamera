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



public class RecordVideoFragment extends BaseCameraFragment implements FragmentCompat.OnRequestPermissionsResultCallback, IRecordView
{
    RecordTextureView recordTextureView;


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
        Log.d("CameraRecordFragment", "RecordFragmentOnResumeStart");
        recordTextureView.openCamera();
        Log.d("CameraRecordFragment", "RecordFragmentOnResumeEnd");
    }

    @Override
    public void onPause()
    {
        recordTextureView.closeCamera();
        super.onPause();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_PERMISSIONS)
        {
            if (grantResults.length == permissions.length)
            {
                for (int result : grantResults)
                {
                    if (result != PackageManager.PERMISSION_GRANTED)
                    {
                        ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), "error");
                        break;
                    }
                }
            } else
            {
                ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), "error");
            }
        } else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
