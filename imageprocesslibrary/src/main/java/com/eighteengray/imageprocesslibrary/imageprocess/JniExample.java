package com.eighteengray.imageprocesslibrary.imageprocess;


public class JniExample
{

    static
    {
        System.loadLibrary("imageprocess-lib");
    }

    public static native String stringFromJNI();
}
