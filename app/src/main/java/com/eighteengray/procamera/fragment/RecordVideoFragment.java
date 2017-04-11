package com.eighteengray.procamera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import com.eighteengray.procameralibrary.camera.RecordTextureView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecordVideoFragment extends BaseCameraFragment implements IRecordView
{
    View view;

    //上部
    @BindView(R.id.iv_flash_camera)
    ImageView iv_flash_camera;
    @BindView(R.id.iv_switch_camera)
    ImageView iv_switch_camera;

    //拍照
    @BindView(R.id.recordTextureView)
    RecordTextureView recordTextureView;

    //中下部
    @BindView(R.id.rl_middle_bottom_menu)
    RelativeLayout rl_middle_bottom_menu;
    @BindView(R.id.tv_mode_select)
    TextView tv_mode_select;

    //下部
    @BindView(R.id.iv_album_camera)
    ImageView iv_album_camera;
    @BindView(R.id.iv_shutter_camera)
    ImageView iv_shutter_camera;
    @BindView(R.id.iv_setting_camera)
    ImageView iv_setting_camera;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_recordvideo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        recordTextureView.openCamera();
    }

    @Override
    public void onPause()
    {
        if(recordTextureView != null)
        {
            recordTextureView.closeCamera();
        }
        super.onPause();
    }


    @OnClick({R.id.iv_flash_camera, R.id.iv_switch_camera,
            R.id.recordTextureView, R.id.tv_mode_select,
            R.id.iv_album_camera, R.id.iv_shutter_camera, R.id.iv_setting_camera})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_flash_camera:

                break;

            case R.id.iv_switch_camera:

                break;

            case R.id.recordTextureView:

                break;

            case R.id.tv_mode_select:
                ModeSelectDialogFragment modeSelectDialogFragment = new ModeSelectDialogFragment();
                modeSelectDialogFragment.show(getFragmentManager(), "Camera");
                break;

            case R.id.iv_album_camera:

                break;

            case R.id.iv_shutter_camera:

                break;

            case R.id.iv_setting_camera:

                break;
        }
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
