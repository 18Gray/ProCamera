package com.eighteengray.procamera.imageprocess.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.eighteengray.imageprocesslibrary.ImageProcessBaseActivity;
import com.eighteengray.imageprocesslibrary.cvdemo.ImageProcessByOpenCv;
import com.eighteengray.procamera.R;


public class FilterActivity extends ImageProcessBaseActivity
{
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_filter;
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageView = (ImageView) findViewById(com.eighteengray.imageprocesslibrary.R.id.image_view);
        Bitmap bitmap = ImageProcessByOpenCv.bitmap2Mat();
        imageView.setImageBitmap(bitmap);
        /*int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w*h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

        int[] resultInt = ImageProcessJni.toGray(pixels, w, h);
        Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);
        imageView.setImageBitmap(resultImg);*/

    }



}
