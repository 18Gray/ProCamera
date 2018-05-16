package com.eighteengray.procamera.activity;


import java.io.File;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.eighteengray.commonutillibrary.FileUtils;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.commonutillibrary.SDCardUtils;
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.CropImageView;
import com.eighteengray.procameralibrary.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CutActivity extends BaseActivity
{
    @BindView(R.id.civ_cut)
    CropImageView mCropImage;
    private Drawable drawable;
    private Bitmap bitmap = null;
    String path;
    int width;


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case Constants.CUTPIC:
                    Intent mIntent = new Intent();
                    mIntent.putExtra(Constants.CROPIMAGEPATH, path);
                    setResult(RESULT_OK, mIntent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);


        width = ScreenUtils.getScreenWidth(CutActivity.this);
        path = getIntent().getStringExtra(Constants.CROPIMAGEPATH);
        bitmap = ImageUtils.getBitmapFromPathSimple(path);
        drawable = new BitmapDrawable(bitmap);
        mCropImage.setDrawable(drawable, width-100, width-100);

        btn_right.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutResId()
    {
        return R.layout.aty_cut;
    }


    @OnClick({R.id.btn_right})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_right:
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Bitmap cutBitmap = mCropImage.getCropImage();
                        File file = FileUtils.createFile(SDCardUtils.getAppFile(CutActivity.this).getAbsolutePath(), "cutBitmap.jpg");
                        path = file.getAbsolutePath();
                        ImageUtils.saveBitmap2Album(CutActivity.this, cutBitmap);
                        handler.sendEmptyMessage(Constants.CUTPIC);
                    }
                }).start();
                break;

            default:
                break;
        }
    }


}
