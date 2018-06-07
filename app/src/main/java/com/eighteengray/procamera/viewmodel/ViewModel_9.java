package com.eighteengray.procamera.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.share.ShareDialogBean;
import com.eighteengray.procamera.share.ShareDialogFragment;
import com.eighteengray.procamera.share.ShareListener;
import com.liulishuo.share.SsoShareManager;
import com.liulishuo.share.type.SsoShareType;


/**
 * Created by lutao on 2017/3/24.
 * 分享弹窗
 */
public class ViewModel_9 implements IViewModel<ShareDialogBean>
{

    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.view_model9, null);
    }

    @Override
    public void onBindView(final Context context, RecyclerView.ViewHolder holder, final ShareDialogBean shareDialogBean, final int position)
    {
        BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        RelativeLayout rl_container_model9 = baseRecyclerViewHolder.getViewById(R.id.rl_container_model9);
        final ImageView iv_icon_viewmodel9 = baseRecyclerViewHolder.getViewById(R.id.iv_icon_viewmodel9);
        final TextView tv_name_viewmodel9 = baseRecyclerViewHolder.getViewById(R.id.tv_name_viewmodel9);

        switch (shareDialogBean.showType){
            case ShareDialogFragment.QQ:
                iv_icon_viewmodel9.setImageResource(R.mipmap.share_dialog_qq);
                tv_name_viewmodel9.setText("QQ");
                break;
            case ShareDialogFragment.QQZONE:
                iv_icon_viewmodel9.setImageResource(R.mipmap.share_dialog_qzone);
                tv_name_viewmodel9.setText("QQ空间");
                break;
            case ShareDialogFragment.WECHAT:
                iv_icon_viewmodel9.setImageResource(R.mipmap.share_dialog_weixin);
                tv_name_viewmodel9.setText("微信");
                break;
            case ShareDialogFragment.CIRCLE:
                iv_icon_viewmodel9.setImageResource(R.mipmap.share_dialog_circle);
                tv_name_viewmodel9.setText("朋友圈");
                break;
            case ShareDialogFragment.WEIBO:
                iv_icon_viewmodel9.setImageResource(R.mipmap.share_dialog_weibo);
                tv_name_viewmodel9.setText("微博");
                break;
            case ShareDialogFragment.MORE:
                iv_icon_viewmodel9.setImageResource(R.mipmap.add_grey_24dp);
                tv_name_viewmodel9.setText("更多");
                break;
        }

        Activity activity = null;
        if(context instanceof Activity){
            activity = (Activity) context;
        }
        final Activity finalActivity = activity;
        rl_container_model9.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SsoShareManager.ShareStateListener mShareListener = new ShareListener(context);
                switch (shareDialogBean.showType){
                    case ShareDialogFragment.QQ:
                        SsoShareManager.share(finalActivity, SsoShareType.QQ_FRIEND, shareDialogBean.shareContent, mShareListener);
                        break;
                    case ShareDialogFragment.QQZONE:
                        SsoShareManager.share(finalActivity, SsoShareType.QQ_ZONE, shareDialogBean.shareContent, mShareListener);
                        break;
                    case ShareDialogFragment.WECHAT:
                        SsoShareManager.share(finalActivity, SsoShareType.WEIXIN_FRIEND, shareDialogBean.shareContent, mShareListener);
                        break;
                    case ShareDialogFragment.CIRCLE:
                        SsoShareManager.share(finalActivity, SsoShareType.WEIXIN_FRIEND_ZONE, shareDialogBean.shareContent, mShareListener);
                        break;
                    case ShareDialogFragment.WEIBO:
                        SsoShareManager.share(finalActivity, SsoShareType.WEIBO_TIME_LINE, shareDialogBean.shareContent, mShareListener);
                        break;
                    case ShareDialogFragment.MORE:

                        break;
                }
            }
        });
    }


}
