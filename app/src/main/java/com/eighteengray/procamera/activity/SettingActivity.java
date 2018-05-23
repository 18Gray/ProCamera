package com.eighteengray.procamera.activity;

import android.os.Bundle;

import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procamera.contract.ISettingContract;
import com.eighteengray.procamera.presenter.SettingPresenter;
import org.greenrobot.eventbus.EventBus;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 功能：
 * 1.图像、视频录制质量、格式调节
 * 2.九宫格、平衡器、防手抖、静音、
 * 3.实时直方图
 * 4.签名，包括日期、位置、版权、字号、颜色
 * 设置信息写入SP
 */
public class SettingActivity extends BaseActivity implements ISettingContract.IView
{
    @BindView(R.id.rl_settings)
    RecyclerLayout rl_settings;

    SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initCommonTitle();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_setting;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //获取数据
        rl_settings.showLoadingView();
        /*rl_settings.getSe(SettingActivity.this);

        rl_pics_album.setRecyclerViewScroll(new RecyclerLayout.RecyclerViewScroll()
        {
            @Override
            public void refreshData()
            {
                albumPresenter.getAlbumData(AlbumActivity.this);
            }

            @Override
            public void getMoreData()
            {
                albumPresenter.getAlbumData(AlbumActivity.this);
            }

            @Override
            public void upScroll()
            {

            }

            @Override
            public void downScroll()
            {

            }
        });*/
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


    @Override
    public void setAdapterData(List<Settings> settingsList)
    {

    }
}
