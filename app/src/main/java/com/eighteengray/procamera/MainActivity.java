package com.eighteengray.procamera;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug;
import android.view.WindowManager;
import com.eighteengray.procamera.camera.Camera2Fragment;
import com.eighteengray.procamera.camera.RecordVideoFragment;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.ModeSelectEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    Camera2Fragment camera2Fragment;
    RecordVideoFragment recordVideoFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.startMethodTracing("ProCamera");
        //去掉status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_main);

        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()//开启所有的detectXX系列方法
                .penaltyDialog()//弹出违规提示框
                .penaltyLog()//在Logcat中打印违规日志
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()//检测Activity泄露
                .penaltyLog()//在Logcat中打印违规日志
                .build());*/
        EventBus.getDefault().register(this);
        initFragment();
    }


    private void initFragment() {
        camera2Fragment = new Camera2Fragment();
        recordVideoFragment = new RecordVideoFragment();
        replaceFragment(camera2Fragment);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_aty_main, fragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Debug.stopMethodTracing();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(ModeSelectEvent modeSelectEvent) {
        switch (modeSelectEvent.getMode()) {
            case Constants.MODE_CAMERA:
                replaceFragment(camera2Fragment);
                break;

            case Constants.MODE_RECORD:
                replaceFragment(recordVideoFragment);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        camera2Fragment.actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        camera2Fragment.actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

}