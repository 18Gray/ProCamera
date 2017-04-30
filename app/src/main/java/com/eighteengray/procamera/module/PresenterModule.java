package com.eighteengray.procamera.module;


import com.eighteengray.procamera.contract.IAlbumContract;
import com.eighteengray.procamera.presenter.AlbumPresenter;
import com.eighteengray.procamera.presenter.SettingPresenter;
import dagger.Module;
import dagger.Provides;


@Module
public class PresenterModule
{
    private IAlbumContract.IView iView;

    public PresenterModule(IAlbumContract.IView iView)
    {
        this.iView = iView;
    }

    @Provides
    public AlbumPresenter provideAlbumPresenter()
    {
        return new AlbumPresenter(iView);
    }

    @Provides
    public SettingPresenter provideSettingPresenter()
    {
        return new SettingPresenter();
    }

}
