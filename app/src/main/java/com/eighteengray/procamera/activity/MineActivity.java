package com.eighteengray.procamera.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lutao on 2016/7/20.
 */
public class MineActivity extends Activity
{
    Context context;
    View snackBarView;

    @BindView(R.id.cdnl_mine)
    CoordinatorLayout cdnl_mine;

    @BindView(R.id.til_name_mine)
    TextInputLayout til_name_mine;
    EditText et_name;

    @BindView(R.id.til_password_mine)
    TextInputLayout til_password_mine;
    EditText et_password;

    @BindView(R.id.btn_mine)
    Button btn_mine;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_mine);
        ButterKnife.bind(this);
        context = this;

        et_name = til_name_mine.getEditText();
        til_name_mine.setHint("请输入姓名：");
        et_password = til_password_mine.getEditText();
        til_password_mine.setHint("请输入密码：");

        snackBarView = LayoutInflater.from(context).inflate(R.layout.view_snackbar, null);
        btn_mine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showSnackBar();
            }
        });
    }


    private void showSnackBar()
    {
        Snackbar snackbar = SnackbarUtil.ShortSnackbar(cdnl_mine, "妹子删了你发出的消息", SnackbarUtil.Warning).setActionTextColor(Color.RED).setAction("再次发送", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SnackbarUtil.LongSnackbar(cdnl_mine, "妹子已将你拉黑", SnackbarUtil.Alert).setActionTextColor(Color.WHITE).show();
            }
        });
        SnackbarUtil.SnackbarAddView(snackbar, R.layout.view_snackbar, 0);
        SnackbarUtil.SnackbarAddView(snackbar, R.layout.view_snackbar, 2);
        snackbar.show();
    }


}
