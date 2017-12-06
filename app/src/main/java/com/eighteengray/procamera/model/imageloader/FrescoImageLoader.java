package com.eighteengray.procamera.model.imageloader;

import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Razer on 2017/12/6.
 */

public class FrescoImageLoader implements IImageLoader
{
    private static final IImageLoader instance = new FrescoImageLoader();

    private FrescoImageLoader(){

    }

    public static IImageLoader getInstance(){
        return instance;
    }

    @Override
    public void loadImage(String uri, ImageView imageView)
    {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
        simpleDraweeView.setImageURI(uri);
    }

    @Override
    public void loadImage(String uri, ImageView imageView, ImageLoadListener imageLoadListener)
    {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
        simpleDraweeView.setImageURI(uri);
    }
}
