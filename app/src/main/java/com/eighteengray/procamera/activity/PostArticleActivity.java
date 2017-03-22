package com.eighteengray.procamera.activity;

import java.util.ArrayList;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.adapter.GridImageAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;





public class PostArticleActivity extends Activity implements OnClickListener
{
	private Context mContext;
	private RelativeLayout rl_back_activity_postarticle, rl_post_postarticle;
	GridView gv_pics_postartical;
	private EditText et_postarticle;
	TextView tv_linshibaocun_postarticle;
	TextView tv_weizhi_postarticle, tv_deslocation_postarticle;
	ToggleButton tb_sign_postarticle;
	ToggleButton tb_savelocal_postarticle;

	//接收到所有图片路径，用于合并多个图片
	ArrayList<String> selectedPaths = new ArrayList<String>();
	//所有缩略图
	ArrayList<Bitmap> horizontalBitmaps = new ArrayList<Bitmap>();
	GridImageAdapter gridImageAdapter = null;

	// 当前内存中的东西
	String mobile;
	Bitmap currentThumbnail;
	Bitmap currentBitmap;
	String nickName;
	//上传的所有图像文件信息
	StringBuilder picMeta = new StringBuilder();
	Bitmap logoBitmap;
	
	String paths;
	String content;
	

	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			}
		}
	};

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postarticle);
		mContext = this;

		initView();

	}

	
	
	public void initView()
	{
		rl_back_activity_postarticle = (RelativeLayout) findViewById(R.id.rl_back_activity_postarticle);
		rl_post_postarticle = (RelativeLayout) findViewById(R.id.rl_post_postarticle);
		
		gv_pics_postartical = (GridView) findViewById(R.id.gv_pics_postartical);

		et_postarticle = (EditText) findViewById(R.id.et_postarticle);

		tv_linshibaocun_postarticle = (TextView) findViewById(R.id.tv_linshibaocun_postarticle);

		tv_weizhi_postarticle = (TextView) findViewById(R.id.tv_weizhi_postarticle);
		tv_deslocation_postarticle = (TextView) findViewById(R.id.tv_deslocation_postarticle);

		tb_sign_postarticle = (ToggleButton) findViewById(R.id.tb_sign_postarticle);
		tb_savelocal_postarticle = (ToggleButton) findViewById(R.id.tb_savelocal_postarticle);

		// setOnClickListener
		rl_back_activity_postarticle.setOnClickListener(this);
		rl_post_postarticle.setOnClickListener(this);

		
		gridImageAdapter = new GridImageAdapter(mContext, horizontalBitmaps);
		gv_pics_postartical.setAdapter(gridImageAdapter);

		et_postarticle.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub
				int number = 140 - s.length();
			}
		});

		tv_linshibaocun_postarticle.setOnClickListener(this);

		tv_deslocation_postarticle.setText("北京");
	}


	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		}
	}


	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		System.gc();
	}
	
}
