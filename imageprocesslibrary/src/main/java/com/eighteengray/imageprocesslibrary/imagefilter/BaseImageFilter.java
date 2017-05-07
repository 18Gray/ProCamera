package com.eighteengray.imageprocesslibrary.imagefilter;



public abstract class BaseImageFilter
{
    static
    {
        System.loadLibrary("imageprocess-lib");
    }

    public abstract Image process(Image imageIn);

}
