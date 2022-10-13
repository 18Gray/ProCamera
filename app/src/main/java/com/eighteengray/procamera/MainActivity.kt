package com.eighteengray.procamera

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.eighteengray.procamera.camera.Camera2Fragment
import com.eighteengray.procamera.camera.RecordVideoFragment
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.ModeSelectEvent
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity: FragmentActivity() {
    private var camera2Fragment: Camera2Fragment? = null
    private var recordVideoFragment: RecordVideoFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //去掉status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        initFragment()
    }

    private fun initFragment() {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)
            .onGranted {
                camera2Fragment = Camera2Fragment()
                recordVideoFragment = RecordVideoFragment()
                replaceFragment(camera2Fragment)
            }
            .onDenied {

            }
            .start()
    }

    private fun replaceFragment(fragment: Fragment?) {
        if(fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onModeSwitch(modeSelectEvent: ModeSelectEvent) {
        when (modeSelectEvent.mode) {
            Constants.MODE_CAMERA -> replaceFragment(camera2Fragment)
            Constants.MODE_RECORD -> replaceFragment(recordVideoFragment)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}