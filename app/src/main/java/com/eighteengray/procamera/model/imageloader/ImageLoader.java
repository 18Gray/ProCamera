package com.eighteengray.procamera.model.imageloader;

import android.widget.ImageView;

/**
 * Created by lutao on 2017/9/28.
 * 图片加载的封装类，实现方法可以采用Fresco或Glide
 */

public class ImageLoader implements IImageLoader
{
    private static final ImageLoader instance = new ImageLoader();
    private int planNum = 2; //图像加载实现方案

    private ImageLoader(){

    }

    public static ImageLoader getInstance(){
        return instance;
    }

    @Override
    public void loadImage(String uri, ImageView imageView){
        switch (planNum){
            case 0:
                UniverImageLoader.getInstance().loadImage(uri, imageView);
                break;
            case 1:
                FrescoImageLoader.getInstance().loadImage(uri, imageView);
                break;
            case 2:
                GlideImageLoader.getInstance().loadImage(uri, imageView);
                break;
        }
    }

    @Override
    public void loadImage(String uri, ImageView imageView, ImageLoadListener imageLoadListener){
        switch (planNum){
            case 0:
                UniverImageLoader.getInstance().loadImage(uri, imageView, imageLoadListener);
                break;
            case 1:
                FrescoImageLoader.getInstance().loadImage(uri, imageView, imageLoadListener);
                break;
            case 2:
                GlideImageLoader.getInstance().loadImage(uri, imageView, imageLoadListener);
                break;
        }
    }

}
