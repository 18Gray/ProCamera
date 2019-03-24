package com.eighteengray.procamera.imageprocess.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.eighteengray.imageprocesslibrary.ImageProcessBaseActivity;
import com.eighteengray.imageprocesslibrary.ImageProcessByOpenCv;
import com.eighteengray.procamera.R;


public class FilterActivity extends ImageProcessBaseActivity
{
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_filter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageView = (ImageView) findViewById(com.eighteengray.imageprocesslibrary.R.id.image_view);
        Bitmap bitmap = ImageProcessByOpenCv.bitmap2Mat();
        imageView.setImageBitmap(bitmap);
    }



}
