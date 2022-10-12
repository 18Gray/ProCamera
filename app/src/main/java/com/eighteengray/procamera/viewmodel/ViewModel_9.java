package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.imageprocess.bean.HorizontalRecyclerItem;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by lutao on 2017/3/24.
 * icon+title+content，横向排列
 */
public class ViewModel_9 implements IViewModel<HorizontalRecyclerItem> {

    @Override
    public View onCreateView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.view_model9, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, HorizontalRecyclerItem horizontalRecyclerItem, final int position) {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_icon_viewmodel9 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_viewmodel9);
        TextView tv_title_viewmodel9 = baseRecyclerViewHolder.getViewById(R.id.tv_title_viewmodel9);
        TextView tv_content_viewmodel9 = baseRecyclerViewHolder.getViewById(R.id.tv_content_viewmodel9);

        iv_icon_viewmodel9.setImageResource(horizontalRecyclerItem.resourceId);
        tv_title_viewmodel9.setText(horizontalRecyclerItem.title);
        tv_content_viewmodel9.setText(horizontalRecyclerItem.content);
    }


}
