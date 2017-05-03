package com.eighteengray.procamera.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;


/**
 * 底图缩放，浮层不变
 * @author yanglonghui
 */
public class CropImageView extends View
{
	// 单点触摸的时候
	private float oldX = 0;
	private float oldY = 0;

	// 多点触摸的时候
	private float oldx_0 = 0;
	private float oldy_0 = 0;

	private float oldx_1 = 0;
	private float oldy_1 = 0;

	// 状态
	private final int STATUS_Touch_SINGLE = 1;// 单点
	private final int STATUS_TOUCH_MULTI_START = 2;// 多点开始
	private final int STATUS_TOUCH_MULTI_TOUCHING = 3;// 多点拖拽中

	private int mStatus = STATUS_Touch_SINGLE;

	// 默认的裁剪图片宽度与高度
	private final int defaultCropWidth = 250;
	private final int defaultCropHeight = 250;
	private int cropWidth = defaultCropWidth;
	private int cropHeight = defaultCropHeight;

	protected float oriRationWH = 0;// 原始宽高比率
	protected float maxZoomOut = 3.0f;// 最大扩大到多少倍
	protected float minZoomIn = 0.6666666f;// 最小缩小到多少倍

	protected Drawable mDrawable;// 原图
	protected FloatDrawable mFloatDrawable;// 浮层
	protected Rect mDrawableSrc = new Rect();
	protected Rect mDrawableDst = new Rect();
	protected Rect mDrawableFloat = new Rect();// 浮层选择框，就是头像选择框
	protected boolean isFrist = true;

	protected Context mContext;

	private int floatWidth;
	private int floatHeight;
	private int floatLeft;
	private int floatTop;

	public CropImageView(Context context)
	{
		super(context);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	
	@SuppressLint("NewApi")
	private void init(Context context)
	{
		this.mContext = context;
		this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		mFloatDrawable = new FloatDrawable(context);// ͷ��ѡ���
	}

	
	
	public void setDrawable(Drawable mDrawable, int cropWidth, int cropHeight)
	{
		this.mDrawable = mDrawable;
		this.cropWidth = cropWidth;
		this.cropHeight = cropHeight;
		this.isFrist = true;
		invalidate();
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int t = mDrawableDst.top;
		int l = mDrawableDst.left;
		int r = mDrawableDst.right;
		int b = mDrawableDst.bottom;

		int left = mFloatDrawable.getLeft_Rect();
		int top = mFloatDrawable.getTop_Rect();
		int right = mFloatDrawable.getRight_Rect();
		int bottom = mFloatDrawable.getBottom_Rect();

		if (event.getPointerCount() > 1)
		{
			if (mStatus == STATUS_Touch_SINGLE)
			{
				mStatus = STATUS_TOUCH_MULTI_START;

				oldx_0 = event.getX(0);
				oldy_0 = event.getY(0);

				oldx_1 = event.getX(1);
				oldy_1 = event.getY(1);
			} else if (mStatus == STATUS_TOUCH_MULTI_START)
			{
				mStatus = STATUS_TOUCH_MULTI_TOUCHING;
			}
		} else
		{
			if (mStatus == STATUS_TOUCH_MULTI_START
					|| mStatus == STATUS_TOUCH_MULTI_TOUCHING)
			{
				oldx_0 = 0;
				oldy_0 = 0;

				oldx_1 = 0;
				oldy_1 = 0;

				oldX = event.getX();
				oldY = event.getY();
			}

			mStatus = STATUS_Touch_SINGLE;
		}

		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			oldX = event.getX();
			oldY = event.getY();

			Log.e("event.getAction()", "event.getAction():X" + oldX);
			Log.e("event.getAction()", "event.getAction():Y" + oldY);

			break;

		case MotionEvent.ACTION_UP:
			checkBounds();
			break;

		case MotionEvent.ACTION_POINTER_1_DOWN:
			break;

		case MotionEvent.ACTION_POINTER_UP:
			break;

		case MotionEvent.ACTION_MOVE:
			if (mStatus == STATUS_TOUCH_MULTI_TOUCHING)
			{
				float newx_0 = event.getX(0);
				float newy_0 = event.getY(0);

				float newx_1 = event.getX(1);
				float newy_1 = event.getY(1);

				float oldWidth = Math.abs(oldx_1 - oldx_0);
				float oldHeight = Math.abs(oldy_1 - oldy_0);

				float newWidth = Math.abs(newx_1 - newx_0);
				float newHeight = Math.abs(newy_1 - newy_0);

				boolean isDependHeight = Math.abs(newHeight - oldHeight) > Math
						.abs(newWidth - oldWidth);
				float ration = isDependHeight ? ((float) newHeight / (float) oldHeight)
						: ((float) newWidth / (float) oldWidth);
				int centerX = mDrawableDst.centerX();
				int centerY = mDrawableDst.centerY();
				int _newWidth = (int) (mDrawableDst.width() * ration);
				int _newHeight = (int) ((float) _newWidth / oriRationWH);

				float tmpZoomRation = (float) _newWidth
						/ (float) mDrawableSrc.width();
				if (tmpZoomRation >= maxZoomOut)
				{
					_newWidth = (int) (maxZoomOut * mDrawableSrc.width());
					_newHeight = (int) ((float) _newWidth / oriRationWH);
				} else if (tmpZoomRation <= minZoomIn)
				{
					_newWidth = (int) (minZoomIn * mDrawableSrc.width());
					_newHeight = (int) ((float) _newWidth / oriRationWH);
				}

				mDrawableDst.set(centerX - _newWidth / 2, centerY - _newHeight
						/ 2, centerX + _newWidth / 2, centerY + _newHeight / 2);

				invalidate();

				Log.v("width():" + (mDrawableSrc.width()) + "height():"
						+ (mDrawableSrc.height()), "new width():"
						+ (mDrawableDst.width()) + "new height():"
						+ (mDrawableDst.height()));
				Log.v("" + (float) mDrawableSrc.height()
						/ (float) mDrawableSrc.width(),
						"mDrawableDst:" + (float) mDrawableDst.height()
								/ (float) mDrawableDst.width());

				oldx_0 = newx_0;
				oldy_0 = newy_0;
				oldx_1 = newx_1;
				oldy_1 = newy_1;

				int dx_d = 0;
				int dy_d = 0;

				if (left < l)
				{
					dx_d = left - l;
				}

				if (top < t)
				{
					dy_d = top - t;
				}
				if (right > r)
				{
					dx_d = right - r;
				}
				if (bottom > b)
				{
					dy_d = bottom - b;
				}
				Log.e("RECT", "dx_d:==>" + dx_d);
				Log.e("RECT", "dy_d:==>" + dy_d);

				mDrawableDst.offset((int) dx_d, (int) dy_d);
				invalidate();

			} else if (mStatus == STATUS_Touch_SINGLE)
			{

				int dx = (int) (event.getX() - oldX);
				int dy = (int) (event.getY() - oldY);

				oldX = event.getX();
				oldY = event.getY();

				int dx_f = 0;
				int dy_f = 0;

				if (left >= l + dx && right <= r + dx)
				{
					dx_f = dx;
				}

				if (top >= t + dy && bottom <= b + dy)
				{
					dy_f = dy;
				}

				if (!(dx == 0 && dy == 0))
				{
					mDrawableDst.offset((int) dx_f, (int) dy_f);
					invalidate();
				}

			}
			break;
		}
		return true;
	}

	
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (mDrawable == null)
		{
			return; // couldn't resolve the URI
		}

		if (mDrawable.getIntrinsicWidth() == 0
				|| mDrawable.getIntrinsicHeight() == 0)
		{
			return; // nothing to draw (empty bounds)
		}

		configureBounds();

		mDrawable.draw(canvas);
		canvas.save();
		canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
		canvas.drawColor(Color.parseColor("#a0000000"));
		canvas.restore();
		mFloatDrawable.draw(canvas);
	}

	
	
	protected void configureBounds()
	{
		if (isFrist)
		{
			oriRationWH = ((float) mDrawable.getIntrinsicWidth())
					/ ((float) mDrawable.getIntrinsicHeight());
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int width = ((Activity) mContext).getWindowManager()
					.getDefaultDisplay().getWidth();
			int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth()
					* scale + 0.5f));
			int h = (int) (w / oriRationWH);
			floatWidth = dipTopx(mContext, cropWidth);
			floatHeight = dipTopx(mContext, cropHeight);
			floatLeft = (getWidth() - floatWidth) / 2;
			floatTop = (getHeight() - floatHeight) / 2;
			if (floatLeft <= 0)
			{
				floatLeft = 0;
			}
			// 图片的长宽
			if (w <= h)
			{
				minZoomIn = (float) cropWidth * scale / (float) w;
				maxZoomOut = minZoomIn * 2;
			} else
			{
				minZoomIn = (float) cropHeight * scale / (float) h;
				maxZoomOut = minZoomIn * 2;
			}
			int newWidth = (int) (minZoomIn * w);
			int newHeight = (int) ((float) newWidth / oriRationWH);
			int left = floatLeft;
			int top = floatTop;
			int right = floatLeft + w;
			int bottom = floatTop + h;
			minZoomIn = 1;
			maxZoomOut = 2;
			mDrawableSrc.set(left, top, right, bottom);
			mDrawableDst.set(mDrawableSrc);
			int floatRight = floatLeft + floatWidth >= width ? width
					- floatLeft : floatLeft + floatWidth;
			mDrawableFloat.set(floatLeft, floatTop, floatRight, floatTop
					+ floatHeight);
			isFrist = false;
		}

		mDrawable.setBounds(mDrawableDst);
		mFloatDrawable.setBounds(mDrawableFloat);
	}

	
	
	protected void checkBounds()
	{
		int newLeft = mDrawableDst.left;
		int newTop = mDrawableDst.top;

		boolean isChange = false;
		if (mDrawableDst.left < -mDrawableDst.width())
		{
			newLeft = -mDrawableDst.width();
			isChange = true;
		}

		if (mDrawableDst.top < -mDrawableDst.height())
		{
			newTop = -mDrawableDst.height();
			isChange = true;
		}

		if (mDrawableDst.left > getWidth())
		{
			newLeft = getWidth();
			isChange = true;
		}

		if (mDrawableDst.top > getHeight())
		{
			newTop = getHeight();
			isChange = true;
		}

		mDrawableDst.offsetTo(newLeft, newTop);
		if (isChange)
		{
			invalidate();
		}
	}

	
	
	public Bitmap getCropImage()
	{
		getBarHeight();
		View view = ((Activity) mContext).getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap screenShoot = view.getDrawingCache();

		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot,
				mDrawableFloat.left, mDrawableFloat.top + titleBarHeight + statusBarHeight + 90, 
				mDrawableFloat.width(), mDrawableFloat.height());

		return finalBitmap;
	}

	
	
	int statusBarHeight = 0;
	int titleBarHeight = 0;

	
	private void getBarHeight()
	{
		// ��ȡ״̬���߶�
		Rect frame = new Rect();
		((Activity) mContext).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		int contenttop = ((Activity) mContext).getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// statusBarHeight�����������״̬���ĸ߶�
		titleBarHeight = contenttop - statusBarHeight;

	}

	
	
	public int dipTopx(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	

}
