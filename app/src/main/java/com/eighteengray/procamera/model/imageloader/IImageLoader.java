package com.eighteengray.procamera.model.imageloader;

import android.app.Fragment;
import android.content.Context;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Razer on 2017/12/6.
 */

public interface IImageLoader
{
    public static int LOADER_PLAN_GLIDE = 1;
    public static int LOADER_PLAN_FRESCO = 2;
    public static int LOADER_PLAN_UNIVERSE = 3;

    public static int IMAGE_FORMAT_BITMAP = 1;
    public static int IMAGE_FORMAT_GIF = 2;

    public IImageLoader with(Context context);
    public IImageLoader with(Fragment fragment);
    public IImageLoader with(android.support.v4.app.Fragment fragment);

    public IImageLoader load(File file);
    public IImageLoader load(int resource);
    public IImageLoader load(byte[] bytes);
    public IImageLoader load(String url);

    public IImageLoader into(ImageView imageView);

    public IImageLoader setFormat(int format);

    public IImageLoader placeHolder(int resource);
    public IImageLoader errorHolder(int resource);

    public IImageLoader setDiskCacheMode(DiskCacheMode diskCacheMode);

    public IImageLoader setSize(int width, int height);

    public IImageLoader setImageLoadListener(ImageLoadListener imageLoadListener);

    public void execute();

}
