package com.eighteengray.procamera.card.baserecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eighteengray.procamera.card.viewmodel.IViewModel;
import java.util.ArrayList;
import java.util.List;


public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter
{
    Context context;
    LayoutInflater layoutInflater;
    List<BaseDataBean<T>> dataBeanList = new ArrayList<>();

    public BaseRecyclerAdapter(Context c)
    {
        this.context = c;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return dataBeanList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataBeanList.get(position).viewModelNum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        IViewModel viewModel = ViewModelFactory.viewModelFactory(viewType);
        View view = viewModel.onCreateView(layoutInflater);
        return new BaseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        BaseDataBean<T> dataBean = dataBeanList.get(position);
        //BaseDataBean中的自定义数据
        T data = dataBean.data;

        //生成IViewModel类
        IViewModel viewModel = ViewModelFactory.viewModelFactory(getItemViewType(position));

        //绑定View
        viewModel.onBindView(context, holder, data, position);
    }


    public void setData(List<BaseDataBean<T>> dataBeanList)
    {
        this.dataBeanList = dataBeanList;
        notifyDataSetChanged();
    }


}
