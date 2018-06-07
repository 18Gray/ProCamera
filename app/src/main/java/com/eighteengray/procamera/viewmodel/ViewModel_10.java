package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.imageprocess.bean.ImageFilterMenuItem;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lutao on 2017/3/24.
 */
public class ViewModel_10 implements IViewModel<ImageFilterMenuItem>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model8, null);
    }

    @Override
    public void onBindView(Context context, RecyclerView.ViewHolder holder, ImageFilterMenuItem data, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_icon_viewmodel8 = baseRecyclerViewHolder.getViewById(R.id.iv_image_item_dirlist);
        TextView tv_name_viewmodel8 = baseRecyclerViewHolder.getViewById(R.id.tv_name_viewmodel8);

        iv_icon_viewmodel8.setImageResource(data.resourceId);
        tv_name_viewmodel8.setText(data.filter);
    }


}
