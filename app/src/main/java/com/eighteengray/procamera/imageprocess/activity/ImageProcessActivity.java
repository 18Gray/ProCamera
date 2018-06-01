package com.eighteengray.procamera.imageprocess.activity;


import android.app.Activity;
import android.os.Bundle;
import com.eighteengray.procamera.R;
import org.greenrobot.eventbus.EventBus;
import butterknife.ButterKnife;


/**
 * LruCache， 弱引用
 * 所有Activity之间的跳转，都通过传递一个图片地址和修改记录列表，跳到下一个Activity后重新
 */
public class ImageProcessActivity extends Activity
{
    private String imagePath;


    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_image_process);
        ButterKnife.bind(this);

    }



}
