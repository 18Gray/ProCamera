package com.eighteengray.procamera.activity;


import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.eighteengray.commonutillibrary.DataConvertUtil;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.CropImageView;
import com.eighteengray.procameralibrary.common.Constants;


public class CutActivity extends BaseActivity implements OnClickListener
{
	ImageView iv_finish_cut;
	Button button_next_gallery;
	
	private CropImageView mCropImage;
	private Drawable drawable;
	private Bitmap bitmap = null;
	
	String mode;
	String path;
	
	
	int width;
	
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what)
			{
			case Constants.CUTPIC:
				Intent mIntent = new Intent();
				mIntent.putExtra("cropImagePath", path);
				setResult(RESULT_OK, mIntent);
				finish();
				break;

			default:
				break;
			}
			
		}
		
	};
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_cut);
		
		width = ScreenUtils.getScreenWidth(CutActivity.this);

		mode = getIntent().getStringExtra("mode");
		path = getIntent().getStringExtra("path");
		bitmap = ImageUtils.getBitmapFromPath(path);
		drawable = DataConvertUtil.bitmap2Drawable(bitmap);
		
		initView();
	}
	
	
	
	
	private void initView()
	{
		iv_finish_cut = (ImageView) findViewById(R.id.iv_finish_cut);
		button_next_gallery = (Button) findViewById(R.id.button_next_gallery);
		
		mCropImage = (CropImageView) findViewById(R.id.civ_cut);
		if(mode.equals("大片") && bitmap != null)
		{
			mCropImage.setDrawable(drawable, width, width*9/32);
		}
		else if (mode.equals("普通") && bitmap != null) 
		{
			mCropImage.setDrawable(drawable, width, width/2);
		}
		
		
		iv_finish_cut.setOnClickListener(this);
		
		button_next_gallery.setOnClickListener(this);
		
	}
	
	


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.iv_finish_cut:
			finish();
			break;
			
			
		case R.id.button_next_gallery:
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					bitmap = mCropImage.getCropImage();
					File file = new File(path);
					ImageUtils.saveBitmap(bitmap, file.getParent().toString(), file.getName().toString());
					
					handler.sendEmptyMessage(Constants.CUTPIC);
					
				}
			}).start();
			break;
				
			
		default:
			break;
		}
	}
	
	
	
}
