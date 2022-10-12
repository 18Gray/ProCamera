package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.procamera.MainActivity;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.album.ImageItem;
import com.eighteengray.procamera.common.JumpActivityUtils;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by lutao on 2017/3/24.
 * ImageView + Checkbox
 */
public class ViewModel_1 implements IViewModel<ImageItem> {

    @Override
    public View onCreateView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.grid_item_picture, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, final ImageItem imageItem, int position) {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_item_grid = baseRecyclerViewHolder.getViewById(R.id.iv_item_grid);
        ImageView iv_checkbox_item_grid = baseRecyclerViewHolder.getViewById(R.id.iv_checkbox_item_grid);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_item_grid.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = ScreenUtils.getScreenWidth(context) / 3;
        iv_item_grid.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(imageItem.getImagePath())) {
            Glide.with(context).load(imageItem.getImagePath()).into(iv_item_grid);
        } else {
            Glide.with(context).load(imageItem.getResource()).into(iv_item_grid);
        }

        iv_item_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(imageItem.getImagePath())) {
                    JumpActivityUtils.jump2ImageProcessActivity(context, imageItem.getImagePath());
                }else { // 跳转相机
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }

            }
        });

        if(imageItem.getShowCheckBox()){
            iv_checkbox_item_grid.setVisibility(View.VISIBLE);
            if(imageItem.isChecked()){
                iv_checkbox_item_grid.setImageResource(R.mipmap.cb_album_checked);
            }else {
                iv_checkbox_item_grid.setImageResource(R.mipmap.cb_album_normal);
            }
        }else {
            iv_checkbox_item_grid.setVisibility(View.GONE);
        }
    }
}
