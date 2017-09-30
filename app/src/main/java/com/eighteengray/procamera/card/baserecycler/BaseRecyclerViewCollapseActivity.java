package com.eighteengray.procamera.card.baserecycler;


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
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.BaseActivity;
import com.eighteengray.procamera.widget.CropImageView;
import com.eighteengray.procameralibrary.common.Constants;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BaseRecyclerViewCollapseActivity extends BaseActivity
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

        width = ScreenUtils.getScreenWidth(BaseRecyclerViewCollapseActivity.this);
        path = getIntent().getStringExtra(Constants.CROPIMAGEPATH);
        bitmap = ImageUtils.getBitmapFromPath(path);
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
                        File file = FileUtils.createCutBitmapFile(BaseRecyclerViewCollapseActivity.this);
                        path = file.getAbsolutePath();
                        ImageUtils.saveBitmap(cutBitmap, file.getParent().toString(), file.getName().toString());
                        handler.sendEmptyMessage(Constants.CUTPIC);
                    }
                }).start();
                break;

            default:
                break;
        }
    }


}
