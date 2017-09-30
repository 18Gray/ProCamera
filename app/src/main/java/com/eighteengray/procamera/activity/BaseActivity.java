package com.eighteengray.procamera.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.statusbar.StatusBarHelper;
import com.eighteengray.procamera.widget.swipebacklayout.SwipeBackActivity;
import com.eighteengray.procamera.widget.swipebacklayout.SwipeBackLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * BaseActivity
 * 通用行为：顶部状态栏透明，左划finish
 */
public abstract class BaseActivity extends SwipeBackActivity
{
    protected RelativeLayout common_title;
    protected Button btn_back;
    protected TextView tv_title;
    protected Button btn_search;
    protected Button btn_right;

    protected StatusBarHelper mStatusBarHelper;
    protected SwipeBackLayout mSwipeBackLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());
        initCommonTitle();
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {

            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    protected int getLayoutResId(){
        return R.layout.activity_base;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mStatusBarHelper == null) {
            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT,StatusBarHelper.LEVEL_21_VIEW);
        }
        mStatusBarHelper.setActivityRootLayoutFitSystemWindows(true);
        mStatusBarHelper.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    protected void initCommonTitle(){
        common_title = (RelativeLayout) findViewById(R.id.common_title);
        btn_back = (Button) findViewById(R.id.btn_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_right = (Button) findViewById(R.id.btn_right);

        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(BaseActivity.this, "跳转SearchActivity", Toast.LENGTH_SHORT).show();
            }
        });

        btn_right.setVisibility(View.GONE);
    }

}
