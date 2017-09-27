package com.eighteengray.procamera.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
public class BaseActivity extends SwipeBackActivity
{
    @BindView(R.id.common_title)
    protected RelativeLayout common_title;
    @BindView(R.id.btn_back)
    protected Button btn_back;
    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.btn_search)
    protected Button btn_search;
    @BindView(R.id.btn_right)
    protected Button btn_right;

    protected StatusBarHelper mStatusBarHelper;
    protected SwipeBackLayout mSwipeBackLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);

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


    @OnClick({R.id.btn_back, R.id.btn_search, R.id.btn_right})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
                break;

            case R.id.btn_right:
                break;
        }
    }

}
