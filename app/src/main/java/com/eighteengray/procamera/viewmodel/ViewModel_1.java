package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.imageprocesslibrary.java.ImageUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.ImageProcessActivity;

/**
 * Created by lutao on 2017/3/24.
 */
public class ViewModel_1 implements IViewModel<String>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.grid_item_picture, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, final String data, int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        Bitmap bitmap = ImageUtils.getBitmapFromPath(data, 400, 400);
        ImageView imageView = baseRecyclerViewHolder.getViewById(R.id.iv_item_grid);
        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ImageProcessActivity.class);
                intent.putExtra("imagePath", data);
                context.startActivity(intent);
            }
        });
    }
}
