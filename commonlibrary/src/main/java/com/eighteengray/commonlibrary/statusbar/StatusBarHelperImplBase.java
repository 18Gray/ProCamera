package com.eighteengray.commonlibrary.statusbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

/**
 *
 * Created by naturs on 2016/2/21.
 */
class StatusBarHelperImplBase extends StatusBarHelperImpl {

    public StatusBarHelperImplBase(Activity activity) {
        super(activity);
    }

    @Override
    protected void setColor(int color) {
        // do noting
    }

    @Override
    protected void setDrawable(Drawable drawable) {
        // do noting
    }

    @Override
    protected void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor)
    {

    }

    @Override
    protected void destroy() {
        // do noting
    }

}
