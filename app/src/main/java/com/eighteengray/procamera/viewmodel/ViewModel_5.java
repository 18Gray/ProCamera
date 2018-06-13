package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.commonutillibrary.SharePreferenceUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.Spinner;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lutao on 2017/3/24.
 * 设置中的图片质量这类
 */
public class ViewModel_5 implements IViewModel<Settings>
{

    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model5, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, Settings settings, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        ImageView iv_icon_model5 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_model5);
        TextView tv_view_model5 = baseRecyclerViewHolder.getViewById(R.id.tv_view_model5);
        final Spinner spinner_viewmodel5 = baseRecyclerViewHolder.getViewById(R.id.spinner_viewmodel5);

        iv_icon_model5.setImageResource(settings.resourceId);

        tv_view_model5.setText(settings.funcName);

        if(settings.funcName.equals("图片质量")){
            String[] items1 = new String[4];
            items1[0] = "1836p";
            items1[1] = "1152p";
            items1[2] = "1080p";
            items1[3] = "720p";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.row_spn, items1);
            adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
            spinner_viewmodel5.setAdapter(adapter);

            int selectedItem = 0;
            String selected = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(Constants.IMAGE_QUALITY, "1080p");
            switch (selected){
                case "1836p":
                    selectedItem = 0;
                    break;
                case "1152p":
                    selectedItem = 1;
                    break;
                case "1080p":
                    selectedItem = 2;
                    break;
                case "720p":
                    selectedItem = 3;
                    break;
            }
            spinner_viewmodel5.setSelection(selectedItem);

            spinner_viewmodel5.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(Spinner spinner, View view, int i, long l)
                {
                    String selectedItem = (String) spinner_viewmodel5.getSelectedItem();
                    SharePreferenceUtils.getInstance(context, Constants.SETTINGS).putString(Constants.IMAGE_QUALITY, selectedItem, true);
                }
            });

        }else if(settings.funcName.equals("图片格式")){
            String[] items2 = new String[4];
            items2[0] = "JPG";
            items2[1] = "PNG";
            items2[2] = "RAW";
            items2[3] = "TIFF";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.row_spn, items2);
            adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
            spinner_viewmodel5.setAdapter(adapter);

            int selectedItem = 0;
            String selected = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(Constants.IMAGE_FORMAT, "JPG");
            switch (selected){
                case "JPG":
                    selectedItem = 0;
                    break;
                case "PNG":
                    selectedItem = 1;
                    break;
                case "RAW":
                    selectedItem = 2;
                    break;
                case "TIFF":
                    selectedItem = 3;
                    break;
            }
            spinner_viewmodel5.setSelection(selectedItem);

            spinner_viewmodel5.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(Spinner spinner, View view, int i, long l)
                {
                    String selectedItem = (String) spinner_viewmodel5.getSelectedItem();
                    SharePreferenceUtils.getInstance(context, Constants.SETTINGS).putString(Constants.IMAGE_FORMAT, selectedItem, true);
                }
            });

        }else if(settings.funcName.equals("签名日期")){
            String[] items3 = new String[4];
            items3[0] = "年-月-日 时:分:秒";
            items3[1] = "年-月-日 时:分";
            items3[2] = "年.月.日 时:分:秒";
            items3[3] = "年.月.日 时:分";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.row_spn, items3);
            adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
            spinner_viewmodel5.setAdapter(adapter);

            int selectedItem = 0;
            String selected = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(Constants.IMAGE_SIGN_DATE, "年-月-日 时:分:秒");
            switch (selected){
                case "年-月-日 时:分:秒":
                    selectedItem = 0;
                    break;
                case "年-月-日 时:分":
                    selectedItem = 1;
                    break;
                case "年.月.日 时:分:秒":
                    selectedItem = 2;
                    break;
                case "年.月.日 时:分":
                    selectedItem = 3;
                    break;
            }
            spinner_viewmodel5.setSelection(selectedItem);

            spinner_viewmodel5.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(Spinner spinner, View view, int i, long l)
                {
                    String selectedItem = (String) spinner_viewmodel5.getSelectedItem();
                    SharePreferenceUtils.getInstance(context, Constants.SETTINGS).putString(Constants.IMAGE_SIGN_DATE, selectedItem, true);
                }
            });
        }else if(settings.funcName.equals("签名字体大小")){
            String[] items3 = new String[6];
            items3[0] = "24";
            items3[1] = "22";
            items3[2] = "20";
            items3[3] = "18";
            items3[4] = "16";
            items3[5] = "14";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.row_spn, items3);
            adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
            spinner_viewmodel5.setAdapter(adapter);

            int selectedItem = 0;
            String selected = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(Constants.IMAGE_SIGN_TEXT_SIZE, "18");
            switch (selected){
                case "24":
                    selectedItem = 0;
                    break;
                case "22":
                    selectedItem = 1;
                    break;
                case "20":
                    selectedItem = 2;
                    break;
                case "18":
                    selectedItem = 3;
                    break;
                case "16":
                    selectedItem = 4;
                    break;
                case "14":
                    selectedItem = 5;
                    break;
            }
            spinner_viewmodel5.setSelection(selectedItem);

            spinner_viewmodel5.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(Spinner spinner, View view, int i, long l)
                {
                    String selectedItem = (String) spinner_viewmodel5.getSelectedItem();
                    SharePreferenceUtils.getInstance(context, Constants.SETTINGS).putString(Constants.IMAGE_SIGN_TEXT_SIZE, selectedItem, true);
                }
            });
        }
    }


}
