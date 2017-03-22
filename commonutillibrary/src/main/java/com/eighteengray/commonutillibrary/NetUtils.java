package com.eighteengray.commonutillibrary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.util.ArrayList;
import java.util.List;


/**
 * 网络相关工具类
 */
public class NetUtils
{
    Context context;
    ConnectivityManager connectivityManager;
    WifiManager wifiManager;
    WifiManager.WifiLock wifiLock;

    // 定义WifiInfo对象
    private WifiInfo wifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList = new ArrayList<>();
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration = new ArrayList<>();


    public NetUtils(Context c)
    {
        this.context = c;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }


    private void getWifiLock(Context context)
    {
        wifiLock = wifiManager.createWifiLock("wifi");
    }


    private void getWifiInfo(Context context)
    {
        wifiInfo = wifiManager.getConnectionInfo();
    }


    /**
     * 打开网络设置界面
     */
    public void openSetting(Activity activity)
    {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }


    /**
     * 扫描所有可用网络
     * @param context
     */
    public void startScan(Context context)
    {
        wifiManager.startScan();
        // 得到扫描结果
        mWifiList = wifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = wifiManager.getConfiguredNetworks();
    }


    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public boolean isConnected(Context context)
    {
        if (connectivityManager != null)
        {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (null != info && info.isConnected())
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断是否是wifi连接
     * @param context
     * @return
     */
    public boolean isWifi(Context context)
    {
        if (connectivityManager == null)
        {
            return false;
        }
        return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 检查当前WIFI状态
     * @param context
     * @return
     */
    public int checkState(Context context)
    {
        return wifiManager.getWifiState();
    }


    /**
     * 打开WIFI
      */
    public void openWifi(Context context)
    {
        if (!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
    }


    /**
     * 关闭WIFI
     * @param context
     */
    public void closeWifi(Context context)
    {
        if (wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
    }


    /**
     * 锁定WifiLock
     * @param context
     */
    public void acquireWifiLock(Context context)
    {
        getWifiLock(context);
        wifiLock.acquire();
    }


    /**
     * 解锁WifiLock
     * @param context
     */
    public void releaseWifiLock(Context context)
    {
        getWifiLock(context);
        if (wifiLock.isHeld())
        {
            wifiLock.acquire();
        }
    }


    /**
     * 按照指定配置进行连接
     * @param index
     * @param context
     */
    public void connectConfiguration(int index, Context context)
    {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size())
        {
            return;
        }
        // 连接配置好的指定ID的网络
        wifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }


    /**
     * 添加一个网络并连接
     * @param wcg
     * @param context
     */
    public boolean addNetwork(WifiConfiguration wcg, Context context)
    {
        int wcgID = wifiManager.addNetwork(wcg);
        boolean b = wifiManager.enableNetwork(wcgID, true);
        return b;
    }


    /**
     * 断开指定ID的网络
     * @param netId
     * @param context
     */
    public void disconnectWifi(int netId, Context context)
    {
        wifiManager.disableNetwork(netId);
        wifiManager.disconnect();
    }


    /**
     * 得到MAC地址
     * @param context
     * @return
     */
    public String getMacAddress(Context context)
    {
        getWifiInfo(context);
        return (wifiInfo == null) ? "NULL" : wifiInfo.getMacAddress();
    }


    /**
     * 得到接入点的BSSID
     * @param context
     * @return
     */
    public String getBSSID(Context context)
    {
        getWifiInfo(context);
        return (wifiInfo == null) ? "NULL" : wifiInfo.getBSSID();
    }


    /**
     * 得到IP地址
     * @param context
     * @return
     */
    public int getIPAddress(Context context)
    {
        getWifiInfo(context);
        return (wifiInfo == null) ? 0 : wifiInfo.getIpAddress();
    }

    /**
     * 得到连接的ID
     * @param context
     * @return
     */
    public int getNetworkId(Context context)
    {
        getWifiInfo(context);
        return (wifiInfo == null) ? 0 : wifiInfo.getNetworkId();
    }


}
