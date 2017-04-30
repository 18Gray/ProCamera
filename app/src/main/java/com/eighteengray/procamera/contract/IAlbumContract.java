package com.eighteengray.procamera.contract;


import android.content.Context;

import com.eighteengray.procamera.bean.ImageFolder;

import java.util.List;

public interface IAlbumContract
{
    public interface IView
    {
        public void setAdapterData(List<ImageFolder> imageFolders);
    }

    public interface IPresenter
    {
        public void getAlbumData(Context context);
    }

}
