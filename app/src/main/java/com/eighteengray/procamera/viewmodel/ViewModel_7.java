package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.commonutils.SharePreferenceUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procameralibrary.common.Constants;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageView;


/**
 * Created by lutao on 2017/3/24.
 * 设置中的签名日期这类,TextView
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
        ImageView iv_icon_model7 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_model7);
        TextView tv_title_model7 = baseRecyclerViewHolder.getViewById(R.id.tv_title_model7);
        final TextView tv_content_model7 = baseRecyclerViewHolder.getViewById(R.id.tv_content_model7);

        iv_icon_model7.setImageResource(settings.resourceId);
        tv_title_model7.setText(settings.funcName);

        switch (settings.funcName){
            case "位置":
                tv_content_model7.setText("引入高德地图");
                break;
            case "签名版权":
                String signName = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(Constants.IMAGE_SIGN_NAME, "签名版权");
                tv_content_model7.setText(signName);
                tv_content_model7.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Dialog.Builder builder = new SimpleDialog.Builder(){
                            @Override
                            protected void onBuildDone(Dialog dialog) {
                                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            }

                            @Override
                            public void onPositiveActionClicked(DialogFragment fragment) {
                                EditText et_signname = (EditText)fragment.getDialog().findViewById(R.id.et_signname);
                                String content = et_signname.getText().toString();
                                SharePreferenceUtils.getInstance(context, Constants.SETTINGS).putString(Constants.IMAGE_SIGN_NAME, content, true);
                                tv_content_model7.setText(content);
                                super.onPositiveActionClicked(fragment);
                            }

                            @Override
                            public void onNegativeActionClicked(DialogFragment fragment) {
                                super.onNegativeActionClicked(fragment);
                            }
                        };
                        builder.title("签名版权")
                                .positiveAction("确定")
                                .negativeAction("取消")
                                .contentView(R.layout.layout_dialog);
                        DialogFragment fragment = DialogFragment.newInstance(builder);
                        FragmentActivity fragmentActivity = null;
                        if(context instanceof FragmentActivity){
                            fragmentActivity = (FragmentActivity) context;
                        }
                        fragment.show(fragmentActivity.getSupportFragmentManager(), null);
                    }
                });
                break;
            case "签名字体颜色":
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_content_model7.getLayoutParams();
                layoutParams.width = 100;
                layoutParams.height = 100;
                tv_content_model7.setLayoutParams(layoutParams);
                tv_content_model7.setBackgroundColor(context.getResources().getColor(R.color.primary_text));
                break;
        }
    }
}
