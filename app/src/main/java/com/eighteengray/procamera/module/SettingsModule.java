package com.eighteengray.procamera.module;


import com.eighteengray.procamera.presenter.SettingPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class SettingsModule
{

    public SettingsModule()
    {
    }

    @Provides
    public SettingPresenter provideSettingPresenter()
    {
        return new SettingPresenter();
    }

}
