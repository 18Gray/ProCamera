package com.eighteengray.procamera.webview;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.eighteengray.basecomponent.baseactivity.BaseActivity;
import com.eighteengray.procamera.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;



/**
 * Created by Administrator on 2018/5/19.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener
{
    BridgeWebView bridgeWebView;
    WebSettings webSettings;
    WebViewClient webViewClient;
    WebChromeClient webChromeClient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        bridgeWebView = (BridgeWebView) findViewById(R.id.bridge_web_view);
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.loadUrl("file:///android_asset/demo.html");
        registerHandler();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onResume(){
        super.onResume();
        bridgeWebView.onResume();

        webSettings = bridgeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


    }

    @Override
    protected void onPause()
    {
        bridgeWebView.onPause();
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
        bridgeWebView.destroy();
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

    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.btn_back:
                if(bridgeWebView.canGoBack()){
                    bridgeWebView.goBack();
                }
                break;
            case R.id.btn_right:
                if(bridgeWebView.canGoForward()){
                    bridgeWebView.goForward();
                }
                break;
            case R.id.btn_search:
                bridgeWebView.goBackOrForward(2);
                break;
        }
    }
}
