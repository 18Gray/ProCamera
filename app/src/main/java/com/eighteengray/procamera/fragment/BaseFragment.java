package com.eighteengray.procamera.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public abstract class BaseFragment extends Fragment
{
    /**
     * 依赖的activity
     */
    protected FragmentActivity mActivity;

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;

    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mActivity = getActivity();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (isVisibleToUser)
        {
            onVisibleToUser();
        }
    }


    protected void onVisibleToUser()
    {
        if (mIsPrepare && mIsVisible)
        {
            onLazyLoad();
        }
    }

    protected void onLazyLoad()
    {

    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        mIsPrepare = true;
        onLazyLoad();
        return mRootView;
    }


    protected <T extends View> T findViewById(int id)
    {
        if (mRootView == null)
        {
            return null;
        }
        return (T) mRootView.findViewById(id);
    }


    /**
     * 设置根布局资源id
     *
     * @return
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     */
    protected abstract int setLayoutResouceId();





}
