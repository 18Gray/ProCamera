package com.eighteengray.procamera.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import java.util.Map;



public class FixedWebView extends WebView
{

    // private WeakReference<Context> mWeakContext;

    public FixedWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // mWeakContext = new WeakReference<Context>(context);
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");
    }

    public FixedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // mWeakContext = new WeakReference<Context>(context);
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");
    }

    public FixedWebView(Context context) {
        super(context);
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");
        // mWeakContext = new WeakReference<Context>(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int temp_ScrollY = getScrollY();
            scrollTo(getScrollX(), getScrollY() + 1);
            scrollTo(getScrollX(), temp_ScrollY);

        }
        return super.onTouchEvent(event);
    }

    public void loadJavascript(String url) {
        if (url == null || url.length() == 0) {
            return;
        }
        try {
            super.loadUrl(url);
        } catch (Exception e) {
            //防止javascript莫名报出空指针
        }
    }

    /**
     * 页面需要注入登录态，统一调用{@link #loadUrlWithCookie(String, Map)}
     */
    @Deprecated
    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (url == null || url.length() == 0) {
            return;
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    //
    public void loadUrlWithCookie(final String url,
                                  final Map<String, String> additionalHttpHeaders) {
        post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // syncCookie(url);
                loadUrl(url, additionalHttpHeaders);
            }

        });
    }

    /**
     * 页面需要注入登录态，统一调用{@link #loadUrlWithCookie(String)}
     */
    @Deprecated
    @Override
    public void loadUrl(String url) {
        try {
            if (url == null || url.length() == 0) {
                return;
            }
            super.loadUrl(url);
        }catch (Exception e){
            //
        }
    }

    public void loadUrlWithCookie(final String url) {
        post(new Runnable() {
            @Override
            public void run() {
                if (url == null || url.length() == 0) {
                    return;
                }
                loadUrl(url);
            }

        });
    }

    @Override
    public void reload() {
        // TODO Auto-generated method stub
        String url = getUrl();
        if (url == null || url.length() == 0) {
            return;
        }
        loadUrl(url);
    }

    /**
     * 给浏览器注入cookie，该方法应该在bindJavaScript、设置setting之后，在webview.loadUrl之前调用
     *
     * @param url
     */
    // public void syncCookie(String url) {
    // Context context = mWeakContext.get();
    // if (context != null) {
    // CookieSyncManager.createInstance(context);
    // CookieManager cookieManager = CookieManager.getInstance();
    // Uri urls = Uri.parse(url);
    // cookieManager.removeAllCookie();
    // cookieManager.setAcceptCookie(true);
    // LoginUser mLoginUser = LoginManager.getLoginUser();
    // String cookie1 = null;
    // String cookie2 = null;
    // String cookie3 = null;
    // String domainStr = ";domain=" + urls.getHost();
    // if (mLoginUser != null) {
    // cookie1 = "loginType="
    // + String.valueOf(mLoginUser.getLoginType(context))
    // + domainStr;
    // cookieManager.setCookie(url, cookie1);
    // switch (mLoginUser.getLoginType(context)) {
    // case ReaderLoginHelper.LOGIN_QQ:
    // case ReaderLoginHelper.LOGIN_WX:
    // if (mLoginUser instanceof QQUserInfo) {
    // cookie2 = "usid="
    // + ((QQUserInfo) mLoginUser).getUsid(context)
    // + domainStr;
    // cookieManager.setCookie(url, cookie2);
    // } else if (mLoginUser instanceof WXUserInfo) {
    // cookie2 = "usid="
    // + ((WXUserInfo) mLoginUser)
    // .getAccessToken(context) + domainStr;
    // cookie3 = "uid="
    // + ((WXUserInfo) mLoginUser).getUid(context)
    // + domainStr;
    // cookieManager.setCookie(url, cookie2);
    // cookieManager.setCookie(url, cookie3);
    // }
    // break;
    // }
    // }
    // CookieSyncManager.getInstance().sync();
    // }
    // }

    private OnScrollChangedCallback mOnScrollChangedCallback;


    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(FixedWebView.this,l, t, oldl, oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedListener(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
//        此处不再对顶部,底部做监听,具体用到单独处理即可
//        public void onScrollToTop(WebView v,int left,int top,int oldleft,int oldtop);
//        public void onScrollToBotton(WebView v,int left,int top,int oldleft,int oldtop);

        public void onScroll(WebView v, int left, int top, int oldleft, int oldtop);
    }

}
