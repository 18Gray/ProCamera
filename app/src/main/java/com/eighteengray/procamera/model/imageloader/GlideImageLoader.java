package com.eighteengray.procamera.model.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Razer on 2017/12/6.
 */

public class GlideImageLoader implements IImageLoader
{
    private static IImageLoader instance;
    private static Context context;

    private GlideImageLoader(Context c){
        this.context = c;
    }

    public static IImageLoader getInstance(){
        if(instance == null){
            synchronized (GlideImageLoader.class){
                if(instance == null){
                    instance = new GlideImageLoader(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void loadImage(String uri, ImageView imageView)
    {
        Glide.with(context).load(uri).into(imageView);
    }

    @Override
    public void loadImage(String uri, ImageView imageView, ImageLoadListener imageLoadListener)
    {
        Glide.with(context).load(uri).into(imageView);
    }
}
