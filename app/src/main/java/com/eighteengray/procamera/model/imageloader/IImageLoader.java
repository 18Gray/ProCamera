package com.eighteengray.procamera.model.imageloader;

import android.widget.ImageView;

/**
 * Created by Razer on 2017/12/6.
 */

public interface IImageLoader
{
    public void loadImage(String uri, ImageView imageView);
    public void loadImage(String uri, ImageView imageView, ImageLoadListener imageLoadListener);
}
