package com.eighteengray.imageprocesslibrary.java.bitmapfilter;


import android.graphics.ColorMatrix;

public class ReverseBitmapFilter implements IBitmapFilter
{
    @Override
    public ColorMatrix createColorMatrix(int count)
    {
        float[] floats = new float[]{
                -1, 0, 0, 1, 1,
                0, -1, 0, 1, 1,
                0, 0, -1, 1, 1,
                0, 0, 0, 1, 0
        };
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(floats);
        return colorMatrix;
    }
}
