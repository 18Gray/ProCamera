package com.eighteengray.procamera.imageprocess.activity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eighteengray.basecomponent.baseactivity.BaseActivity;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.business.JumpActivityUtils;
import com.eighteengray.procamera.imageprocess.widget.OutputMenuDialog;
import com.eighteengray.procamera.imageprocess.widget.RedoMenuDialog;
import com.eighteengray.procamera.model.imageloader.ImageLoader;
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
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

    @BindView(R.id.imageview_process)
    ImageView imageview_process;

    @BindView(R.id.ll_bottommenu_image_process)
    LinearLayout ll_bottommenu_image_process;
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
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        imagePath = bundle.getString(Constants.IMAGE_PATH);

        initCommonTitle();
        btn_search.setVisibility(View.VISIBLE);
        btn_search.setBackgroundResource(R.mipmap.redo_menu_white_24dp);
        btn_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RedoMenuDialog redoMenuDialog = new RedoMenuDialog();
                redoMenuDialog.show(getSupportFragmentManager(), "redo");
            }
        });

        ImageLoader.getInstance().with(ImageProcessActivity.this).load(new File(imagePath)).into(imageview_process).execute();

        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_image_process;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.tv_filter_image_process, R.id.tv_tools_image_process, R.id.tv_output_image_process})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_filter_image_process:
//                PopupWindowFactory.createFilterPopupWindow(ImageProcessActivity.this).showAtLocation(ll_bottommenu_image_process, Gravity.BOTTOM, 0, 288);
                JumpActivityUtils.jump2FilterActivity(this);
                break;
            case R.id.tv_tools_image_process:
                PopupWindowFactory.createProcessPopupWindow(ImageProcessActivity.this).showAtLocation(ll_bottommenu_image_process, Gravity.BOTTOM, 0, 288);
                break;
            case R.id.tv_output_image_process:
                OutputMenuDialog outputMenuDialog = new OutputMenuDialog();
                outputMenuDialog.show(getSupportFragmentManager(), "output");
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageFolderSelected(ImageFolderEvent imageFolderEvent)
    {
        int currentImageFolderNum = imageFolderEvent.getCurrentImageFolderNum();
    }

}
