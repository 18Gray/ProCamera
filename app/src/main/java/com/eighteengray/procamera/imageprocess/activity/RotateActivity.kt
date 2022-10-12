package com.eighteengray.procamera.imageprocess.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.eighteengray.commonutillibrary.FileUtils
import com.eighteengray.commonutillibrary.ImageUtils
import com.eighteengray.commonutillibrary.SDCardUtils
import com.eighteengray.commonutillibrary.ScreenUtils
import com.eighteengray.procamera.R
import com.eighteengray.procameralibrary.common.Constants
import com.supaur.baseactivity.baseactivity.BaseActivity
import kotlinx.android.synthetic.main.aty_cut.*
import java.io.File


class RotateActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aty_cut)
        initView()
    }

    private fun initView() {
        var width = ScreenUtils.getScreenWidth(this@RotateActivity)
        var path = intent.getStringExtra(Constants.CROPIMAGEPATH)
        var bitmap = ImageUtils.getBitmapFromPathSimple(path)
        var drawable = BitmapDrawable(bitmap)
        civ_cut.setDrawable(drawable, width - 100, width - 100)

        iv_back.setOnClickListener {
            val cutBitmap: Bitmap = civ_cut.cropImage
            val file: File = FileUtils.createFile(
                SDCardUtils.getAppFile(this@RotateActivity).absolutePath,
                "cutBitmap.jpg"
            )
            path = file.absolutePath
            ImageUtils.saveBitmap2Album(this@RotateActivity, cutBitmap)

            val mIntent = Intent()
            mIntent.putExtra(Constants.CROPIMAGEPATH, path)
            setResult(RESULT_OK, mIntent)
            finish()
        }
    }

}