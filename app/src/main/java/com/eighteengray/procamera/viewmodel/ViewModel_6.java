package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;
import com.rey.material.widget.Switch;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lutao on 2017/3/24.
 * 设置中的是否开启某功能
 */
public class ViewModel_6 implements IViewModel<Settings>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model6, null);
    }

    @Override
    public void onBindView(Context context, RecyclerView.ViewHolder holder, Settings settings, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        TextView tv_view_model6 = baseRecyclerViewHolder.getViewById(R.id.tv_view_model6);
        Switch switch_viewmodel6 = baseRecyclerViewHolder.getViewById(R.id.switch_viewmodel6);

        tv_view_model6.setText(settings.funcName);
    }


}
