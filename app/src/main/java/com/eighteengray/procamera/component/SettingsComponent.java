package com.eighteengray.procamera.component;

import com.eighteengray.procamera.activity.SettingActivity;
import com.eighteengray.procamera.module.PresenterModule;
import dagger.Component;


@Component(modules = PresenterModule.class)
public interface SettingsComponent
{
   public void inject(SettingActivity settingActivity);
}
