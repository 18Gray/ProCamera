package com.eighteengray.procamera.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.eighteengray.procamera.R;



public class BaseActivity extends FragmentActivity
{
    private Toolbar mToolBar;
    private LinearLayout mDectorView = null;//根布局
    private FrameLayout mContentView = null;//activity内容布局
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (mDectorView == null)
        {
            initView();
        }
        //如果已经创建就先把内容清空，再添加
        if (mContentView != null)
        {
            mContentView.removeAllViews();//mContentview清空里面的view
        }
        initView();//初始化控件
    }

    private void initView()
    {
        //生成DecorView
        mDectorView = new LinearLayout(this);
        mDectorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mDectorView.setOrientation(LinearLayout.VERTICAL);

        //添加toolbar，把activity_toolbar的布局添加到mDectorView上
        View view = getLayoutInflater().inflate(R.layout.activity_toolbar, mDectorView);
        mToolBar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.parseColor("#ff00ff"));
        mToolBar.setNavigationIcon(R.mipmap.label_procamera);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        mContentView = (FrameLayout) mDrawerLayout.findViewById(R.id.content_frame);
        // 将传入的layout加载到activity_base的content_frame里面
        getLayoutInflater().inflate(layoutResID, mContentView, true);
        super.setContentView(mDrawerLayout);

        //生成drawer
        /*planetTitles = getResources().getStringArray(R.array.planets_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(BaseActivity.this,
                R.layout.list_item_drawer, planetTitles));*/

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

}
