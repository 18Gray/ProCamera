package com.eighteengray.procamera.fragment;

import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.activity.SettingActivity;
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment;
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory;
import com.eighteengray.procameralibrary.camera.RecordTextureView;
import com.eighteengray.procameralibrary.dataevent.CameraConfigure;
import com.eighteengray.procameralibrary.dataevent.RecordVideoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecordVideoFragment extends BaseCameraFragment
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

    private boolean isRecording = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_recordvideo, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
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
        if (recordTextureView != null)
        {
            recordTextureView.closeCamera();
        }
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    @OnClick({R.id.iv_flash_camera, R.id.iv_switch_camera,
            R.id.recordTextureView, R.id.tv_mode_select,
            R.id.iv_album_camera, R.id.iv_shutter_camera, R.id.iv_setting_camera})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_flash_camera: //闪光灯
                int[] location1 = new int[2];
                iv_flash_camera.getLocationOnScreen(location1);
                PopupWindowFactory.createFlashPopupWindow(getActivity()).showAtLocation(iv_flash_camera, Gravity.NO_GRAVITY, location1[0] + iv_flash_camera.getWidth(), location1[1] - iv_flash_camera.getHeight());
                break;

            case R.id.iv_switch_camera:
                recordTextureView.switchCamera();
                break;

            case R.id.recordTextureView:

                break;

            case R.id.tv_mode_select:
                ModeSelectDialogFragment modeSelectDialogFragment = new ModeSelectDialogFragment();
                modeSelectDialogFragment.show(getFragmentManager(), "Camera");
                break;

            case R.id.iv_album_camera:
                Intent intent_album = new Intent(getActivity(), AlbumActivity.class);
                startActivity(intent_album);
                break;

            case R.id.iv_shutter_camera:
                if (!isRecording)
                {
                    isRecording = true;
                    recordTextureView.startRecordVideo();
                } else if (isRecording)
                {
                    isRecording = false;
                    recordTextureView.stopRecordVideo();
                }
                break;

            case R.id.iv_setting_camera:
                Intent intent_setting = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent_setting);
                break;
        }
    }


    //EventBus接收相机配置的参数
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFlashSelect(CameraConfigure.Flash flash) throws CameraAccessException
    {
        recordTextureView.setFlashMode(flash.getFlash());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordVideo(RecordVideoEvent recordVideoEvent) throws CameraAccessException
    {
        if(recordVideoEvent.isRecording())
        {
            iv_shutter_camera.setImageResource(R.drawable.btn_shutter_recording);
        }
        else
        {
            iv_shutter_camera.setImageResource(R.drawable.btn_shutter_record);
        }
    }


}