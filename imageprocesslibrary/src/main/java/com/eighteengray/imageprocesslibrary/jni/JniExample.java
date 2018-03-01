package com.eighteengray.imageprocesslibrary.jni;


public class JniExample
{

    static
    {
        System.loadLibrary("imageprocess-lib");
    }

    public static native String stringFromJNI();
}
