package com.eighteengray.imageprocesslibrary.bitmapfilter;


import android.graphics.ColorMatrix;

public class GrayBitmapFilter extends BaseBitmapFilter
{
    @Override
    public ColorMatrix createColorMatrix(int count)
    {
        float[] floats = new float[]{
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0, 0, 0, 1, 0
        };
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(floats);
        return colorMatrix;
    }
}
