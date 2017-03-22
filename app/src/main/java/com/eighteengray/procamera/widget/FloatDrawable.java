package com.eighteengray.procamera.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * ͷ��ͼƬѡ���ĸ���
 * 
 * @author Administrator
 * 
 */
public class FloatDrawable extends Drawable
{

	private Context mContext;
	// private Drawable mCropPointDrawable;

	private int left_Rect;
	private int top_Rect;
	private int bottom_Rect;
	private int right_Rect;

	private Paint mLinePaint = new Paint();
	{
		mLinePaint.setARGB(200, 50, 50, 50);
		mLinePaint.setStrokeWidth(1F);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setAntiAlias(true);
		mLinePaint.setColor(Color.WHITE);
	}

	public FloatDrawable(Context context)
	{
		super();
		this.mContext = context;
	}

	@Override
	public void draw(Canvas canvas)
	{

		int left = getBounds().left;
		int top = getBounds().top;
		int right = getBounds().right;
		int bottom = getBounds().bottom;

		left_Rect = left;// + mCropPointDrawable.getIntrinsicWidth() / 2;
		top_Rect = top;// + mCropPointDrawable.getIntrinsicHeight() / 2;
		right_Rect = right;// - mCropPointDrawable.getIntrinsicWidth() / 2;
		bottom_Rect = bottom;// - mCropPointDrawable.getIntrinsicHeight() / 2;

		Log.e("FloatDrawable", "left_Rect:   ==>" + left_Rect);
		Log.e("FloatDrawable", "top_Rect:    ==>" + top_Rect);
		Log.e("FloatDrawable", "right_Rect:  ==>" + right_Rect);
		Log.e("FloatDrawable", "bottom_Rect: ==>" + bottom_Rect);

		Rect mRect = new Rect(left_Rect, top_Rect, right_Rect, bottom_Rect);

	}

	@Override
	public void setBounds(Rect bounds)
	{
		super.setBounds(new Rect(bounds.left, bounds.top, bounds.right,
				bounds.bottom));
	}

	@Override
	public void setAlpha(int alpha)
	{
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
	}

	@Override
	public int getOpacity()
	{
		return PixelFormat.OPAQUE;
	}

	public int getLeft_Rect()
	{
		return left_Rect;
	}

	public void setLeft_Rect(int left_Rect)
	{
		this.left_Rect = left_Rect;
	}

	public int getTop_Rect()
	{
		return top_Rect;
	}

	public void setTop_Rect(int top_Rect)
	{
		this.top_Rect = top_Rect;
	}

	public int getBottom_Rect()
	{
		return bottom_Rect;
	}

	public void setBottom_Rect(int bottom_Rect)
	{
		this.bottom_Rect = bottom_Rect;
	}

	public int getRight_Rect()
	{
		return right_Rect;
	}

	public void setRight_Rect(int right_Rect)
	{
		this.right_Rect = right_Rect;
	}

}
