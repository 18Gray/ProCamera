package com.eighteengray.procamera.model.imageloader;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Razer on 2017/12/6.
 */

public interface ImageLoadListener
{
    public void onLoadStarted(String uri, View view);

    public void onLoadFailed(String uri, View view, String failReason);

    public void onLoadComplete(String uri, View view, Bitmap var3);

    public void onLoadCancelled(String uri, View view);
}
