package com.eighteengray.procamera.widget.baserecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder>
{
    List<T> mDatas = new ArrayList<>();
    protected final int mItemLayoutId;


    public BaseRecyclerAdapter(int layoutid)
    {
        this.mItemLayoutId = layoutid;
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new BaseRecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position)
    {
        setData2ViewR(holder, mDatas.get(position));
    }


    public void setData(List<T> list)
    {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public abstract void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, T item);

}
