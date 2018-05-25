package com.eighteengray.procamera.module;


import com.eighteengray.procamera.contract.IAlbumContract;
import com.eighteengray.procamera.contract.ISettingContract;
import com.eighteengray.procamera.presenter.AlbumPresenter;
import com.eighteengray.procamera.presenter.SettingPresenter;
import dagger.Module;
import dagger.Provides;


@Module
public class PresenterModule
{
    private IAlbumContract.IView albumView;
    private ISettingContract.IView settingView;

    public PresenterModule(IAlbumContract.IView iView)
    {
        this.albumView = iView;
    }

    public PresenterModule(ISettingContract.IView iView){
        this.settingView = iView;
    }

    @Provides
    public AlbumPresenter provideAlbumPresenter()
    {
        return new AlbumPresenter(albumView);
    }

    @Provides
    public SettingPresenter provideSettingPresenter()
    {
        return new SettingPresenter(settingView);
    }

}
