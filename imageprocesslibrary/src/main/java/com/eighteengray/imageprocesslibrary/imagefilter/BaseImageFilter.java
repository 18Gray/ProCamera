package com.eighteengray.imageprocesslibrary.imagefilter;

/**
 * Created by lutao on 2017/3/17.
 */

public abstract class BaseImageFilter
{
    static
    {
        System.loadLibrary("imageprocess-lib");
    }

    public abstract Image process(Image imageIn);

}
