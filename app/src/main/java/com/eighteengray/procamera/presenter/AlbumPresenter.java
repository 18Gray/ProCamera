package com.eighteengray.procamera.presenter;

import android.content.Context;

import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.business.AlbumBusiness;
import com.eighteengray.procamera.contract.IAlbumContract;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;


public class AlbumPresenter implements IAlbumContract.IPresenter
{
    private IAlbumContract.IView iView;

    @Inject
    public AlbumPresenter(IAlbumContract.IView iView)
    {
        this.iView = iView;
    }


    @Override
    public void getAlbumData(Context context)
    {
        AlbumBusiness.getImageFolder(context.getContentResolver()).subscribe(new Action1<List<ImageFolder>>()
        {
            @Override
            public void call(List<ImageFolder> imageFolders)
            {
                iView.setAdapterData(imageFolders, 0);
            }
        });
    }

}
