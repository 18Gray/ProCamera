package com.eighteengray.imageprocesslibrary.bitmapfilter;


import android.graphics.ColorMatrix;


public class RedBitmapFilter implements IBitmapFilter {
    @Override
    public ColorMatrix createColorMatrix(int count) {
        float[] floats = new float[]{
                1, 0, 0, 0, 0, //red
                0, 0, 0, 0, 0, //green
                0, 0, 0, 0, 0, //blue
                0, 0, 0, 1, 0
        };
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(floats);
        return colorMatrix;
    }
}
