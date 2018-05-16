package com.eighteengray.procamera.component;

import com.eighteengray.procamera.activity.SettingActivity;
import com.eighteengray.procamera.module.SettingsModule;
import dagger.Component;


@Component(modules = SettingsModule.class)
public interface SettingsComponent
{
   public void inject(SettingActivity settingActivity);
}
