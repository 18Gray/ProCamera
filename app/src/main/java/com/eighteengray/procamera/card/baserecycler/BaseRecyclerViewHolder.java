package com.eighteengray.procamera.card.baserecycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder
{
    private View itemView;
    private SparseArray<View> mViews;

    public BaseRecyclerViewHolder(View itemView)
    {
        super(itemView);
        this.itemView = itemView;
        this.mViews = new SparseArray<>();
    }


    /**
     * 通过控件的Id获取控件，如果没有则加入views
     */
    public <T extends View> T getViewById(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


}
