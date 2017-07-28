package com.eighteengray.procamera.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.eighteengray.commonutillibrary.FileUtils;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.activity.SettingActivity;
import com.eighteengray.procamera.business.ImageSaver;
import com.eighteengray.procamera.widget.FocusView;
import com.eighteengray.procamera.widget.TextureViewTouchListener;
import com.eighteengray.procamera.widget.VerticalSeekBar;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerAdapter;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerViewHolder;
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment;
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.CameraConfigure;
import com.eighteengray.procameralibrary.dataevent.ImageAvailableEvent;
import com.eighteengray.procameralibrary.camera.TextureViewTouchEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * 关于点击后聚焦和相应的视图变化：
 * 点击TextureView后会触发TextureViewTouchListener，发送EventBus，Camera2Fragment接收该EventBus消息，在onTextureClick等四个函数中处理触摸事件。
 * 例如就是onTextureClick这个单击事件，则调用cameraTextureView.focusRegion，然后会进入captureSessionCaptureCallback的checkState，然后judgeFocus。
 * judgeFocus中会存在几种聚焦状态，根据不同的状态发送EventBus，Camera2Fragment接收该EventBus消息，在onShowFocus中处理不同聚焦状态应该显示的视图。
 * 只有完成了上面聚焦和测光后，才能进行单指滑动。如果是向右下则进度环增加，否则减小，用于调节焦点白平衡。滑动后修改上面两个延时任务的标志位，似其不再执行。
 */
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
    @BindView(R.id.iv_takepicture_done)
    ImageView iv_takepicture_done;
    @BindView(R.id.seekbar_camera2)
    VerticalSeekBar seekbar_camera2;
    @BindView(R.id.focusview_camera2)
    FocusView focusview_camera2;

    //Scene和Effect的RecyclerView
    @BindView(R.id.rcv_scene)
    RecyclerView rcv_scene;
    BaseRecyclerAdapter<String> sceneRecyclerAdapter;
    ArrayList<String> sceneArrayList = new ArrayList<>();

    @BindView(R.id.rcv_effect)
    RecyclerView rcv_effect;
    BaseRecyclerAdapter<String> effectRecyclerAdapter;
    ArrayList<String> effectArrayList = new ArrayList<>();

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

    Handler handler;  //用来更新UI的handler
    private boolean mFlagShowFocusImage = false; //聚焦图像是否显示的标志位
    private float mRawX, mRawY; //触摸聚焦时候的中心点
    protected File mFile;   //保存图片的路径
    private int delayTime = 0;

    boolean isHDRVisible = false;
    boolean isEFFECTVisible = false;
    TextureViewTouchListener textureViewTouchListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_camera2, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        handler = new Handler(Looper.getMainLooper());
        mFile = FileUtils.createSaveBitmapFile(getActivity());
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        cameraTextureView.openCamera();
        textureViewTouchListener = new TextureViewTouchListener(cameraTextureView);
        cameraTextureView.setOnTouchListener(textureViewTouchListener);
        initView();
    }


    private void initView()
    {
        LinearLayoutManager sceneLayoutManager = new LinearLayoutManager(getActivity());
        sceneLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_scene.setLayoutManager(sceneLayoutManager);
        sceneRecyclerAdapter = new BaseRecyclerAdapter<String>(R.layout.item_text_grid)
        {
            @Override
            public void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, final String item, int position)
            {
                TextView textView = baseRecyclerViewHolder.getViewById(R.id.tv_item_textgrid);
                textView.setText(item);

                textView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CameraConfigure.Scene scene = new CameraConfigure.Scene();
                        scene.setScene(item);
                        EventBus.getDefault().post(scene);
                        rcv_scene.setVisibility(View.GONE);
                    }
                });
            }
        };
        rcv_scene.setAdapter(sceneRecyclerAdapter);
        setSceneData();
        sceneRecyclerAdapter.setData(sceneArrayList);

        LinearLayoutManager effectLayoutManager = new LinearLayoutManager(getActivity());
        effectLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_effect.setLayoutManager(effectLayoutManager);
        effectRecyclerAdapter = new BaseRecyclerAdapter<String>(R.layout.item_text_grid)
        {
            @Override
            public void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, final String item, int position)
            {
                TextView textView = baseRecyclerViewHolder.getViewById(R.id.tv_item_textgrid);
                textView.setText(item);

                textView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CameraConfigure.Effect effect = new CameraConfigure.Effect();
                        effect.setEffect(item);
                        EventBus.getDefault().post(effect);
                        rcv_effect.setVisibility(View.GONE);
                    }
                });
            }
        };
        rcv_effect.setAdapter(effectRecyclerAdapter);
        setEffectData();
        effectRecyclerAdapter.setData(effectArrayList);

        seekbar_camera2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                try
                {
                    cameraTextureView.changeFocusDistance(progress);
                } catch (CameraAccessException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });
    }

    private void setSceneData()
    {
        sceneArrayList.add("DISABLED");
        sceneArrayList.add("FACE_PRIORITY");
        sceneArrayList.add("ACTION");
        sceneArrayList.add("PORTRAIT");
        sceneArrayList.add("LANDSCAPE");
        sceneArrayList.add("NIGHT");
        sceneArrayList.add("PORTRAIT");
        sceneArrayList.add("THEATRE");
        sceneArrayList.add("BEACH");
        sceneArrayList.add("SNOW");
        sceneArrayList.add("SUNSET");
        sceneArrayList.add("STEADYPHOTO");
        sceneArrayList.add("FIREWORKS");
        sceneArrayList.add("SPORTS");
        sceneArrayList.add("PARTY");
        sceneArrayList.add("CANDLELIGHT");
        sceneArrayList.add("BARCODE");
        sceneRecyclerAdapter.setData(sceneArrayList);
    }

    private void setEffectData()
    {
        effectArrayList.add("AQUA");
        effectArrayList.add("BLACKBOARD");
        effectArrayList.add("MONO");
        effectArrayList.add("NEGATIVE");
        effectArrayList.add("POSTERIZE");
        effectArrayList.add("SEPIA");
        effectArrayList.add("SOLARIZE");
        effectArrayList.add("WHITEBOARD");
        effectArrayList.add("OFF");
        effectRecyclerAdapter.setData(effectArrayList);
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

    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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

            case R.id.iv_hdr_camera:  //HDR设置
                if(rcv_effect.getVisibility() == View.VISIBLE)
                {
                    rcv_effect.setVisibility(View.GONE);
                }
                if(!isHDRVisible)
                {
                    rcv_scene.setVisibility(View.VISIBLE);
                    isHDRVisible = true;
                }
                else
                {
                    rcv_scene.setVisibility(View.GONE);
                    isHDRVisible = false;
                }
                break;

            case R.id.tv_mode_select: //模式选择，相机、视频
                ModeSelectDialogFragment modeSelectDialogFragment = new ModeSelectDialogFragment();
                modeSelectDialogFragment.show(getFragmentManager(), "mode");
                break;

            case R.id.iv_gpufilter_camera: //GPU滤镜
                if(rcv_scene.getVisibility() == View.VISIBLE)
                {
                    rcv_scene.setVisibility(View.GONE);
                }
                if(!isEFFECTVisible)
                {
                    rcv_effect.setVisibility(View.VISIBLE);
                    isEFFECTVisible = true;
                }
                else
                {
                    rcv_effect.setVisibility(View.GONE);
                    isEFFECTVisible = false;
                }
                break;

            case R.id.iv_album_camera: //进入相册
                Intent intent_album = new Intent(getActivity(), AlbumActivity.class);
                startActivity(intent_album);
                break;

            case R.id.iv_ratio_camera: //弹出比例修改对话框，修改拍摄比例
                int[] location = new int[2];
                iv_ratio_camera.getLocationOnScreen(location);
                PopupWindowFactory.createRatioPopupWindow(getActivity()).showAtLocation(iv_ratio_camera, Gravity.BOTTOM, 0, rl_bottommenu.getHeight() + rl_middle_bottom_menu.getHeight());
                break;

            case R.id.iv_shutter_camera: //点击拍摄，黑屏显示，执行拍摄操作。然后存储图像到指定路径，黑屏消失，相册处显示缩略图
                showViewTakePicture();
                cameraTextureView.takePicture();
                break;

            case R.id.iv_delay_shutter: //在延时TextView上显示时间(做放大缩小动画)，同时执行延时拍摄配置
                switch (delayTime)
                {
                    case 0:
                        delayTime = 3;
                        break;
                    case 3:
                        delayTime = 10;
                        break;
                    case 10:
                        delayTime = 0;
                        break;
                }
                tv_delay_second.setText(delayTime + "");
                cameraTextureView.setDalayTime(delayTime * 1000);
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
        mRawX = textureClick.getRawX();
        mRawY = textureClick.getRawY();
        cameraTextureView.focusRegion(textureClick.getX(), textureClick.getY());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //长按：进行测光点和对焦点锁定
    public void onTextureLongClick(TextureViewTouchEvent.TextureLongClick textureLongClick)
    {
        showViewTakePicture();
        cameraTextureView.takePicture();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) // 单指滑动，如果是向右下则进度环增加，否则减小，用于调节焦点白平衡。
    public void onTextureOneDrag(TextureViewTouchEvent.TextureOneDrag textureOneDrag)
    {
        focusview_camera2.dragChangeAWB(textureOneDrag.getDistance());
    }


    //聚焦的四种状态，对应的显示的View
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowFocus(TextureViewTouchEvent.FocusState focusState)
    {
        switch (focusState.getFocusState())
        {
            case Constants.FOCUS_FOCUSING:
                if (mFlagShowFocusImage == false)
                {
                    //聚焦图片显示在手点击的位置
                    if(mRawX == 0 || mRawY == 0)
                    {
                        mRawX = cameraTextureView.getMeasuredWidth() / 2;
                        mRawY = cameraTextureView.getMeasuredHeight() / 2;
                    }
                    focusview_camera2.showFocusing(mRawX, mRawY, textureViewTouchListener);
                    mFlagShowFocusImage = true;
                }
                break;

            case Constants.FOCUS_SUCCEED:
                if (mFlagShowFocusImage == true)
                {
                    focusview_camera2.showFocusSucceed(textureViewTouchListener);
                    mFlagShowFocusImage = false;
                }
                break;

            case Constants.FOCUS_INACTIVE:
                focusview_camera2.setVisibility(View.GONE);
                mFlagShowFocusImage = false;
                break;

            case Constants.FOCUS_FAILED:
                if (mFlagShowFocusImage == true)
                {
                    focusview_camera2.showFocusFailed();
                    mFlagShowFocusImage = false;
                }
                break;
        }
    }




    //EventBus--接收相机配置的参数
    @Subscribe(threadMode = ThreadMode.MAIN)  //闪光灯设置
    public void onFlashSelect(CameraConfigure.Flash flash) throws CameraAccessException
    {
        tv_mode_gpufileter.setVisibility(View.VISIBLE);
        switch (flash.getFlash())
        {
            case Constants.FLASH_AUTO:
                iv_flash_camera.setImageResource(R.mipmap.flash_auto_white_24dp);
                break;

            case Constants.FLASH_ON:
                iv_flash_camera.setImageResource(R.mipmap.flash_on_white_24dp);
                break;

            case Constants.FLASH_OFF:
                iv_flash_camera.setImageResource(R.mipmap.flash_off_white_24dp);
                break;

            case Constants.FLASH_FLARE:
                iv_flash_camera.setImageResource(R.mipmap.flash_flare_white_24dp);
                break;
        }
        cameraTextureView.setFlashMode(flash.getFlash());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //HDR模式选择
    public void onSceneSelect(CameraConfigure.Scene scene) throws CameraAccessException
    {
        cameraTextureView.setSceneMode(scene.getScene());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //GPU滤镜选择
    public void onEffectSelect(CameraConfigure.Effect effect) throws CameraAccessException
    {
        tv_mode_gpufileter.setText(effect.getEffect());
        cameraTextureView.setEffectMode(effect.getEffect());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //拍摄比例调节
    public void onRatioSelect(CameraConfigure.Ratio ratio)
    {
        cameraTextureView.setRatioMode(ratio.getRatio());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //延时拍摄
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

    @Subscribe(threadMode = ThreadMode.MAIN) //拍照完成后，拿到ImageReader，然后做保存图片的操作
    public void onImageReaderAvailable(ImageAvailableEvent.ImageReaderAvailable imageReaderAvailable)
    {
        new Thread(new ImageSaver(imageReaderAvailable.getImageReader(), mFile)).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //存储图像完成后，拿到ImagePath图片路径
    public void onImagePathAvailable(ImageAvailableEvent.ImagePathAvailable imagePathAvailable)
    {
        Bitmap bitmap = ImageUtils.getBitmapFromPath(imagePathAvailable.getImagePath());
        if(bitmap != null)
        {
            final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 60, 60);
            iv_album_camera.setImageBitmap(thumbnail);
        }
    }

    private void showViewTakePicture()
    {
        iv_takepicture_done.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                iv_takepicture_done.setVisibility(View.GONE);
            }
        }, 200);
    }

}
