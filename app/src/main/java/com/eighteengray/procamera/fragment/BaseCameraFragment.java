package com.eighteengray.procamera.fragment;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.MineActivity;
import com.eighteengray.procamera.business.JumpActivityUtils;


public class BaseCameraFragment extends Fragment
{

    //ToolBar/DrawerLayout/Navigation
    protected AppCompatActivity activity;
    protected Toolbar toolbar;
    protected ActionBar actionBar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigation;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    public void onResume()
    {
        super.onResume();
    }

    protected void initToolDrawerNavi(){
        activity = (AppCompatActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.text));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.text));
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        actionBar = activity.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true); //设置返回键可用
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        navigation.setItemIconTintList(null);
        View headerView = navigation.getHeaderView(0);
        headerView.findViewById(R.id.iv_navigation_header).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), MineActivity.class);
                startActivity(intent);
            }
        });
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.navi_album:
                        JumpActivityUtils.jump2AlbumActivity(getActivity(), true, true, false);
                        break;
                    case R.id.navi_cloudpics:
                       JumpActivityUtils.jump2SettingActivity(getActivity());
                        break;
                    case R.id.navi_settings:
                        JumpActivityUtils.jump2SettingActivity(getActivity());
                        break;
                }
                return true;
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.action_settings, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

}
