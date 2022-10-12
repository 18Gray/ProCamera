package com.eighteengray.cardlibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.viewmodel.ViewModelFactory;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    Context context;
    LayoutInflater layoutInflater;
    List<BaseDataBean<T>> dataBeanList = new ArrayList<>();
    String viewModelPackage;


    public BaseRecyclerAdapter(Context c, String vmp) {
        this.context = c;
        layoutInflater = LayoutInflater.from(context);
        this.viewModelPackage = vmp;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataBeanList.get(position).viewModelNum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        IViewModel viewModel = ViewModelFactory.createViewModel(viewModelPackage, viewType);
        if(viewModel != null) {
           view = viewModel.onCreateView(layoutInflater);
        }
        return new BaseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseDataBean<T> dataBean = dataBeanList.get(position);
        //BaseDataBean中的自定义数据
        T data = dataBean.data;

        //生成IViewModel类
        IViewModel viewModel = ViewModelFactory.createViewModel(viewModelPackage, getItemViewType(position));
        if(viewModel != null) {
            //绑定View
            viewModel.onBindView(context, holder, data, position);
        }
    }


    public void setData(List<BaseDataBean<T>> dataBeanList)
    {
        this.dataBeanList = dataBeanList;
        notifyDataSetChanged();
    }


}
