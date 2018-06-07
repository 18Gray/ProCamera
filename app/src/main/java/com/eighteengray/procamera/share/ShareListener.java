package com.eighteengray.procamera.share;


import android.content.Context;
import android.widget.Toast;
import com.liulishuo.share.SsoShareManager;



public class ShareListener implements SsoShareManager.ShareStateListener
{
    private Context context;

    public ShareListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess() {
        String result = "分享成功";
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        String result = "分享失败，出错信息：" + msg;
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        String result = "取消分享";
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }
}
