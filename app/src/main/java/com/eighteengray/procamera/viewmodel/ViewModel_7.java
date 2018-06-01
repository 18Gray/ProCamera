package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.commonutillibrary.TimeDateUtil;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;

import java.text.ParseException;


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
        TextView tv_title_model7 = baseRecyclerViewHolder.getViewById(R.id.tv_title_model7);
        TextView tv_content_model7 = baseRecyclerViewHolder.getViewById(R.id.tv_content_model7);

        tv_title_model7.setText(settings.funcName);

        switch (settings.funcName){
            case "签名日期":
                try
                {
                   String timeStr = TimeDateUtil.long2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");
                    tv_content_model7.setText(timeStr);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
                break;
            case "位置":
                tv_content_model7.setText("引入高德地图");
                break;
            case "签名版权":
                tv_content_model7.setText("您的版权名称");
                break;
            case "签名字体大小":
                tv_content_model7.setText("16");
                break;
            case "签名字体颜色":
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_content_model7.getLayoutParams();
                layoutParams.width = 100;
                layoutParams.height = 100;
                tv_content_model7.setLayoutParams(layoutParams);
                tv_content_model7.setBackgroundColor(context.getResources().getColor(R.color.primary_text));
                break;
        }

        tv_content_model7.setOnClickListener(new View.OnClickListener()
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
