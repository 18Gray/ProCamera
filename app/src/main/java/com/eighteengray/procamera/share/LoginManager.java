package com.eighteengray.procamera.share;

import android.app.Activity;
import android.content.Context;
import com.liulishuo.share.SsoLoginManager;
import com.liulishuo.share.type.SsoLoginType;


public class LoginManager
{
    private Context context;

    public static final int QQ = 1;
    public static final int WECHAT = 2;
    public static final int WEIBO = 3;

    private static LoginManager INSTANCE = new LoginManager();

    public LoginManager getInstance(Context c){
        this.context = c;
        return INSTANCE;
    }

    public void loginIn(int type){

        Activity activity = null;
        if(context instanceof Activity){
            activity = (Activity) context;
        }

        switch (type){
            case QQ:
                SsoLoginManager.login(activity, SsoLoginType.QQ, new LoginListener(context, SsoLoginType.QQ));
                break;
            case WECHAT:
                SsoLoginManager.login(activity, SsoLoginType.WEIXIN, new LoginListener(context, SsoLoginType.WEIXIN));
                break;
            case WEIBO:
                SsoLoginManager.login(activity, SsoLoginType.WEIBO, new LoginListener(context, SsoLoginType.WEIBO));
                break;
        }
    }

}
