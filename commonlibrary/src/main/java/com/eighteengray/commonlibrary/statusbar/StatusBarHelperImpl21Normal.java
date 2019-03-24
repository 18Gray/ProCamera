package com.eighteengray.commonlibrary.statusbar;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 使用5.x自带的方法设置status bar颜色。适用于大部分的情况。
 * Created by naturs on 2016/2/21.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class StatusBarHelperImpl21Normal extends StatusBarHelperImplBase {

    private static ValueAnimator sAnimator;

    public StatusBarHelperImpl21Normal(Activity activity) {
        super(activity);
    }

    @Override
    protected void setColor(int color) {
        mActivity.getWindow().setStatusBarColor(color);
        setActivityRootLayoutFitSystemWindowsInternal();
    }

    @Override
    protected void setDrawable(Drawable drawable) {
        // not support
    }

    @Override
    protected void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, final int statusColor)
    {
        final Window window = activity.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return insets;
            }
        });

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }

        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);

        toolbar.setFitsSystemWindows(false);
        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            int statusBarHeight = getStatusBarHeight();
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            toolbar.setTag(true);
        }

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
            int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                window.setStatusBarColor(statusColor);
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        } else {
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        collapsingToolbarLayout.setFitsSystemWindows(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    if (window.getStatusBarColor() != statusColor) {
                        startColorAnimation(window.getStatusBarColor(), statusColor, collapsingToolbarLayout.getScrimAnimationDuration(), window);
                    }
                } else {
                    if (window.getStatusBarColor() != Color.TRANSPARENT) {
                        startColorAnimation(window.getStatusBarColor(), Color.TRANSPARENT, collapsingToolbarLayout.getScrimAnimationDuration(), window);
                    }
                }
            }
        });
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
    }

    static void startColorAnimation(int startColor, int endColor, long duration, final Window window) {
        if (sAnimator != null) {
            sAnimator.cancel();
        }
        sAnimator = ValueAnimator.ofArgb(startColor, endColor)
                .setDuration(duration);
        sAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (window != null) {
                    window.setStatusBarColor((Integer) valueAnimator.getAnimatedValue());
                }
            }
        });
        sAnimator.start();
    }

    @Override
    protected void destroy() {
        // don't need
    }
}
