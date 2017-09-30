package com.eighteengray.procamera.card.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.bean.SaveImage;
import com.eighteengray.procamera.card.baserecycler.BaseRecyclerViewHolder;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;

import org.greenrobot.eventbus.EventBus;

import static com.eighteengray.procamera.R.id.rl_item_dirlsit;

/**
 * Created by lutao on 2017/3/24.
 */
public class ViewModel_ImageFolder implements IViewModel<ImageFolder>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.list_dir_item, null);
    }

    @Override
    public void onBindView(Context context, RecyclerView.ViewHolder holder, ImageFolder data, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        RelativeLayout rl_item_dirlsit = baseRecyclerViewHolder.getViewById(R.id.rl_item_dirlsit);
        ImageView iv_image_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.iv_image_item_dirlist);
        TextView tv_name_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.tv_name_item_dirlist);
        TextView tv_count_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.tv_count_item_dirlist);
        ImageView iv_choosen_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.iv_choosen_item_dirlist);

        Bitmap bitmap = ImageUtils.getBitmapFromPath(data.getFirstImagePath());
        iv_image_item_dirlist.setImageBitmap(bitmap);
        tv_name_item_dirlist.setText(data.getName());
        tv_count_item_dirlist.setText(data.getImagePathList().size() + "张");
        iv_choosen_item_dirlist.setVisibility(data.isSelected() ? View.VISIBLE : View.GONE);

        rl_item_dirlsit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //返回AlbumActivity
                ImageFolderEvent imageFolderEvent = new ImageFolderEvent();
                imageFolderEvent.setCurrentImageFolderNum(position);
                EventBus.getDefault().post(imageFolderEvent);
            }
        });
    }


}
