package com.eighteengray.procamera.camera;


import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.common.JumpActivityUtils;
import com.eighteengray.procamera.mine.MineActivity;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


public class BaseCameraFragment extends Fragment {
    protected AppCompatActivity activity;
    protected Toolbar toolbar;
    protected ActionBar actionBar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigation;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    protected void initToolDrawerNavi() {
        activity = (AppCompatActivity) getActivity();

        toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.text));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.text));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        headerView.findViewById(R.id.iv_navigation_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MineActivity.class);
                startActivity(intent);
            }
        });
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.navi_album:
                        JumpActivityUtils.jump2AlbumActivity(getActivity(), true, true, false);
                        break;
                    case R.id.navi_cloudpics:
                       JumpActivityUtils.jump2WebViewActivity(getActivity());
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
