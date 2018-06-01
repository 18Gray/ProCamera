package com.eighteengray.procamera.imageprocess.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.BaseActivity;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * LruCache， 弱引用
 * 所有Activity之间的跳转，都通过传递一个图片地址和修改记录列表，跳到下一个Activity后重新
 */
public class ImageProcessActivity extends BaseActivity
{
    private String imagePath;

    @BindView(R.id.tv_filter_image_process)
    TextView tv_filter_image_process;
    @BindView(R.id.tv_tools_image_process)
    TextView tv_tools_image_process;
    @BindView(R.id.tv_output_image_process)
    TextView tv_output_image_process;



    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_image_process);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        imagePath = bundle.getString("imagePath");

        initCommonTitle();
        btn_search.setVisibility(View.VISIBLE);
        btn_search.setBackgroundResource(R.mipmap.redo_menu_black_24dp);

        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.tv_select_album})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_search:

                break;
            case R.id.tv_filter_image_process:

                break;
            case R.id.tv_tools_image_process:

                break;
            case R.id.tv_output_image_process:

                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageFolderSelected(ImageFolderEvent imageFolderEvent)
    {
        int currentImageFolderNum = imageFolderEvent.getCurrentImageFolderNum();
    }

}
