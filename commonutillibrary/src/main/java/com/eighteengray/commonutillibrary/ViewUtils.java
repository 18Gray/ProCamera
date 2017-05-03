package com.eighteengray.commonutillibrary;

import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;
import static android.R.attr.x;
import static android.R.attr.y;

/**
 * View相关工具类
 */
public class ViewUtils
{

    /**
     * 获取触摸屏幕时的view
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static View getTouchTarget(View view, int x, int y)
    {
        View target = null;
        ArrayList<View> views = view.getTouchables();
        for(View child:views)
        {
            if(isTouchPointInView(view, x, y))
            {
                target = child;
                break;
            }
        }
        return target;
    }


    /**
     * 判断触摸屏幕的点是否在指定View中
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static boolean isTouchPointInView(View view, int x, int y)
    {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if(view.isClickable() && x >= left && x <= right && y >= top && y >= bottom)
        {
            return true;
        }
        return false;
    }


    //获取两指之间的距离
    public static float getDistance(MotionEvent event)
    {
        if(event.getPointerCount() < 2)
        {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return FloatMath.sqrt(x * x + y * y);
        }
        else
        {
            return 10;
        }
    }

    //取两指的中心点坐标
    public static PointF getMid(MotionEvent event)
    {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }



    //Fragment的工具
    public static void addFragment(FragmentActivity activity, int containerViewId, Fragment fragment)
    {
        if (activity != null)
        {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(containerViewId, fragment);
            ft.commitAllowingStateLoss();
        }
    }


    public static void removeFragment(FragmentActivity activity, Fragment fragment)
    {
        if(activity != null)
        {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }


    public static void showFragment(FragmentActivity activity, List<Fragment> fragments, Fragment fragment)
    {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment itemFragment : fragments)
        {
            if (itemFragment == fragment)
            {
                ft.show(itemFragment);
            } else
            {
                ft.hide(itemFragment);
            }
        }
        ft.commitAllowingStateLoss();
    }


}
