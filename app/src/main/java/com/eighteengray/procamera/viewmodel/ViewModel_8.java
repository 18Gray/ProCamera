package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.imageprocess.bean.VerticalRecyclerItem;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by lutao on 2017/3/24.
 * 图像+文字，纵向排列
 */
public class ViewModel_8 implements IViewModel<VerticalRecyclerItem> {

    @Override
    public View onCreateView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.view_model8, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, VerticalRecyclerItem verticalRecyclerItem, final int position) {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_icon_viewmodel8 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_viewmodel8);
        TextView tv_name_viewmodel8 = baseRecyclerViewHolder.getViewById(R.id.tv_title_viewmodel8);

        iv_icon_viewmodel8.setImageResource(verticalRecyclerItem.resourceId);
        tv_name_viewmodel8.setText(verticalRecyclerItem.title);
    }


}
