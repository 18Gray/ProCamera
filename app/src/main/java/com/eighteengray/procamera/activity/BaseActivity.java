package com.eighteengray.procamera.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.eighteengray.procamera.widget.statusbar.StatusBarHelper;
import com.eighteengray.procamera.widget.swipebacklayout.SwipeBackActivity;
import com.eighteengray.procamera.widget.swipebacklayout.SwipeBackLayout;


/**
 * BaseActivity
 * 通用行为：顶部状态栏透明，左划finish
 */
public abstract class BaseActivity extends SwipeBackActivity
{

    protected StatusBarHelper mStatusBarHelper;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());
        if (mStatusBarHelper == null) {
            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT,StatusBarHelper.LEVEL_21_VIEW);
        }
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


    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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



    abstract protected int getLayoutResId();

}
