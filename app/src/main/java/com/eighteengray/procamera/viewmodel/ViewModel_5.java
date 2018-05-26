package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
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

        String[] items = null;
        switch (settings.funcName){
            case "图片质量":
                items = new String[4];
                items[0] = "1836p";
                items[1] = "1152p";
                items[2] = "1080p";
                items[3] = "720p";
                break;
            case "图片格式":
                items = new String[4];
                items[0] = "JPG";
                items[1] = "PNG";
                items[2] = "RAW";
                items[3] = "TIFF";
                break;
        }
        /*ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, R.layout.item_text_grid, R.id.tv_item_textgrid, items);
        spinnerAdapter.setDropDownViewResource(R.layout.item_text_grid);
        spinner_viewmodel5.setAdapter(spinnerAdapter);*/
    }


}
