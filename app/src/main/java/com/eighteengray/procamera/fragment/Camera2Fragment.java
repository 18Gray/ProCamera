package com.eighteengray.procamera.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerAdapter;
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.eighteengray.procamera.R.id.cameraTextureView;



public class Camera2Fragment extends BaseCameraFragment implements ICameraView
{
    View view;

    //上部
    @BindView(R.id.iv_flash_camera)
    ImageView iv_flash_camera;
    @BindView(R.id.tv_mode_gpufileter)
    TextView tv_mode_gpufileter;
    @BindView(R.id.iv_switch_camera)
    ImageView iv_switch_camera;

    //拍照
    @BindView(R.id.cameraTextureView)
    Camera2TextureView cameraTextureView;

    //中下部
    @BindView(R.id.rl_middle_bottom_menu)
    RelativeLayout rl_middle_bottom_menu;
    @BindView(R.id.iv_hdr_camera)
    ImageView iv_hdr_camera;
    @BindView(R.id.tv_mode_select)
    TextView tv_mode_select;
    @BindView(R.id.iv_gpufilter_camera)
    ImageView iv_gpufilter_camera;

    //下部
    @BindView(R.id.iv_album_camera)
    ImageView iv_album_camera;
    @BindView(R.id.iv_ratio_camera)
    ImageView iv_ratio_camera;
    @BindView(R.id.iv_shutter_camera)
    ImageView iv_shutter_camera;
    @BindView(R.id.iv_delay_shutter)
    ImageView iv_delay_shutter;
    @BindView(R.id.iv_setting_camera)
    ImageView iv_setting_camera;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_camera2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        cameraTextureView.openCamera();
    }

    @Override
    public void onPause()
    {
        if(cameraTextureView != null)
        {
            cameraTextureView.closeCamera();
        }
        super.onPause();
    }


    @OnClick({R.id.iv_flash_camera, R.id.iv_switch_camera,
        R.id.cameraTextureView,
        R.id.iv_hdr_camera, R.id.tv_mode_select, R.id.iv_gpufilter_camera,
        R.id.iv_album_camera, R.id.iv_ratio_camera, R.id.iv_shutter_camera, R.id.iv_delay_shutter, R.id.iv_setting_camera})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_flash_camera:

                break;

            case R.id.iv_switch_camera:

                break;

            case R.id.cameraTextureView:

                break;

            case R.id.iv_hdr_camera:

                break;

            case R.id.tv_mode_select:
                ModeSelectDialogFragment modeSelectDialogFragment = new ModeSelectDialogFragment();
                modeSelectDialogFragment.show(getFragmentManager(), "Camera");
                break;

            case R.id.iv_gpufilter_camera:

                break;

            case R.id.iv_album_camera:

                break;

            case R.id.iv_ratio_camera:

                break;

            case R.id.iv_shutter_camera:

                break;

            case R.id.iv_delay_shutter:

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
