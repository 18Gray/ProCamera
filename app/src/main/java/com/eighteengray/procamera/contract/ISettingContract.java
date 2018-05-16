package com.eighteengray.procamera.contract;


import com.eighteengray.procamera.bean.Settings;

import java.util.List;


public interface ISettingContract
{
    public interface IView
    {
        public void setAdapterData(List<Settings> settingsList);
    }

    public interface IPresenter
    {
        public List<Settings> getSettingsList();
    }
}
