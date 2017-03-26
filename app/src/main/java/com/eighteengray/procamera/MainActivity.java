package com.eighteengray.procamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.fragment.ConfirmationDialogFragment;
import com.eighteengray.procamera.fragment.ErrorDialogFragment;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerAdapter;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerViewHolder;
import com.eighteengray.procameralibrary.camera.Camera2TextureView;
import com.eighteengray.procameralibrary.camera.IRequestPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.eighteengray.procamera.fragment.ConfirmationDialogFragment.REQUEST_CAMERA_PERMISSION;



public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback
{
	private static final String FRAGMENT_DIALOG = "dialog";
	//上部
	@BindView(R.id.iv_flash_camera)
	ImageView iv_flash_camera;
	@BindView(R.id.tv_mode_gpufileter)
	TextView tv_mode_gpufileter;
	@BindView(R.id.iv_switch_camera)
	ImageView iv_switch_camera;

	//中部相机拍照区域
	@BindView(R.id.texture)
	public Camera2TextureView mTextureView;

	//中下部
	@BindView(R.id.rl_middle_bottom_menu)
	RelativeLayout rl_middle_bottom_menu;
	@BindView(R.id.recyclerview_mode)
	RecyclerView recyclerview_mode;

	//下部
	@BindView(R.id.iv_album_camera)
	ImageView iv_album_camera;
	@BindView(R.id.iv_ratio_camera)
	ImageView iv_ratio_camera;
	@BindView(R.id.iv_shutter_camera)
	ImageView iv_shutter_camera;
	@BindView(R.id.iv_gpufilter_camera)
	ImageView iv_gpufilter_camera;
	@BindView(R.id.iv_setting_camera)
	ImageView iv_setting_camera;
	

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
			public void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, String item)
			{
				TextView textView = baseRecyclerViewHolder.getViewById(R.id.tv_item_recycler);
				textView.setText(item);
			}
		};
		recyclerview_mode.setAdapter(adapter);
		List<String> list = new ArrayList<>();
		list.add("视频");
		list.add("照片");
		list.add("全景");
		adapter.setData(list);
	}


	@Override
	public void onResume()
	{
		super.onResume();
		mTextureView.setIRequestPermission(new IRequestPermission()
		{
			@Override
			public void requestPermissionSuccess()
			{
				requestCameraPermission();
			}

			@Override
			public void requestPermissionFail()
			{
				ErrorDialogFragment.newInstance(getString(R.string.api_cancel)).show(getFragmentManager(), FRAGMENT_DIALOG);
			}
		});
		mTextureView.openCamera();
	}


	@Override
	public void onPause()
	{
		mTextureView.closeCamera();
		super.onPause();
	}



	private void requestCameraPermission()
	{
		if (android.support.v13.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
		{
			new ConfirmationDialogFragment().show(getFragmentManager(), FRAGMENT_DIALOG);
		} else
		{
			android.support.v13.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
		}
	}



	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if (requestCode == REQUEST_CAMERA_PERMISSION)
		{
			if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
			{
				ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), FRAGMENT_DIALOG);
			}
		} else
		{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}



	@OnClick({R.id.iv_album_camera, R.id.iv_ratio_camera,
	R.id.iv_shutter_camera, R.id.iv_gpufilter_camera, R.id.iv_setting_camera})
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.iv_album_camera:
				Intent intent1 = new Intent(MainActivity.this, AlbumActivity.class);
				startActivity(intent1);
				break;
			case R.id.iv_ratio_camera:
				rl_middle_bottom_menu.setVisibility(View.VISIBLE);
				break;
			case R.id.iv_shutter_camera:
				mTextureView.takePicture();
				break;
			case R.id.iv_gpufilter_camera:

				break;
			case R.id.iv_setting_camera:
				Intent intent2 = new Intent(MainActivity.this, AlbumActivity.class);
				startActivity(intent2);
				break;
		}
	}



}