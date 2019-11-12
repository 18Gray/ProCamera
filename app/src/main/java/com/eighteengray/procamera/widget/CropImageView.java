package com.eighteengray.procamera.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class CropImageView extends View
{
    // 在touch重要用到的点，
    private float mX_1 = 0;
    private float mY_1 = 0;
    // 触摸事件判断
    private final int STATUS_SINGLE = 1;
    private final int STATUS_MULTI_START = 2;
    private final int STATUS_MULTI_TOUCHING = 3;
    // 当前状态
    private int mStatus = STATUS_SINGLE;
    // 默认裁剪的宽高
    private int cropWidth;
    private int cropHeight;
    // 浮层Drawable的四个点
    private final int EDGE_LT = 1;
    private final int EDGE_RT = 2;
    private final int EDGE_LB = 3;
    private final int EDGE_RB = 4;
    private final int EDGE_MOVE_IN = 5;
    private final int EDGE_MOVE_OUT = 6;
    private final int EDGE_NONE = 7;

    public int currentEdge = EDGE_NONE;

    protected float oriRationWH = 0;
    protected final float maxZoomOut = 5.0f;
    protected final float minZoomIn = 0.333333f;

    protected Drawable mDrawable;
    protected FloatDrawable mFloatDrawable;

    protected Rect mDrawableSrc = new Rect();// 图片Rect变换时的Rect
    protected Rect mDrawableDst = new Rect();// 图片Rect
    protected Rect mDrawableFloat = new Rect();// 浮层的Rect
    protected boolean isFrist = true;
    private boolean isTouchInSquare = true;

    protected Context mContext;

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

    private void init(Context context)
    {
        this.mContext = context;
        try
        {
            if (android.os.Build.VERSION.SDK_INT >= 11)
            {
                this.setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        mFloatDrawable = new FloatDrawable(context);
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

        if (event.getPointerCount() > 1)
        {
            if (mStatus == STATUS_SINGLE)
            {
                mStatus = STATUS_MULTI_START;
            } else if (mStatus == STATUS_MULTI_START)
            {
                mStatus = STATUS_MULTI_TOUCHING;
            }
        } else
        {
            if (mStatus == STATUS_MULTI_START
                    || mStatus == STATUS_MULTI_TOUCHING)
            {
                mX_1 = event.getX();
                mY_1 = event.getY();
            }

            mStatus = STATUS_SINGLE;
        }

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mX_1 = event.getX();
                mY_1 = event.getY();
                currentEdge = getTouch((int) mX_1, (int) mY_1);
                isTouchInSquare = mDrawableFloat.contains((int) event.getX(),
                        (int) event.getY());

                break;

            case MotionEvent.ACTION_UP:
                checkBounds();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                currentEdge = EDGE_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mStatus == STATUS_MULTI_TOUCHING)
                {

                } else if (mStatus == STATUS_SINGLE)
                {
                    int dx = (int) (event.getX() - mX_1);
                    int dy = (int) (event.getY() - mY_1);

                    mX_1 = event.getX();
                    mY_1 = event.getY();
                    // 根據得到的那一个角，并且变换Rect
                    if (!(dx == 0 && dy == 0))
                    {
                        switch (currentEdge)
                        {
                            case EDGE_LT:
                                mDrawableFloat.set(mDrawableFloat.left + dx,
                                        mDrawableFloat.top + dy, mDrawableFloat.right,
                                        mDrawableFloat.bottom);
                                break;

                            case EDGE_RT:
                                mDrawableFloat.set(mDrawableFloat.left,
                                        mDrawableFloat.top + dy, mDrawableFloat.right
                                                + dx, mDrawableFloat.bottom);
                                break;

                            case EDGE_LB:
                                mDrawableFloat.set(mDrawableFloat.left + dx,
                                        mDrawableFloat.top, mDrawableFloat.right,
                                        mDrawableFloat.bottom + dy);
                                break;

                            case EDGE_RB:
                                mDrawableFloat.set(mDrawableFloat.left,
                                        mDrawableFloat.top, mDrawableFloat.right + dx,
                                        mDrawableFloat.bottom + dy);
                                break;

                            case EDGE_MOVE_IN:
                                if (isTouchInSquare)
                                {
                                    mDrawableFloat.offset((int) dx, (int) dy);
                                }
                                break;

                            case EDGE_MOVE_OUT:
                                break;
                        }
                        mDrawableFloat.sort();
                        invalidate();
                    }
                }
                break;
        }

        return true;
    }

    // 根据初触摸点判断是触摸的Rect哪一个角
    public int getTouch(int eventX, int eventY)
    {
        if (mFloatDrawable.getBounds().left <= eventX
                && eventX < (mFloatDrawable.getBounds().left + mFloatDrawable
                .getBorderWidth())
                && mFloatDrawable.getBounds().top <= eventY
                && eventY < (mFloatDrawable.getBounds().top + mFloatDrawable
                .getBorderHeight()))
        {
            return EDGE_LT;
        } else if ((mFloatDrawable.getBounds().right - mFloatDrawable
                .getBorderWidth()) <= eventX
                && eventX < mFloatDrawable.getBounds().right
                && mFloatDrawable.getBounds().top <= eventY
                && eventY < (mFloatDrawable.getBounds().top + mFloatDrawable
                .getBorderHeight()))
        {
            return EDGE_RT;
        } else if (mFloatDrawable.getBounds().left <= eventX
                && eventX < (mFloatDrawable.getBounds().left + mFloatDrawable
                .getBorderWidth())
                && (mFloatDrawable.getBounds().bottom - mFloatDrawable
                .getBorderHeight()) <= eventY
                && eventY < mFloatDrawable.getBounds().bottom)
        {
            return EDGE_LB;
        } else if ((mFloatDrawable.getBounds().right - mFloatDrawable
                .getBorderWidth()) <= eventX
                && eventX < mFloatDrawable.getBounds().right
                && (mFloatDrawable.getBounds().bottom - mFloatDrawable
                .getBorderHeight()) <= eventY
                && eventY < mFloatDrawable.getBounds().bottom)
        {
            return EDGE_RB;
        } else if (mFloatDrawable.getBounds().contains(eventX, eventY))
        {
            return EDGE_MOVE_IN;
        }
        return EDGE_MOVE_OUT;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        if (mDrawable == null)
        {
            return;
        }

        if (mDrawable.getIntrinsicWidth() == 0
                || mDrawable.getIntrinsicHeight() == 0)
        {
            return;
        }

        configureBounds();
        // 在画布上花图片
        mDrawable.draw(canvas);
        canvas.save();
        // 在画布上画浮层FloatDrawable,Region.Op.DIFFERENCE是表示Rect交集的补集
        canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
        // 在交集的补集上画上灰色用来区分
        canvas.drawColor(Color.parseColor("#a0000000"));
        canvas.restore();
        // 画浮层
        mFloatDrawable.draw(canvas);
    }

    protected void configureBounds()
    {
        // configureBounds在onDraw方法中调用
        // isFirst的目的是下面对mDrawableSrc和mDrawableFloat只初始化一次，
        // 之后的变化是根据touch事件来变化的，而不是每次执行重新对mDrawableSrc和mDrawableFloat进行设置
        if (isFrist)
        {
            oriRationWH = ((float) mDrawable.getIntrinsicWidth())
                    / ((float) mDrawable.getIntrinsicHeight());

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth()
                    * scale + 0.5f));
            int h = (int) (w / oriRationWH);

            int left = (getWidth() - w) / 2;
            int top = (getHeight() - h) / 2;
            int right = left + w;
            int bottom = top + h;

            mDrawableSrc.set(left, top, right, bottom);
            mDrawableDst.set(mDrawableSrc);

            int floatWidth = dipTopx(mContext, cropWidth);
            int floatHeight = dipTopx(mContext, cropHeight);

            if (floatWidth > getWidth())
            {
                floatWidth = getWidth();
                floatHeight = cropHeight * floatWidth / cropWidth;
            }

            if (floatHeight > getHeight())
            {
                floatHeight = getHeight();
                floatWidth = cropWidth * floatHeight / cropHeight;
            }

            int floatLeft = (getWidth() - floatWidth) / 2;
            int floatTop = (getHeight() - floatHeight) / 2;
            mDrawableFloat.set(floatLeft, floatTop, floatLeft + floatWidth,
                    floatTop + floatHeight);

            isFrist = false;
        }

        mDrawable.setBounds(mDrawableDst);
        mFloatDrawable.setBounds(mDrawableFloat);
    }

    // 在up事件中调用了该方法，目的是检查是否把浮层拖出了屏幕
    protected void checkBounds()
    {
        int newLeft = mDrawableFloat.left;
        int newTop = mDrawableFloat.top;

        boolean isChange = false;
        if (mDrawableFloat.left < getLeft())
        {
            newLeft = getLeft();
            isChange = true;
        }

        if (mDrawableFloat.top < getTop())
        {
            newTop = getTop();
            isChange = true;
        }

        if (mDrawableFloat.right > getRight())
        {
            newLeft = getRight() - mDrawableFloat.width();
            isChange = true;
        }

        if (mDrawableFloat.bottom > getBottom())
        {
            newTop = getBottom() - mDrawableFloat.height();
            isChange = true;
        }

        mDrawableFloat.offsetTo(newLeft, newTop);
        if (isChange)
        {
            invalidate();
        }
    }

    // 进行图片的裁剪，所谓的裁剪就是根据Drawable的新的坐标在画布上创建一张新的图片
    public Bitmap getCropImage()
    {
        Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tmpBitmap);
        mDrawable.draw(canvas);

        Matrix matrix = new Matrix();
        float scale = (float) (mDrawableSrc.width())
                / (float) (mDrawableDst.width());
        matrix.postScale(scale, scale);

        Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left,
                mDrawableFloat.top, mDrawableFloat.width(),
                mDrawableFloat.height(), matrix, true);
        tmpBitmap.recycle();
        tmpBitmap = null;

        return ret;
    }

    public int dipTopx(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
