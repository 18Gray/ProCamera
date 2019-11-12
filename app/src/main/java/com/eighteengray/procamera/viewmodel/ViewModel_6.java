package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.commonutils.SharePreferenceUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procameralibrary.common.Constants;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.Switch;



/**
 * Created by lutao on 2017/3/24.
 * 设置中的是否开启某功能，Switch
 */
public class ViewModel_6 implements IViewModel<Settings>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model6, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, final Settings settings, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_icon_model6 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_model6);
        TextView tv_view_model6 = baseRecyclerViewHolder.getViewById(R.id.tv_view_model6);
        Switch switch_viewmodel6 = baseRecyclerViewHolder.getViewById(R.id.switch_viewmodel6);

        iv_icon_model6.setImageResource(settings.resourceId);
        tv_view_model6.setText(settings.funcName);

        String key = null;
        switch(settings.funcName){
            case "九宫格":
                key = Constants.IMAGE_GRID;
                break;
            case "矫衡器":
                key = Constants.IMAGE_BALANCE;
                break;
            case "防手抖":
                key = Constants.IMAGE_ANTI_SHAKE;
                break;
            case "拍摄静音":
                key = Constants.IMAGE_MUTE;
                break;
            case "实时直方图":
                key = Constants.IMAGE_HISTOGRAM;
                break;
            case "开启签名":
                key = Constants.IMAGE_SIGN;
                break;
        }
        boolean isChecked = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getBoolean(key, false);
        switch_viewmodel6.setChecked(isChecked);

        switch_viewmodel6.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b)
            {
                String key = null;
                switch(settings.funcName){
                    case "九宫格":
                        key = Constants.IMAGE_GRID;
                        break;
                    case "矫衡器":
                        key = Constants.IMAGE_BALANCE;
                        break;
                    case "防手抖":
                        key = Constants.IMAGE_ANTI_SHAKE;
                        break;
                    case "拍摄静音":
                        key = Constants.IMAGE_MUTE;
                        break;
                    case "实时直方图":
                        key = Constants.IMAGE_HISTOGRAM;
                        break;
                    case "开启签名":
                        key = Constants.IMAGE_SIGN;
                        break;
                }
                SharePreferenceUtils.getInstance(context, Constants.SETTINGS).putBoolean(key, b, false);
            }
        });
    }


}
