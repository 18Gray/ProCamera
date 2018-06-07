package com.eighteengray.procamera.imageprocess.widget;

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
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.business.GenerateDataUtils;
import com.eighteengray.procamera.imageprocess.bean.ImageFilterMenuItem;
import com.eighteengray.procameralibrary.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterMenuDialog extends DialogFragment
{
    View view;

    @BindView(R.id.rl_filter_menu_dialogfragment)
    RecyclerLayout rl_filter_menu_dialogfragment;

    List<ImageFilterMenuItem> imageFilterMenuItems;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        imageFilterMenuItems = GenerateDataUtils.generateFilterMenuList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //全屏显示
        Window window = getDialog().getWindow();
        view = inflater.inflate(R.layout.dialogfragment_filter_menu, null);
        ButterKnife.bind(this, view);

        // 设置宽度为屏宽, 靠近屏幕底部。
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        getDialog().setCancelable(true);

        rl_filter_menu_dialogfragment.showRecyclerView(GenerateDataUtils.generateDataBeanList(10, imageFilterMenuItems), Constants.viewModelPackage);
        return view;
    }


}
