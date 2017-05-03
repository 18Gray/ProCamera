package com.eighteengray.procamera.activity;


import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.eighteengray.commonutillibrary.DataConvertUtil;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.widget.CropImageView;
import com.eighteengray.procameralibrary.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.mode;


public class CutActivity extends BaseActivity
{
    @BindView(R.id.iv_finish_cut)
    ImageView iv_finish_cut;
    @BindView(R.id.button_next_gallery)
    Button button_next_gallery;

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
        setContentView(R.layout.aty_cut);
        ButterKnife.bind(this);

        width = ScreenUtils.getScreenWidth(CutActivity.this);
        path = getIntent().getStringExtra(Constants.CROPIMAGEPATH);
        bitmap = ImageUtils.getBitmapFromPath(path);
        drawable = DataConvertUtil.bitmap2Drawable(bitmap);
        mCropImage.setDrawable(drawable, width, width / 2);
    }


    @OnClick({R.id.iv_finish_cut, R.id.button_next_gallery})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_finish_cut:
                finish();
                break;

            case R.id.button_next_gallery:
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        bitmap = mCropImage.getCropImage();
                        File file = new File(path);
                        ImageUtils.saveBitmap(bitmap, file.getParent().toString(), file.getName().toString());

                        handler.sendEmptyMessage(Constants.CUTPIC);
                    }
                }).start();
                break;

            default:
                break;
        }
    }


}
