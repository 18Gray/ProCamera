package com.eighteengray.procamera.activity;


import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.eighteengray.procamera.R;


/**
 * 录像界面功能：关闭界面，切换摄像头；对Camera的滤镜功能；录像，记录录像时间；找到录像文件列表，点击可以查看。
 * @author lawliet
 */
public class VideoActivity extends BaseActivity implements OnClickListener
{
	public final static String TAG = "CameraAty";
	private boolean mIsRecordMode = false;
	private String mSaveRoot;
	private ImageButton mRecordShutterButton;
	private ImageView mSwitchCameraView;
	RelativeLayout rl_playrecord_videorecord;
	private boolean isRecording = false;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.videorecord);
		mRecordShutterButton = (ImageButton) findViewById(R.id.btn_shutter_record);
		rl_playrecord_videorecord = (RelativeLayout) findViewById(R.id.rl_playrecord_videorecord);
		
		
		// mThumbView.setOnClickListener(this);
		// mCameraShutterButton.setOnClickListener(this);
		mRecordShutterButton.setOnClickListener(this);
		// mFlashView.setOnClickListener(this);
		// mSwitchModeButton.setOnClickListener(this);
		// mSwitchCameraView.setOnClickListener(this);
		// mSettingView.setOnClickListener(this);
		rl_playrecord_videorecord.setOnClickListener(this);
		
		mSaveRoot = "test";
		// initThumbnail();
	}

	
	
	/**
	 * 加载缩略图
	 */
	// private void initThumbnail() {
	// String thumbFolder=FileOperateUtil.getFolderPath(this,
	// FileOperateUtil.TYPE_THUMBNAIL, mSaveRoot);
	// List<File> files=FileOperateUtil.listFiles(thumbFolder, ".jpg");
	// if(files!=null&&files.size()>0){
	// Bitmap
	// thumbBitmap=BitmapFactory.decodeFile(files.get(0).getAbsolutePath());
	// if(thumbBitmap!=null){
	// mThumbView.setImageBitmap(thumbBitmap);
	// //视频缩略图显示播放图案
	// if(files.get(0).getAbsolutePath().contains("video")){
	// mVideoIconView.setVisibility(View.VISIBLE);
	// }else {
	// mVideoIconView.setVisibility(View.GONE);
	// }
	// }
	// }else {
	// mThumbView.setImageBitmap(null);
	// mVideoIconView.setVisibility(View.VISIBLE);
	// }
	//
	// }
	
	
	
	

	@Override
	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		switch (view.getId())
		{
		// case R.id.btn_shutter_camera:
		// mCameraShutterButton.setClickable(false);
		// mContainer.takePicture(this);
		// break;
		// case R.id.btn_thumbnail:
		// startActivity(new Intent(this,AlbumAty.class));
		// break;
		// case R.id.btn_flash_mode:
		// if(mContainer.getFlashMode()==FlashMode.ON){
		// mContainer.setFlashMode(FlashMode.OFF);
		// mFlashView.setImageResource(R.drawable.btn_flash_off);
		// }else if (mContainer.getFlashMode()==FlashMode.OFF) {
		// mContainer.setFlashMode(FlashMode.AUTO);
		// mFlashView.setImageResource(R.drawable.btn_flash_auto);
		// }
		// else if (mContainer.getFlashMode()==FlashMode.AUTO) {
		// mContainer.setFlashMode(FlashMode.TORCH);
		// mFlashView.setImageResource(R.drawable.btn_flash_torch);
		// }
		// else if (mContainer.getFlashMode()==FlashMode.TORCH) {
		// mContainer.setFlashMode(FlashMode.ON);
		// mFlashView.setImageResource(R.drawable.btn_flash_on);
		// }
		// break;
		// case R.id.btn_switch_mode:
		// if(mIsRecordMode){
		// // mSwitchModeButton.setImageResource(R.drawable.ic_switch_camera);
		// mCameraShutterButton.setVisibility(View.VISIBLE);
		// mRecordShutterButton.setVisibility(View.GONE);
		// //拍照模式下显示顶部菜单
		// // mHeaderBar.setVisibility(View.VISIBLE);
		// mIsRecordMode=false;
		// mContainer.switchMode(0);
		// stopRecord();
		// }
		// else {
		// // mSwitchModeButton.setImageResource(R.drawable.ic_switch_video);
		// mCameraShutterButton.setVisibility(View.GONE);
		// mRecordShutterButton.setVisibility(View.VISIBLE);
		// //录像模式下隐藏顶部菜单
		// // mHeaderBar.setVisibility(View.GONE);
		// mIsRecordMode=true;
		// mContainer.switchMode(5);
		// }
		// break;
		case R.id.btn_shutter_record:

			
		// case R.id.btn_switch_camera:
		// mContainer.switchCamera();
		// break;
		// case R.id.btn_other_setting:
		// mContainer.setWaterMark();
		// break;
		default:
			break;
			
		}
	}


}
