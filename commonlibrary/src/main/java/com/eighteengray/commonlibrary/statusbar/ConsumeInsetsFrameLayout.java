package com.eighteengray.commonlibrary.statusbar;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by naturs on 2016/2/21.
 */
final class ConsumeInsetsFrameLayout extends FrameLayout
{
    private Rect mInsets = new Rect();

    private OnInsetsCallback mOnInsetsCallback;

    public ConsumeInsetsFrameLayout(Context context) {
        super(context);
    }

    public ConsumeInsetsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConsumeInsetsFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mInsets.left = insets.left;
            mInsets.top = insets.top;
            mInsets.right = insets.right;
            mInsets.bottom = insets.bottom;

            // Intentionally do not modify the bottom inset. For some reason,
            // if the bottom inset is modified, window resizing stops working.
            // maybe: insets.bottom has value(eg. Rect(0, 50 - 0, 597)) when keyboard is showing.
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;

            if (mOnInsetsCallback != null) {
                mOnInsetsCallback.onInsetsChanged(mInsets);
            }
        }

        return super.fitSystemWindows(insets);
    }

    public void setOnInsetsCallback(OnInsetsCallback onInsetsCallback) {
        mOnInsetsCallback = onInsetsCallback;
    }

    public interface OnInsetsCallback {
        void onInsetsChanged(Rect insets);
    }
}