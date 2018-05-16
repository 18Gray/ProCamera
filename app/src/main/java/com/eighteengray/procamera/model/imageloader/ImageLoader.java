package com.eighteengray.procamera.model.imageloader;

import android.app.Fragment;
import android.content.Context;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by lutao on 2017/9/28.
 * 图片加载的封装类，实现方法可以采用Fresco或Glide
 */

public class ImageLoader implements IImageLoader
{
    private static ImageLoader instance;

    protected int loaderPlan = LOADER_PLAN_GLIDE; //图像加载实现方案

    protected Context context = null;
    protected Fragment fragment = null;
    protected android.support.v4.app.Fragment v4Fragment = null;

    protected File loadFile = null;
    protected int loadResource = 0;
    protected byte[] loadBytes = null;
    protected String loadUrl = null;

    protected ImageView imageView = null;

    protected int format = IMAGE_FORMAT_BITMAP;

    protected int placeHolder = 0;
    protected int errorHolder = 0;

    protected DiskCacheMode diskCacheMode = null;

    protected int width, height;
    protected ImageLoadListener imageLoadListener = null;


    protected ImageLoader(){
    }

    public static ImageLoader getInstance(){
        if(instance == null){
            synchronized (ImageLoader.class){
                if(instance == null){
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    @Override
    public IImageLoader setLoaderPlan(int plan)
    {
        this.loaderPlan = plan;
        return this;
    }

    @Override
    public IImageLoader with(Context context)
    {
        if(this.fragment == null && this.v4Fragment == null){
            this.context = context;
        }
        return this;
    }

    @Override
    public IImageLoader with(Fragment fragment)
    {
        if(this.context == null && this.v4Fragment == null){
            this.fragment = fragment;
        }
        return this;
    }

    @Override
    public IImageLoader with(android.support.v4.app.Fragment fragment)
    {
        if(this.context == null && this.fragment == null){
            this.v4Fragment = fragment;
        }
        return this;
    }

    @Override
    public IImageLoader load(File file)
    {
        if(this.loadResource == 0 && this.loadBytes == null && this.loadUrl == null){
            this.loadFile = file;
        }
        return this;
    }

    @Override
    public IImageLoader load(int resource)
    {
        if(this.loadFile == null && this.loadBytes == null && this.loadUrl == null){
            this.loadResource = resource;
        }
        return this;
    }

    @Override
    public IImageLoader load(byte[] bytes)
    {
        if(this.loadResource == 0 && this.loadFile == null && this.loadUrl == null){
            this.loadBytes = bytes;
        }
        return this;
    }

    @Override
    public IImageLoader load(String url)
    {
        if(this.loadResource == 0 && this.loadBytes == null && this.loadFile == null){
            this.loadUrl = url;
        }
        return this;
    }

    @Override
    public IImageLoader into(ImageView imageView)
    {
        this.imageView = imageView;
        return this;
    }

    @Override
    public IImageLoader setFormat(int format)
    {
        this.format = format;
        return this;
    }

    @Override
    public IImageLoader placeHolder(int resource)
    {
        this.placeHolder = resource;
        return this;
    }

    @Override
    public IImageLoader errorHolder(int resource)
    {
        this.errorHolder = resource;
        return this;
    }

    @Override
    public IImageLoader setDiskCacheMode(DiskCacheMode diskCacheMode)
    {
        this.diskCacheMode = diskCacheMode;
        return this;
    }

    @Override
    public IImageLoader setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public IImageLoader setImageLoadListener(ImageLoadListener imageLoadListener)
    {
        this.imageLoadListener = imageLoadListener;
        return this;
    }

    @Override
    public void execute()
    {
        switch (loaderPlan){
            case LOADER_PLAN_GLIDE:
                GlideImageLoader.getInstance().execute();
                break;
            case LOADER_PLAN_FRESCO:
                FrescoImageLoader.getInstance().execute();
                break;
            case LOADER_PLAN_UNIVERSE:
                UniverImageLoader.getInstance().execute();
                break;
        }
    }

}
