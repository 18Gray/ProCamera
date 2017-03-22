package com.eighteengray.procamera.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eighteengray.procamera.R;


public class SubtitleActivity extends Activity
{
	LinearLayout rl_back_subtitle;
	RelativeLayout rl_add_subtitle;
	
	EditText et_subtitle;
	
	String subtitle;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_subtitle);

		initView();

		setListener();
	}
	
	
	
	private void initView()
	{
		rl_back_subtitle = (LinearLayout) findViewById(R.id.rl_back_subtitle);
		rl_add_subtitle = (RelativeLayout) findViewById(R.id.rl_add_subtitle);
		
		et_subtitle = (EditText) findViewById(R.id.et_subtitle);
		
	}

	
	
	private void setListener()
	{
		rl_back_subtitle.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		rl_add_subtitle.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				subtitle = et_subtitle.getText().toString();
				
				Intent intent = new Intent(SubtitleActivity.this, ActivityFilm.class);
				intent.putExtra("subtitle", subtitle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		
		
	}
	
	

}
