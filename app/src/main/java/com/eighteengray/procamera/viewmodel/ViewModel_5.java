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
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;
import com.rey.material.widget.Spinner;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lutao on 2017/3/24.
 * 设置中的图片质量这类
 */
public class ViewModel_5 implements IViewModel<Settings>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model5, null);
    }

    @Override
    public void onBindView(Context context, RecyclerView.ViewHolder holder, Settings settings, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        TextView tv_view_model5 = baseRecyclerViewHolder.getViewById(R.id.tv_view_model5);
        Spinner spinner_viewmodel5 = baseRecyclerViewHolder.getViewById(R.id.spinner_viewmodel5);

        tv_view_model5.setText(settings.funcName);

    }


}
