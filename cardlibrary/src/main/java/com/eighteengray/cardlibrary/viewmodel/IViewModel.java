package com.eighteengray.cardlibrary.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;


/**
 * Created by lutao on 2017/9/29.
 */

public interface IViewModel<T>
{
    public View onCreateView(LayoutInflater layoutInflater);
    public void onBindView(Context context, RecyclerView.ViewHolder holder, T data, int position);
}
















