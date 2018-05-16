package com.eighteengray.procamera.model.imageloader;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * Created by Razer on 2017/12/6.
 */
public class FrescoImageLoader extends ImageLoader
{
    private static ImageLoader instance;

    public static ImageLoader getInstance(){
        if(instance == null){
            synchronized (GlideImageLoader.class){
                if(instance == null){
                    instance = new GlideImageLoader();
                }
            }
        }
        return instance;
    }

    @Override
    public void execute()
    {
        RequestManager requestManager = null;
        if(context != null){
            requestManager = Glide.with(context);
        }else if(fragment != null){
            requestManager = Glide.with(fragment);
        }else if(v4Fragment != null){
            requestManager = Glide.with(v4Fragment);
        }

        DrawableTypeRequest drawableTypeRequest = null;
        if(loadFile != null){
            drawableTypeRequest = requestManager.load(loadFile);
        }else if(loadResource != 0){
            drawableTypeRequest = requestManager.load(loadResource);
        }else if(loadBytes != null){
            drawableTypeRequest = requestManager.load(loadBytes);
        }else if(loadUrl != null){
            drawableTypeRequest = requestManager.load(loadUrl);
        }

        DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.ALL;
        switch (diskCacheMode){
            case ALL:
                diskCacheStrategy = DiskCacheStrategy.ALL;
                break;
            case SOURCE:
                diskCacheStrategy = DiskCacheStrategy.SOURCE;
                break;
            case RESULT:
                diskCacheStrategy = DiskCacheStrategy.RESULT;
                break;
            case NONE:
                diskCacheStrategy = DiskCacheStrategy.NONE;
                break;
        }

        GenericRequestBuilder genericRequestBuilder = null;
        switch (format){
            case IMAGE_FORMAT_BITMAP:
                genericRequestBuilder = drawableTypeRequest.asBitmap();
                break;
            case IMAGE_FORMAT_GIF:
                genericRequestBuilder = drawableTypeRequest.asGif();
                break;
        }

        if(placeHolder != 0){
            genericRequestBuilder.placeholder(placeHolder);
        }
        if(errorHolder != 0){
            genericRequestBuilder.error(errorHolder);
        }

        if(width != 0 && height != 0){
            genericRequestBuilder.override(width, height);
        }

        genericRequestBuilder.diskCacheStrategy(diskCacheStrategy).into(imageView);
    }
}
