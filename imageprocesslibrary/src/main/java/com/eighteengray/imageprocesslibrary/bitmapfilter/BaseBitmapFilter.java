package com.eighteengray.imageprocesslibrary.bitmapfilter;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;



public abstract class BaseBitmapFilter
{
    protected abstract ColorMatrix createColorMatrix(int count);


    public Bitmap process(Bitmap bitmap, int count)
    {
        Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(copyBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix colorMatrix = createColorMatrix(count);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(copyBitmap, 0, 0, paint);
        return copyBitmap;
    }



}
