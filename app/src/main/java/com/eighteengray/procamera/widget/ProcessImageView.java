package com.eighteengray.procamera.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.eighteengray.commonutillibrary.ImageUtils;


/**
 * 图像处理专用的ImageView
 */
public class ProcessImageView extends ImageView
{
    private Paint mPaint;
    private Context mContext;
    private String path;


    public ProcessImageView(Context context)
    {
        this(context, null);
        mContext = context;
        initPaint();
    }

    public ProcessImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    private void initPaint()
    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
        if(bitmapDrawable != null)
        {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap != null)
            {
                canvas.drawBitmap(bitmap, 0, 0, mPaint);
            }
            else
            {
                Bitmap newBitmap = ImageUtils.getBitmapFromPath(path);
                canvas.drawBitmap(newBitmap, 0, 0, mPaint);
            }

        }
    }


    public void setColorFilter(ColorFilter colorFilter)
    {
        mPaint.setColorFilter(colorFilter);
        invalidate();
    }


    public void setImagePath(String path)
    {
        this.path = path;
        invalidate();
    }

}
