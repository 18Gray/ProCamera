package com.eighteengray.procamera.widget.dialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.procamera.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BaseRecyclerDialogFragment extends DialogFragment
{
    View view;

    @BindView(R.id.recycler_layout)
    public RecyclerLayout recycler_layout;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.layout_common_recycler, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, view);
        showRecyclerView();
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //全屏显示，设置宽度为屏宽, 靠近屏幕底部
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();

        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    protected void showRecyclerView(){

    }

}
