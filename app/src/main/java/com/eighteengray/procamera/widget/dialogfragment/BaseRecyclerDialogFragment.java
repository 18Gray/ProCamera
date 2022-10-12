package com.eighteengray.procamera.widget.dialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.eighteengray.procamera.databinding.LayoutCommonRecyclerBinding;
import androidx.fragment.app.DialogFragment;


public class BaseRecyclerDialogFragment extends DialogFragment {
    protected LayoutCommonRecyclerBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LayoutCommonRecyclerBinding.inflate(inflater);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        showRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
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

    protected void showRecyclerView() {
    }

}
