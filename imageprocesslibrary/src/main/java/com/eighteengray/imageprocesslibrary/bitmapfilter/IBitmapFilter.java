package com.eighteengray.imageprocesslibrary.bitmapfilter;

import android.graphics.ColorMatrix;

/**
 * Created by lutao on 2017/5/8.
 */

public interface IBitmapFilter {
    public ColorMatrix createColorMatrix(int count);
}
