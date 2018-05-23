package com.eighteengray.procamera.activity;


import android.os.Bundle;
import android.widget.Toast;

import com.eighteengray.procamera.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

/**
 * Created by Administrator on 2018/5/19.
 */

public class WebViewActivity extends BaseActivity
{
    BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        bridgeWebView = (BridgeWebView) findViewById(R.id.bridge_web_view);
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.loadUrl("file:///android_asset/TestJsBridge.html");
        registerHandler();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    // JS调用native，需要注册的方法
    private void registerHandler(){
        bridgeWebView.registerHandler("getUserInfo", new BridgeHandler()
        {
            @Override
            public void handler(String data, CallBackFunction function)
            {
                Toast.makeText(WebViewActivity.this, data, Toast.LENGTH_LONG).show();
                function.onCallBack("succ");
            }
        });
    }

}
