package com.eighteengray.procamera.widget.dialogfragment;

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
import com.eighteengray.procamera.imageprocess.bean.ImageProcessToolsMenuItem;
import com.eighteengray.procameralibrary.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageProcessToolsDialogFragment extends DialogFragment
{
    View view;

    @BindView(R.id.rl_imageprocess_tools_dialogfragment)
    RecyclerLayout rl_imageprocess_tools_dialogfragment;

    List<ImageProcessToolsMenuItem> toolsMenuItems;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        toolsMenuItems = (ArrayList<ImageProcessToolsMenuItem>) bundle.getSerializable(Constants.IMAGEPROCESS_TOOLS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //全屏显示
        Window window = getDialog().getWindow();
        view = inflater.inflate(R.layout.dialogfragment_imageprocess_tools, null);
        ButterKnife.bind(this, view);

        // 设置宽度为屏宽, 靠近屏幕底部。
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        getDialog().setCancelable(true);

        toolsMenuItems = GenerateDataUtils.generateToolMenuItemList();
        rl_imageprocess_tools_dialogfragment.showRecyclerView(GenerateDataUtils.generateDataBeanList(8, toolsMenuItems), Constants.viewModelPackage);
        return view;
    }


}
