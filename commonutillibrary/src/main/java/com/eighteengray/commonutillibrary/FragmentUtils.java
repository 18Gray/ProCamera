package com.eighteengray.commonutillibrary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Razer on 2017/8/27.
 */

public class FragmentUtils
{
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
