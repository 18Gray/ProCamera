package com.eighteengray.procamera.component;

import com.eighteengray.procamera.activity.AlbumActivity;
import com.eighteengray.procamera.module.PresenterModule;
import dagger.Component;


@Component(modules = PresenterModule.class)
public interface AlbumComponent
{
   public void inject(AlbumActivity albumActivity);
}
