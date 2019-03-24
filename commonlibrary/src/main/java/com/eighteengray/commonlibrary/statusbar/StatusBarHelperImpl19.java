package com.eighteengray.commonlibrary.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * set translucent status bar on 4.4
 * Created by naturs on 2016/2/21.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class StatusBarHelperImpl19 extends StatusBarHelperImplBase {

    protected View mStatusBarView;
    protected Drawable mStatusBarDrawable;

    public StatusBarHelperImpl19(Activity activity) {
        super(activity);
    }

    @Override
    protected void setColor(int color) {
        setDrawable(new ColorDrawable(color));
    }

    @Override
    protected void setDrawable(Drawable drawable) {
        setStatusBarTranslucent(true);
        setupStatusBarView();
        setActivityRootLayoutFitSystemWindowsInternal();
        mStatusBarDrawable = drawable;
        mStatusBarView.setBackground(drawable);
    }

    @Override
    protected void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor)
    {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);

        View mContentChild = mContentView.getChildAt(0);
        mContentChild.setFitsSystemWindows(false);
        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);

        toolbar.setFitsSystemWindows(false);
        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            int statusBarHeight = getStatusBarHeight();
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            toolbar.setTag(true);
        }

        int statusBarHeight = getStatusBarHeight();
        removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, statusBarHeight);
        final View statusView = addFakeStatusBarView(activity, statusColor, statusBarHeight);

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
            int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                statusView.setAlpha(1f);
            } else {
                statusView.setAlpha(0f);
            }
        } else {
            statusView.setAlpha(0f);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    if (statusView.getAlpha() == 0) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }
                } else {
                    if (statusView.getAlpha() == 1) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }
                }
            }
        });
    }

    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_MARGIN_ADDED = "marginAdded";
    private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin -= statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(null);
        }
    }

    @Override
    protected void destroy() {
        destroyStatusBarView();
        setStatusBarTranslucent(false);
    }

    /**
     * 设置状态栏透明
     * @param on true为设置，false为取消
     */
    protected void setStatusBarTranslucent(boolean on) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            if ((params.flags & bits) == 0) {
                params.flags |= bits;
                window.setAttributes(params);
            }
        } else {
            if ((params.flags & bits) != 0) {
                params.flags &= ~bits;
                window.setAttributes(params);
            }
        }
    }

    /**
     * 添加用于着色的View
     */
    protected void setupStatusBarView() {
        if (mStatusBarView == null) {
            mStatusBarView = new View(mActivity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());

            ViewGroup contentLayout = (ViewGroup) mActivity.findViewById(android.R.id.content);
            contentLayout.addView(mStatusBarView, lp);
        }
    }

    /**
     * 移除用于着色的View
     */
    protected void destroyStatusBarView() {
        if (mStatusBarView != null) {
            ViewGroup contentLayout = (ViewGroup) mActivity.findViewById(android.R.id.content);
            contentLayout.removeView(mStatusBarView);
            mStatusBarView = null;
        }
    }

}
