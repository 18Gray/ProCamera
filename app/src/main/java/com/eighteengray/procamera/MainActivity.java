package com.eighteengray.procamera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.activity.GpuFilterActivity;
import com.eighteengray.procamera.activity.SettingActivity;
import com.eighteengray.procamera.common.Constants;
import com.eighteengray.procamera.widget.dialogfragment.ExplationDialogFragment;
import com.eighteengray.procamera.widget.dialogfragment.ErrorDialogFragment;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerAdapter;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerViewHolder;
import com.eighteengray.procamera.widget.dialogfragment.SetDelayTimeDialogFragment;
import com.eighteengray.procamera.widget.dialogfragment.SetRatioDialogFragment;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import com.eighteengray.procameralibrary.camera.IRequestPermission;
import com.eighteengray.procameralibrary.camera.RecordTextureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.eighteengray.procameralibrary.camera.BaseCamera2TextureView.REQUEST_CAMERA_PERMISSION;
import static com.eighteengray.procameralibrary.camera.BaseCamera2TextureView.REQUEST_RECORD_PERMISSION;
import static com.eighteengray.procameralibrary.camera.BaseCamera2TextureView.REQUEST_WRITESTORAGE_PERMISSION;


public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback
{
    //上部
    @BindView(R.id.iv_flash_camera)
    ImageView iv_flash_camera;
    @BindView(R.id.tv_mode_gpufileter)
    TextView tv_mode_gpufileter;
    @BindView(R.id.iv_switch_camera)
    ImageView iv_switch_camera;

    //中部相机拍照录像区域
    @BindView(R.id.cameraTextureView)
    public Camera2TextureView cameraTextureView;
    @BindView(R.id.recordTextureView)
    public RecordTextureView recordTextureView;
    @BindView(R.id.ll_histogram)
    LinearLayout ll_histogram;

    //中下部
    @BindView(R.id.rl_middle_bottom_menu)
    RelativeLayout rl_middle_bottom_menu;
    @BindView(R.id.iv_extra_camera)
    ImageView iv_extra_camera;
    @BindView(R.id.recyclerview_mode)
    RecyclerView recyclerview_mode;
    @BindView(R.id.iv_gpufilter_camera)
    ImageView iv_gpufilter_camera;

    //下部
    @BindView(R.id.iv_album_camera)
    ImageView iv_album_camera;
    @BindView(R.id.iv_ratio_camera)
    ImageView iv_ratio_camera;
    @BindView(R.id.iv_shutter_camera)
    ImageView iv_shutter_camera;
    @BindView(R.id.iv_shutter_record)
    ImageView iv_shutter_record;
    @BindView(R.id.iv_delay_shutter)
    ImageView iv_delay_shutter;
    @BindView(R.id.iv_setting_camera)
    ImageView iv_setting_camera;

    boolean isRecording = false;
    boolean isFront = false;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //去掉status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        setContentView(R.layout.activity_camera);
        initView();
        ButterKnife.bind(this);
    }


    private void initView()
    {
        //RecyclerView的三种模式，对应三种UI，三种行为及其对应的presenter，lib中提供底层实现方法，相当于model层。
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_mode = (RecyclerView) findViewById(R.id.recyclerview_mode);
        recyclerview_mode.setLayoutManager(layoutManager);
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(R.layout.item_recycler_main)
        {
            @Override
            public void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, final String item)
            {
                TextView textView = baseRecyclerViewHolder.getViewById(R.id.tv_item_recycler);
                textView.setText(item);
                textView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (item.equals("照片"))
                        {
                            updateCameraView();
                        } else if (item.equals("视频"))
                        {
                            updateRecordView();
                        }
                    }
                });
            }
        };
        recyclerview_mode.setAdapter(adapter);
        List<String> list = new ArrayList<>();
        list.add("照片");
        list.add("视频");
        adapter.setData(list);
    }


    @OnClick({R.id.iv_flash_camera, R.id.iv_switch_camera,
            R.id.iv_extra_camera, R.id.iv_gpufilter_camera,
            R.id.iv_album_camera, R.id.iv_ratio_camera, R.id.iv_shutter_camera, R.id.iv_delay_shutter, R.id.iv_setting_camera})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_flash_camera:
                cameraTextureView.setFlashMode(0);
                recordTextureView.setFlashMode(1);
                break;

            case R.id.iv_switch_camera:
                if(isFront)
                {
                    cameraTextureView.switchCamera(true);
                    recordTextureView.switchCamera(true);
                }
                else
                {
                    cameraTextureView.switchCamera(false);
                    recordTextureView.switchCamera(false);
                }
                break;

            case R.id.iv_gpufilter_camera:
                Intent intent1 = new Intent(MainActivity.this, GpuFilterActivity.class);
                startActivity(intent1);
                break;

            case R.id.iv_album_camera:
                Intent intent2 = new Intent(MainActivity.this, AlbumActivity.class);
                startActivity(intent2);
                break;

            case R.id.iv_ratio_camera:
                SetRatioDialogFragment.newInstance("SetRatio", Constants.REQUESTCODE_RATIO).show(getFragmentManager(), "SetRatio");
                break;

            case R.id.iv_shutter_camera:
                cameraTextureView.takePicture();
                break;

            case R.id.iv_shutter_record:
                if(!isRecording)
                {
                    recordTextureView.startRecordVideo();
                    Toast.makeText(MainActivity.this, "正在录制", Toast.LENGTH_SHORT).show();
                }
                else {
                    recordTextureView.stopRecordVideo();
                    Toast.makeText(MainActivity.this, "录制结束", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_delay_shutter:
                SetDelayTimeDialogFragment.newInstance("SetDelay", Constants.REQUESTCODE_DELAYTIME).show(getFragmentManager(), "SetDelay");
                break;

            case R.id.iv_setting_camera:
                Intent intent3 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent3);
                break;
        }
    }


    //最后处理权限
    @Override
    public void onResume()
    {
        super.onResume();
        if (cameraTextureView.getVisibility() == View.VISIBLE)
        {
            cameraTextureView.setIRequestPermission(new IRequestPermission()
            {
                @Override
                public void requestPermission(String permission, int requestCode)
                {
                    //权限说明对话框
                    if (android.support.v13.app.ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                    {
                        ExplationDialogFragment.newInstance(permission, requestCode).show(getFragmentManager(), permission);
                    } else //获取权限对话框
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                    }
                }
            });
            cameraTextureView.openCamera();
        } else if (recordTextureView.getVisibility() == View.VISIBLE)
        {
            recordTextureView.setIRequestPermission(new IRequestPermission()
            {
                @Override
                public void requestPermission(String permission, int requestCode)
                {
                    //权限说明对话框
                    if (android.support.v13.app.ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                    {
                        ExplationDialogFragment.newInstance(permission, requestCode).show(getFragmentManager(), permission);
                    } else //获取权限对话框
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                    }
                }
            });
            recordTextureView.openCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), permissions[0]);
            }
        } else if (requestCode == REQUEST_WRITESTORAGE_PERMISSION)
        {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), permissions[1]);
            }
        } else if (requestCode == REQUEST_RECORD_PERMISSION)
        {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), permissions[2]);
            }
        } else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onPause()
    {
        cameraTextureView.closeCamera();
        recordTextureView.closeCamera();
        super.onPause();
    }


    private void updateCameraView()
    {
        cameraTextureView.setVisibility(View.VISIBLE);
        recordTextureView.setVisibility(View.GONE);

        ll_histogram.setVisibility(View.VISIBLE);
        iv_shutter_camera.setVisibility(View.VISIBLE);
        iv_shutter_record.setVisibility(View.GONE);
    }


    private void updateRecordView()
    {
        cameraTextureView.setVisibility(View.GONE);
        recordTextureView.setVisibility(View.VISIBLE);

        ll_histogram.setVisibility(View.GONE);
        iv_shutter_camera.setVisibility(View.GONE);
        iv_shutter_record.setVisibility(View.VISIBLE);
    }

}