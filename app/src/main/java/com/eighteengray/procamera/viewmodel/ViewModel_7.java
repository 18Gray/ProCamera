package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;


/**
 * Created by lutao on 2017/3/24.
 * 设置中的图片质量这类
 */
public class ViewModel_7 implements IViewModel<Settings>
{

    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model7, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, final Settings settings, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        TextView tv_view_model7 = baseRecyclerViewHolder.getViewById(R.id.tv_view_model7);
        tv_view_model7.setText(settings.funcName);

        Button btn_select_format_viewmodel7 = baseRecyclerViewHolder.getViewById(R.id.btn_select_format_viewmodel7);
        btn_select_format_viewmodel7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (settings.funcName){
                    case "签名日期":
                        Toast.makeText(context, "签名日期", Toast.LENGTH_SHORT).show();
                        break;
                    case "位置":
                        Toast.makeText(context, "位置", Toast.LENGTH_SHORT).show();
                        break;
                    case "签名版权":
                        Toast.makeText(context, "签名版权", Toast.LENGTH_SHORT).show();
                        break;
                    case "签名字体大小":
                        Toast.makeText(context, "签名字体大小", Toast.LENGTH_SHORT).show();
                        break;
                    case "签名字体颜色":
                        Toast.makeText(context, "签名字体颜色", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


}
