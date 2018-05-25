package com.eighteengray.procamera.contract;


import android.content.Context;

import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.procamera.bean.Settings;

import java.util.List;


public interface ISettingContract
{
    public interface IView
    {
        public void setAdapterData(List<BaseDataBean<Settings>> settingsDataList);
    }

    public interface IPresenter
    {
        public void getSettingsData(Context context);
    }
}
