package com.eighteengray.procamera.presenter;


import android.content.Context;

import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procamera.business.SettingsBusiness;
import com.eighteengray.procamera.contract.ISettingContract;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;


public class SettingPresenter implements ISettingContract.IPresenter
{
    private ISettingContract.IView settingView;

    @Inject
    public SettingPresenter(ISettingContract.IView iView)
    {
        this.settingView = iView;
    }

    @Override
    public void getSettingsData(Context context)
    {
        SettingsBusiness.getSettingsDataList(context).subscribe(new Action1<List<BaseDataBean<Settings>>>()
        {
            @Override
            public void call(List<BaseDataBean<Settings>> baseDataBeans)
            {
                settingView.setAdapterData(baseDataBeans);
            }
        });
    }

}
