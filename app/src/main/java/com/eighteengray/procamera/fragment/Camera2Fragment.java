package com.eighteengray.procamera.fragment;

import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eighteengray.procamera.dataevent.CameraConfigure;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.activity.GpuFilterActivity;
import com.eighteengray.procamera.activity.SettingActivity;
import com.eighteengray.procamera.widget.TextureViewTouchListener;
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment;
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import com.eighteengray.procameralibrary.camera.Constants;
import com.eighteengray.procameralibrary.camera.TextureViewTouchEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class Camera2Fragment extends BaseCameraFragment
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
    @BindView(R.id.iv_focus_camera)
    ImageView iv_focus_camera;

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
    @BindView(R.id.rl_bottommenu)
    RelativeLayout rl_bottommenu;
    @BindView(R.id.iv_album_camera)
    ImageView iv_album_camera;
    @BindView(R.id.iv_ratio_camera)
    ImageView iv_ratio_camera;
    @BindView(R.id.iv_shutter_camera)
    ImageView iv_shutter_camera;
    @BindView(R.id.iv_delay_shutter)
    ImageView iv_delay_shutter;
    @BindView(R.id.tv_delay_second)
    TextView tv_delay_second;
    @BindView(R.id.iv_setting_camera)
    ImageView iv_setting_camera;

    Handler handler;
    private boolean mFlagShowFocusImage = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_camera2, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        handler = new Handler(Looper.getMainLooper());
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        cameraTextureView.openCamera();
        cameraTextureView.setOnTouchListener(new TextureViewTouchListener());
    }

    @Override
    public void onPause()
    {
        if(cameraTextureView != null)
        {
            cameraTextureView.closeCamera();
        }
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    @OnClick({R.id.iv_flash_camera, R.id.iv_switch_camera,
        R.id.iv_hdr_camera, R.id.tv_mode_select, R.id.iv_gpufilter_camera,
        R.id.iv_album_camera, R.id.iv_ratio_camera, R.id.iv_shutter_camera, R.id.iv_delay_shutter, R.id.iv_setting_camera})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_flash_camera: //闪光灯
                tv_mode_gpufileter.setVisibility(View.GONE);
                int[] location1 = new int[2];
                iv_flash_camera.getLocationOnScreen(location1);
                PopupWindowFactory.createFlashPopupWindow(getActivity()).showAtLocation(iv_flash_camera, Gravity.NO_GRAVITY, location1[0]+iv_flash_camera.getWidth(), location1[1]-iv_flash_camera.getHeight());
                break;

            case R.id.iv_switch_camera: //切换摄像头
                cameraTextureView.switchCamera();
                break;

            case R.id.iv_hdr_camera:  //hdr设置
                int[] location2 = new int[2];
                iv_hdr_camera.getLocationOnScreen(location2);
                PopupWindowFactory.createHdrPopupWindow(getActivity()).showAtLocation(iv_hdr_camera, Gravity.NO_GRAVITY, location2[0]+iv_hdr_camera.getWidth(), location2[1] - 30);
                break;

            case R.id.tv_mode_select: //模式选择，相机、视频、全景
                ModeSelectDialogFragment modeSelectDialogFragment = new ModeSelectDialogFragment();
                modeSelectDialogFragment.show(getFragmentManager(), "mode");
                break;

            case R.id.iv_gpufilter_camera: //添加gpu滤镜
                Intent intent_gpufilter = new Intent(getActivity(), GpuFilterActivity.class);
                getActivity().startActivity(intent_gpufilter);
                break;

            case R.id.iv_album_camera: //进入相册
                Intent intent_album = new Intent(getActivity(), AlbumActivity.class);
                startActivity(intent_album);
                break;

            case R.id.iv_ratio_camera: //弹出比例修改对话框，修改拍摄比例
                int[] location3 = new int[2];
                iv_ratio_camera.getLocationOnScreen(location3);
                PopupWindowFactory.createRatioPopupWindow(getActivity()).showAtLocation(iv_ratio_camera, Gravity.BOTTOM, 0, rl_bottommenu.getHeight() + rl_middle_bottom_menu.getHeight());
                break;

            case R.id.iv_shutter_camera: //点击拍摄，执行拍摄操作（要结合已经点击的配置），存储图像，然后把图像显示到屏幕上。
                                        // 然后图片通过动画下到相册按钮，相册按钮显示图片缩略图，然后主屏幕进入预览
                cameraTextureView.takePicture();
                break;

            case R.id.iv_delay_shutter: //在延时TextView上显示时间(做放大缩小动画)，同时执行延时拍摄配置

                break;

            case R.id.iv_setting_camera: //进入设置界面
                Intent intent_setting = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent_setting);
                break;
        }
    }


    //EventBus--TextureView触摸事件
    @Subscribe(threadMode = ThreadMode.MAIN)  //轻按：显示焦点，完成聚焦和测光。
    public void onTextureClick(TextureViewTouchEvent.TextureClick textureClick) throws CameraAccessException
    {
        cameraTextureView.focus(textureClick.getX(), textureClick.getY());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //长按：显示焦点，完成聚焦和测光，再拍摄。
    public void onTextureLongClick(TextureViewTouchEvent.TextureLongClick textureLongClick)
    {
        Toast.makeText(getActivity(), "longclick", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) // 单指滑动，如果是向右下则进度环增加，否则减小，用于调节焦点白平衡。
    public void onTextureOneDrag(TextureViewTouchEvent.TextureOneDrag textureOneDrag)
    {
        Toast.makeText(getActivity(), "onedrag", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  // 两手指拖动则完成焦距调节。
    public void onTextureTwoDrag(TextureViewTouchEvent.TextureTwoDrag textureTwoDrag)
    {
        Toast.makeText(getActivity(), "twodrag", Toast.LENGTH_SHORT).show();
    }


    //针对上面的聚焦，这四个是聚焦成功、失败等状态时显示的view
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowFocus(TextureViewTouchEvent.FocusState focusState)
    {
        switch (focusState.getFocusState())
        {
            case Constants.FOCUS_FOCUSING:
                if (mFlagShowFocusImage == false)
                {
                    iv_focus_camera.setVisibility(View.VISIBLE);
                    iv_focus_camera.setImageResource(R.mipmap.focusing);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(200);
                    iv_focus_camera.startAnimation(scaleAnimation);
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            iv_focus_camera.setVisibility(View.GONE);
                        }
                    }, 1000);
                    mFlagShowFocusImage = true;
                }
                break;

            case Constants.FOCUS_SUCCEED:
                if (mFlagShowFocusImage == true)
                {
                    iv_focus_camera.setVisibility(View.VISIBLE);
                    iv_focus_camera.setImageResource(R.mipmap.focus_succeed);
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            iv_focus_camera.setVisibility(View.GONE);
                        }
                    }, 1000);
                    mFlagShowFocusImage = false;
                }
                break;

            case Constants.FOCUS_INACTIVE:
                iv_focus_camera.setVisibility(View.GONE);
                mFlagShowFocusImage = false;
                break;

            case Constants.FOCUS_FAILED:
                if (mFlagShowFocusImage == true)
                {
                    iv_focus_camera.setVisibility(View.VISIBLE);
                    iv_focus_camera.setImageResource(R.mipmap.focus_failed);
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            iv_focus_camera.setVisibility(View.GONE);
                        }
                    }, 1000);
                    mFlagShowFocusImage = false;
                }
                break;
        }
    }




    //EventBus--接收相机配置的参数
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFlashSelect(CameraConfigure.Flash flash) throws CameraAccessException
    {
        tv_mode_gpufileter.setVisibility(View.VISIBLE);
        cameraTextureView.setFlashMode(flash.getFlash());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHdrSelect(CameraConfigure.Hdr hdr)
    {
        switch (hdr.getHdr())
        {
            case Constants.HDR_ON:

                break;

            case Constants.HDR_OFF:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGpuFilter(CameraConfigure.GpuFilter gpuFilter)
    {
        switch (gpuFilter.getGpufilter())
        {
            case Constants.GPUFILTER_0:

                break;

            case Constants.GPUFILTER_1:

                break;

            case Constants.GPUFILTER_2:

                break;

            case Constants.GPUFILTER_3:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRatioSelect(CameraConfigure.Ratio ratio)
    {
        switch (ratio.getRatio())
        {
            case Constants.RATIO_NORMAL:

                break;

            case Constants.RATIO_SQUARE:

                break;

            case Constants.RATIO_4V3:

                break;

            case Constants.RATIO_16V9:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDelayTime(CameraConfigure.DelayTime delayTime)
    {
        switch (delayTime.getDelaytime())
        {
            case Constants.DELAY_3:

                break;

            case Constants.DELAY_5:

                break;

            case Constants.DELAY_8:

                break;

            case Constants.DELAY_10:

                break;
        }
    }


}
