package com.eighteengray.procamera.activity;

import android.os.Bundle;

import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procamera.business.GenerateDataUtils;
import com.eighteengray.procamera.component.DaggerSettingsComponent;
import com.eighteengray.procamera.contract.ISettingContract;
import com.eighteengray.procamera.module.PresenterModule;
import com.eighteengray.procamera.presenter.SettingPresenter;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

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

    @Inject
    SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        initCommonTitle();
        EventBus.getDefault().register(this);
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
        // 注册Dagger2，从而可以注入Presenter
        DaggerSettingsComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);

        //获取数据
        rl_settings.showLoadingView();
        settingPresenter.getSettingsData(this);

        rl_settings.setRecyclerViewScroll(new RecyclerLayout.RecyclerViewScroll()
        {
            @Override
            public void refreshData()
            {
            }

            @Override
            public void getMoreData()
            {
            }

            @Override
            public void upScroll()
            {

            }

            @Override
            public void downScroll()
            {

            }
        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void setAdapterData(List<BaseDataBean<Settings>> settingsList)
    {
        rl_settings.showRecyclerView(settingsList, Constants.viewModelPackage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageFolderSelected(ImageFolderEvent imageFolderEvent)
    {

    }


}
