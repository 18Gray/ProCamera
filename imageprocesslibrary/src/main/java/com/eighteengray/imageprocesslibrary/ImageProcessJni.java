package com.eighteengray.imageprocesslibrary;

public class ImageProcessJni {

    // 灰度图
    public static native int[] toGray(int[] buf, int width, int height);
    // 黑白图
    public static native int[] toWhiteBlack(int[] buf, int width, int height);
    // 浮雕效果
    public static native int[] toCameo(int[] buf, int width, int height);


    // 缩放
    public static native int[] zoom(int[] buf, int oriWidth, int oriHeight, int desWidth, int desHeight);

    //膨胀化
    public static native int[] dilation(int[] buf, int width, int height);

    // 对二值化Bitmap进行腐蚀运算
    public static native int[] erosion(int[] buf, int width, int height);

    // 细化
    public static native int[] thinning(int[] buf, int width, int height);

    // 高斯模糊
    public static native int[] blurPixels(int[] img, int w, int h, int r);


    public static native int[] gray(int[] buf, int w, int h);


}
