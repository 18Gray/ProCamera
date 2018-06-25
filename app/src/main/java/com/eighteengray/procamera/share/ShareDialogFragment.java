package com.eighteengray.procamera.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.business.GenerateDataUtils;
import com.eighteengray.procameralibrary.common.Constants;
import com.liulishuo.share.ShareLoginSDK;
import com.liulishuo.share.SlConfig;
import com.liulishuo.share.content.ShareContent;
import com.liulishuo.share.content.ShareContentWebPage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShareDialogFragment extends DialogFragment
{
    View view;

    @BindView(R.id.recycler_view)
    RecyclerLayout recycler_view;

    ArrayList<ShareDialogBean> shareDialogBeanArrayList = new ArrayList<>();
    String title;
    String content;
    String url;
    String imageUri;
    ShareContent shareContent;

    protected static String QQ_APPID, QQ_SCOPE,
            WEIBO_APPID, WEIBO_SCOPE, WEIBO_REDIRECT_URL,
            WEIXIN_APPID, WEIXIN_SECRET;

    public static final int QQ = 1;
    public static final int QQZONE = 2;
    public static final int WECHAT = 3;
    public static final int CIRCLE = 4;
    public static final int WEIBO = 5;
    public static final int MORE = 6;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        shareDialogBeanArrayList = (ArrayList<ShareDialogBean>) bundle.getSerializable(Constants.SHARE_DIALOG_TYPE);
        title = bundle.getString(Constants.SHARE_TITLE);
        content = bundle.getString(Constants.SHARE_CONTENT);
        url = bundle.getString(Constants.SHARE_URL);
        imageUri = bundle.getString(Constants.SHARE_IMAGE_URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //全屏显示
        Window window = getDialog().getWindow();
        view = inflater.inflate(R.layout.layout_common_recycler, null);
        ButterKnife.bind(this, view);

        // 设置宽度为屏宽, 靠近屏幕底部。
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        getDialog().setCancelable(true);

        initConstant();
        SlConfig cfg = new SlConfig.Builder()
                .debug(true)
                .appName("test app")
                .picTempFile(null)
                .qq(QQ_APPID, QQ_SCOPE)
                .weiBo(WEIBO_APPID, WEIBO_REDIRECT_URL, WEIBO_SCOPE)
                .weiXin(WEIXIN_APPID, WEIXIN_SECRET)
                .build();
        ShareLoginSDK.init(getActivity().getApplication(), cfg);

        shareContent = new ShareContentWebPage(title, content, url, null, null);
        for(int i=0;i<shareDialogBeanArrayList.size();i++){
            shareDialogBeanArrayList.get(i).shareContent = shareContent;
        }
        recycler_view.showRecyclerView(GenerateDataUtils.generateDataBeanList(9, shareDialogBeanArrayList), Constants.viewModelPackage);
        return view;
    }


    protected void initConstant() {
        QQ_APPID = "xxxxxxxxxxxx";
        QQ_SCOPE = "xxxxxxxxxxxx";
        WEIBO_APPID = "xxxxxxxxxxxx";
        WEIBO_REDIRECT_URL = "xxxxxxxxxxxx";
        WEIXIN_APPID = "xxxxxxxxxxxx";
        WEIXIN_SECRET = "xxxxxxxxxxxx";
        WEIBO_SCOPE = "xxxxxxxxxxxx";
    }

}
