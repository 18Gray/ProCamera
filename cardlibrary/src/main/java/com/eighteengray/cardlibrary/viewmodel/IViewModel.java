package com.eighteengray.cardlibrary.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by lutao on 2017/9/29.
 */

public interface IViewModel<T> {
    View onCreateView(LayoutInflater layoutInflater);
    void onBindView(Context context, RecyclerView.ViewHolder holder, T data, int position);
}
















