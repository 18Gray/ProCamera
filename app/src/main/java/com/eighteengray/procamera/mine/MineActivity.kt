package com.eighteengray.procamera.mine

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.eighteengray.procamera.R
import com.eighteengray.procamera.widget.SnackbarUtil
import com.supaur.baseactivity.baseactivity.BaseActivity
import kotlinx.android.synthetic.main.aty_mine.*


class MineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aty_mine)
        initView()
    }

    private fun initView() {
        til_name_mine.editText
        til_name_mine.hint = "请输入姓名："
        til_password_mine.editText
        til_password_mine.hint = "请输入密码："

        var snackBarView = LayoutInflater.from(this).inflate(R.layout.view_snackbar, null)
        btn_mine.setOnClickListener(View.OnClickListener { showSnackBar() })
    }

    private fun showSnackBar() {
        val snackbar = SnackbarUtil.ShortSnackbar(cdnl_mine, "妹子删了你发出的消息", SnackbarUtil.Warning)
                .setActionTextColor(Color.RED).setAction("再次发送") {
                    SnackbarUtil.LongSnackbar(cdnl_mine, "妹子已将你拉黑", SnackbarUtil.Alert).setActionTextColor(Color.WHITE).show()
                }
        SnackbarUtil.SnackbarAddView(snackbar, R.layout.view_snackbar, 0)
        SnackbarUtil.SnackbarAddView(snackbar, R.layout.view_snackbar, 2)
        snackbar.show()
    }

}