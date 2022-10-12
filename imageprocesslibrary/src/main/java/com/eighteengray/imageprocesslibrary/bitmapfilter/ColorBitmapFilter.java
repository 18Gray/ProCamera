package com.eighteengray.imageprocesslibrary.bitmapfilter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


public class ColorBitmapFilter {
    private Bitmap mBitmap;

    public ColorBitmapFilter(Bitmap bmp) {
        mBitmap = bmp;
    }

    private ColorMatrix mSaturationMatrix;
    private ColorMatrix mHueMatrix;
    private ColorMatrix mLumMatrix;
    private ColorMatrix mAllMatrix;

    private float mSaturationValue = 0F;
    private float mHueValue = 0F;
    private float mLumValue = 0F;

    private static final int MIDDLE_VALUE = 127;
    private static final int MAX_VALUE = 255;


    public void setSaturation(int value) {
        mSaturationValue = value * 1.0F / MIDDLE_VALUE;
    }

    public void SetHue(int value) {
        mHueValue = (value - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
    }

    public void SetLum(int value) {
        mLumValue = value * 1.0F / MIDDLE_VALUE;
    }


    public Bitmap process(Bitmap bmp, int flag) {
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (mAllMatrix == null) {
            mAllMatrix = new ColorMatrix();
        }
        if (mSaturationMatrix == null) {
            mSaturationMatrix = new ColorMatrix();
        }
        if (mHueMatrix == null) {
            mHueMatrix = new ColorMatrix();
        }
        if (mLumMatrix == null) {
            mLumMatrix = new ColorMatrix();
        }
        if (flag == 0) {
            mSaturationMatrix.reset();
            mSaturationMatrix.setSaturation(mSaturationValue);
        } else if (flag == 1) {
            mHueMatrix.reset();
            mHueMatrix.setRotate(0, mHueValue);
            mHueMatrix.setRotate(1, mHueValue);
            mHueMatrix.setRotate(2, mHueValue);
        } else if (flag == 2) {
            mLumMatrix.reset();
            mLumMatrix.setScale(mLumValue, mLumValue, mLumValue, 1);
        }

        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mLumMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bitmap;
    }


}
