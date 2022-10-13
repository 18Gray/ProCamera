package com.eighteengray.procamera.widget.dialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.eighteengray.procamera.R
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.ModeSelectEvent
import kotlinx.android.synthetic.main.dialogfragment_modeselect.*
import org.greenrobot.eventbus.EventBus


class ModeSelectDialogFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialogfragment_modeselect, null)

        //全屏显示
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(-1, -2)

        initView()

        return view
    }

    private fun initView() {
        ll_camera_modeselect.setOnClickListener {
            val modeSelectEvent1 = ModeSelectEvent()
            modeSelectEvent1.mode = Constants.MODE_CAMERA
            EventBus.getDefault().post(modeSelectEvent1)
            dismiss()
        }

        ll_video_modeselect.setOnClickListener {
            val modeSelectEvent2 = ModeSelectEvent()
            modeSelectEvent2.mode = Constants.MODE_RECORD
            EventBus.getDefault().post(modeSelectEvent2)
            dismiss()
        }


    }

}

