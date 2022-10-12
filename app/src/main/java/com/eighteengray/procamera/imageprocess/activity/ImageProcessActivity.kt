package com.eighteengray.procamera.imageprocess.activity

import android.os.Bundle
import android.view.Gravity
import com.eighteengray.procamera.R
import com.eighteengray.procamera.imageprocess.widget.OutputMenuDialog
import com.eighteengray.procamera.imageprocess.widget.RedoMenuDialog
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent
import com.supaur.baseactivity.baseactivity.BaseActivity
import kotlinx.android.synthetic.main.aty_cut.iv_right
import kotlinx.android.synthetic.main.aty_image_process.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ImageProcessActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aty_image_process)
        initView()
    }

    private fun initView() {
        val bundle = intent.extras
        var imagePath = bundle!!.getString(Constants.IMAGE_PATH)

        EventBus.getDefault().register(this)

        iv_right.setBackgroundResource(R.mipmap.redo_menu_white_24dp)
        iv_right.setOnClickListener {
            val redoMenuDialog = RedoMenuDialog()
            redoMenuDialog.show(supportFragmentManager, "redo")
        }

//        ImageLoader.getInstance().with(this@ImageProcessActivity).load(File(imagePath)).into(imageview_process).execute()

        tv_filter_image_process.setOnClickListener {
            //                PopupWindowFactory.createFilterPopupWindow(ImageProcessActivity.this).showAtLocation(ll_bottommenu_image_process, Gravity.BOTTOM, 0, 288);
//            JumpActivityUtils.jump2FilterActivity(this)
        }

        tv_tools_image_process.setOnClickListener {
            PopupWindowFactory.createProcessPopupWindow(this@ImageProcessActivity)
                .showAtLocation(ll_bottommenu_image_process, Gravity.BOTTOM, 0, 288)
        }

        tv_output_image_process.setOnClickListener {
            val outputMenuDialog = OutputMenuDialog()
            outputMenuDialog.show(supportFragmentManager, "output")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageFolderSelected(imageFolderEvent: ImageFolderEvent) {
        val currentImageFolderNum = imageFolderEvent.currentImageFolderNum
    }

}