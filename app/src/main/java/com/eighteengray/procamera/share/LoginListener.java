package com.eighteengray.procamera.share;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.eighteengray.procamera.MainActivity;
import com.liulishuo.share.OAuthUserInfo;
import com.liulishuo.share.SsoLoginManager;
import com.liulishuo.share.SsoUserInfoManager;
import com.liulishuo.share.type.SsoLoginType;


class LoginListener implements SsoLoginManager.LoginListener
{

    private Context context;

    private
    @SsoLoginType
    String type;

    LoginListener(Context c, @SsoLoginType String type) {
        this.context = c;
        this.type = type;
    }

    @Override
    public void onSuccess(String accessToken, String userId, long expiresIn, String data) {
        loadUserInfo(accessToken, userId);

        String result = "登录成功";
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        String result = "登录失败,失败信息：" + msg;
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        String result = "取消登录";
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载用户的个人信息
     */
    private void loadUserInfo(String accessToken, String userId) {
        SsoUserInfoManager.getUserInfo(context, type, accessToken, userId, new SsoUserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(@NonNull final OAuthUserInfo userInfo) {
                final String info = " nickname = " + userInfo.nickName + "\n"
                        + " sex = " + userInfo.sex + "\n"
                        + " id = " + userInfo.userId;
            }

            @Override
            public void onError(String msg) {
            }
        });
    }
}
