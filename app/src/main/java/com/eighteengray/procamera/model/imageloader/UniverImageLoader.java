package com.eighteengray.procamera.model.imageloader;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Razer on 2017/12/6.
 */

public class UniverImageLoader implements IImageLoader
{
    private static final IImageLoader instance = new UniverImageLoader();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(null)
            .showImageForEmptyUri(null)
            .showImageOnFail(null)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    private UniverImageLoader(){

    }

    public static IImageLoader getInstance(){
        return instance;
    }

    @Override
    public void loadImage(String uri, ImageView imageView)
    {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    @Override
    public void loadImage(String uri, ImageView imageView, final ImageLoadListener imageLoadListener)
    {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(uri, imageView, options, new ImageLoadingListener()
        {
            @Override
            public void onLoadingStarted(String s, View view)
            {
                imageLoadListener.onLoadStarted(s, view);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason)
            {
                imageLoadListener.onLoadFailed(s, view, failReason.toString());
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap)
            {
                imageLoadListener.onLoadComplete(s, view, bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view)
            {
                imageLoadListener.onLoadCancelled(s, view);
            }
        });
    }
}
