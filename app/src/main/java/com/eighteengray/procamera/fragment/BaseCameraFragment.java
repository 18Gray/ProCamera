package com.eighteengray.procamera.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.eighteengray.procamera.MainActivity;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.activity.BaseListViewCollapseActivity;
import com.eighteengray.procamera.activity.MineActivity;
import com.eighteengray.procamera.activity.SettingActivity;


public class BaseCameraFragment extends Fragment
{
    public static final int REQUEST_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
//        Log.d("CameraRecordFragment", "BaseFragmentOnResumeStart");
//        checkPermission(PERMISSIONS, REQUEST_PERMISSIONS);
//        Log.d("CameraRecordFragment", "BaseFragmentOnResumeEnd");


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
                        Intent intent1 = new Intent(getActivity(), AlbumActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navi_cloudpics:
                        Intent intent2 = new Intent(getActivity(), BaseListViewCollapseActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navi_settings:
                        Intent intent3 = new Intent(getActivity(), SettingActivity.class);
                        startActivity(intent3);
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


    /*protected void checkPermission(String[] permissions, int requestCode)
    {
        if(!hasPermissionsGranted(permissions))
        {
            if (shouldShowRequestPermissionRationale(permissions))
            {
                ExplationDialogFragment.newInstance(permissions, requestCode).show(getFragmentManager(), "dialog");
            } else
            {
                Log.d("CameraRecordFragment", "requestPermissionStart");
                ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
                Log.d("CameraRecordFragment", "requestPermissionEnd");
            }

            return;
        }
    }

    private boolean hasPermissionsGranted(String[] permissions)
    {
        for (String permission : permissions)
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }


    protected boolean shouldShowRequestPermissionRationale(String[] permissions)
    {
        for (String permission : permissions)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_PERMISSIONS)
        {
            if (grantResults.length == permissions.length)
            {
                for (int result : grantResults)
                {
                    if (result != PackageManager.PERMISSION_GRANTED)
                    {
                        ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), "error");
                        break;
                    }
                }
            } else
            {
                ErrorDialogFragment.newInstance(getString(R.string.album_message)).show(getFragmentManager(), "error");
            }
        } else
        {
            Log.d("CameraRecordFragment", "onRequestPermissionsResultStart");
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            Log.d("CameraRecordFragment", "onRequestPermissionsResult");
        }
    }*/


}
