package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procamera.imageprocess.bean.ImageProcessToolsMenuItem;
import com.rey.material.widget.Button;


/**
 * Created by lutao on 2017/3/24.
 * 设置中的图片质量这类
 */
public class ViewModel_8 implements IViewModel<ImageProcessToolsMenuItem>
{

    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model7, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, ImageProcessToolsMenuItem imageProcessToolsMenuItem, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_icon_viewmodel8 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_viewmodel8);
        TextView tv_name_viewmodel8 = baseRecyclerViewHolder.getViewById(R.id.tv_name_viewmodel8);

        iv_icon_viewmodel8.setImageResource(imageProcessToolsMenuItem.resourceId);
        tv_name_viewmodel8.setText(imageProcessToolsMenuItem.name);
    }


}
