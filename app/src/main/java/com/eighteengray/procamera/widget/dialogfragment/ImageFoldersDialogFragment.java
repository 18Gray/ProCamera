package com.eighteengray.procamera.widget.dialogfragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.dataevent.ImageFolderEvent;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerAdapter;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerViewHolder;
import com.eighteengray.procameralibrary.common.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageFoldersDialogFragment extends DialogFragment
{
    View view;

    @BindView(R.id.rcv_imagefolders_dialogfragment)
    RecyclerView rcv_imagefolders_dialogfragment;

    BaseRecyclerAdapter<ImageFolder> imageFoldersAdapter;
    ArrayList<ImageFolder> imageFolderArrayList;
    int currentImageFolderNum;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imageFolderArrayList = (ArrayList<ImageFolder>) bundle.getSerializable(Constants.IMAGEFOLDERS);
        currentImageFolderNum = bundle.getInt(Constants.CURRENTFOLDERNUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        //全屏显示
        Window window = getDialog().getWindow();
        view = inflater.inflate(R.layout.dialogfragment_imagefolders, null);
        ButterKnife.bind(this, view);

        // 设置宽度为屏宽, 靠近屏幕底部。
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        getDialog().setCancelable(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv_imagefolders_dialogfragment.setLayoutManager(linearLayoutManager);
        imageFoldersAdapter = new BaseRecyclerAdapter<ImageFolder>(R.layout.list_dir_item)
        {
            @Override
            public void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, final ImageFolder item, final int position)
            {
                RelativeLayout rl_item_dirlsit = baseRecyclerViewHolder.getViewById(R.id.rl_item_dirlsit);
                ImageView iv_image_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.iv_image_item_dirlist);
                TextView tv_name_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.tv_name_item_dirlist);
                TextView tv_count_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.tv_count_item_dirlist);
                ImageView iv_choosen_item_dirlist = baseRecyclerViewHolder.getViewById(R.id.iv_choosen_item_dirlist);

                Bitmap bitmap = ImageUtils.getBitmapFromPath(item.getFirstImagePath());
                iv_image_item_dirlist.setImageBitmap(bitmap);
                tv_name_item_dirlist.setText(item.getName());
                tv_count_item_dirlist.setText(item.getImagePathList().size() + "张");
                iv_choosen_item_dirlist.setVisibility(currentImageFolderNum == position ? View.VISIBLE : View.GONE);

                rl_item_dirlsit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        currentImageFolderNum = position;
                        //返回AlbumActivity
                        ImageFolderEvent imageFolderEvent = new ImageFolderEvent();
                        imageFolderEvent.setCurrentImageFolderNum(currentImageFolderNum);
                        EventBus.getDefault().post(imageFolderEvent);
                        dismiss();
                    }
                });
            }
        };
        rcv_imagefolders_dialogfragment.setAdapter(imageFoldersAdapter);
        imageFoldersAdapter.setData(imageFolderArrayList);

        return view;
    }




}
