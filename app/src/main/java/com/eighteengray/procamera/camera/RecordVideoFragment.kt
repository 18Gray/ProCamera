package com.eighteengray.procamera.camera

import android.hardware.camera2.CameraAccessException
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eighteengray.procamera.R
import com.eighteengray.procamera.common.JumpActivityUtils
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory
import com.eighteengray.procameralibrary.dataevent.CameraConfigure.Flash
import com.eighteengray.procameralibrary.dataevent.RecordVideoEvent
import kotlinx.android.synthetic.main.fragment_recordvideo.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class RecordVideoFragment: BaseCameraFragment() {
    private var isRecording = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_recordvideo, null)

        initView()

        EventBus.getDefault().register(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        recordTextureView.openCamera()
    }

    override fun onPause() {
        if (recordTextureView != null) {
            recordTextureView.closeCamera()
        }
        super.onPause()
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initView() {
        iv_flash_camera.setOnClickListener {
            val location1 = IntArray(2)
            iv_flash_camera.getLocationOnScreen(location1)
            PopupWindowFactory.createFlashPopupWindow(getActivity()).showAtLocation(
                iv_flash_camera,
                Gravity.NO_GRAVITY,
                location1[0] + iv_flash_camera.width,
                location1[1] - iv_flash_camera.height
            )
        }

        iv_switch_camera.setOnClickListener {
            recordTextureView.switchCamera()
        }

        recordTextureView.setOnClickListener {

        }

        tv_mode_select.setOnClickListener {
            val modeSelectDialogFragment = ModeSelectDialogFragment()
            modeSelectDialogFragment.show(requireFragmentManager(), "Camera")
        }

        iv_album_camera.setOnClickListener {
            JumpActivityUtils.jump2AlbumActivity(getActivity(), true, true, false)
        }

        iv_shutter_camera.setOnClickListener {
            if (!isRecording) {
                isRecording = true
                recordTextureView.startRecordVideo()
            } else if (isRecording) {
                isRecording = false
                recordTextureView.stopRecordVideo()
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Throws(CameraAccessException::class)
    fun onFlashSelect(flash: Flash) {
        recordTextureView.setFlashMode(flash.flash)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    @Throws(CameraAccessException::class)
    fun onRecordVideo(recordVideoEvent: RecordVideoEvent) {
        if (recordVideoEvent.isRecording) {
            iv_shutter_camera.setImageResource(R.drawable.btn_shutter_recording)
        } else {
            iv_shutter_camera.setImageResource(R.drawable.btn_shutter_record)
        }
    }

}