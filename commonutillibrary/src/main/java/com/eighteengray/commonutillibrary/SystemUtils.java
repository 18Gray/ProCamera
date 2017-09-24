package com.eighteengray.commonutillibrary;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


/**
 * 系统信息相关工具类
 */
public class SystemUtils
{
    //应用相关
    /**
     * 获取当前应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当前应用程序的版本名称
     */
    public static String getAppVersion(Context context)
    {
        String version = "0";
        try
        {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e)
        {
            throw new RuntimeException(SystemUtils.class.getName() + "the application not found");
        }
        return version;
    }


    /**
     * 获取当前应用程序的版本号
     */
    public static int getAppVersionCode(Context context)
    {
        int version = 0;
        try
        {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e)
        {
            throw new RuntimeException(SystemUtils.class.getName() + "the application not found");
        }
        return version;
    }


    /**
     * 判断当前应用程序是否在后台运行
     */
    public static boolean isBackground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses)
        {
            if (appProcess.processName.equals(context.getPackageName()))
            {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
                {
                    // 后台运行
                    return true;
                } else
                {
                    // 前台运行
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 获取应用程序的签名
     * @param context
     * @param pkgName
     */
    public static String getSign(Context context, String pkgName)
    {
        try
        {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (NameNotFoundException e)
        {
            throw new RuntimeException(SystemUtils.class.getName() + "the "
                    + pkgName + "'s application not found");
        }
    }


    /**
     * 将签名字符串转换成需要的32位签名
     * @param paramArrayOfByte
     * @return
     */
    private static String hexdigest(byte[] paramArrayOfByte)
    {
        final char[] hexDigits =
                {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++)
            {
                if (i >= 16)
                {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e)
        {
        }
        return "";
    }




    //手机系统相关

    /**
     * 获取手机IMEI码
     * @param cxt
     * @return
     */
    public static String getPhoneIMEI(Context cxt)
    {
        TelephonyManager tm = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    /**
     * 获取手机系统SDK版本
     * @return
     */
    public static int getSDKVersion()
    {
        return android.os.Build.VERSION.SDK_INT;
    }


    /**
     * 获取手机系统内核版本
     * @return
     */
    public static String getKernelVersion()
    {
        String kernelVersion = "";
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                info += line;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            if (info != "")
            {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        return kernelVersion;
    }


    /**
     * 判断手机是否处于睡眠状态
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context)
    {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }


    /**
     * 获取手机的可用内存大小
     * @param cxt 应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context cxt)
    {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        return (int) (mi.availMem / (1024 * 1024));
    }


    /**
     * 清理后台进程与服务
     * @param cxt 应用上下文对象context
     * @return 被清理的数量
     */
    public static int gc(Context cxt)
    {
        long i = getDeviceUsableMemory(cxt);
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null)
            for (RunningServiceInfo service : serviceList)
            {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try
                {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e)
                {
                    e.getStackTrace();
                    continue;
                }
            }

        // 获取正在运行的进程列表
        List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null)
            for (RunningAppProcessInfo process : processList)
            {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE)
                {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList)
                    {
                        try
                        {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e)
                        { // 防止意外发生
                            e.getStackTrace();
                            continue;
                        }
                    }
                }
            }
        return count;
    }





    //系统Intent

    /**
     * 在桌面创建快捷方式
     * @param cxt
     * @param icon
     * @param title
     * @param cls
     */
    public void createDeskShortCut(Context cxt, int icon, String title, Class<?> cls)
    {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 快捷图片
        Parcelable ico = Intent.ShortcutIconResource.fromContext(cxt.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
        Intent intent = new Intent(cxt, cls);
        // 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        cxt.sendBroadcast(shortcutIntent);
    }


    /**
     * 安装apk
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 回到home，后台运行
     */
    public static void goHome(Context context)
    {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }



    /**
     * 调用系统发送短信，跳转发短信界面
     */
    public static void sendSMS(Context cxt, String smsBody)
    {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }


    /**
     * 调用系统短信接口，后台发送短信
     * @param destinationAddress
     * @param text
     * @param sentIntent
     * @param deliveryIntent
     */
    public static void sendSMSBackground(String destinationAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, null, text, sentIntent, deliveryIntent);
    }


    /**
     * 调用系统群发短信
     * @param context
     * @param mobileList
     * @param content
     */
    public static void sendGroupSMS(Context context, ArrayList<String> mobileList, String content)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mobileList.size(); i++)
        {
            stringBuilder.append(mobileList.get(i));
            stringBuilder.append(";");
        }
        if (stringBuilder.length() > 0)
        {
            stringBuilder.deleteCharAt(mobileList.size() - 1);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", stringBuilder.toString());
        intent.putExtra("sms_body", content);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);

    }


    /**
     * 调用系统邮件，跳转发送邮件界面
     * @param context
     * @param emails
     */
    public static void sendEmail(Context context, String[] emails)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822"); // 设置邮件格式
        intent.putExtra(Intent.EXTRA_EMAIL, emails); // 接收人
        intent.putExtra(Intent.EXTRA_CC, emails); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }


    /**
     * android获取一个用于打开HTML文件的intent
     * @param param
     * @return
     */
    public static Intent getHtmlFileIntent(String param)
    {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }


    /**
     * android获取一个用于打开PDF文件的intent
     * @param param
     * @return
     */
    public static Intent getPdfFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


    /**
     * android获取一个用于打开文本文件的intent
     * @param param
     * @param paramBoolean
     * @return
     */
    public static Intent getTextFileIntent(String param, boolean paramBoolean)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean)
        {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else
        {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }


    /**
     * android获取一个用于打开音频文件的intent
     * @param param
     * @return
     */
    public static Intent getAudioFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }


    /**
     * android获取一个用于打开视频文件的intent
     * @param param
     * @return
     */
    public static Intent getVideoFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }


    /**
     * android获取一个用于打开CHM文件的intent
     * @param param
     * @return
     */
    public static Intent getChmFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }


    /**
     * android获取一个用于打开Word文件的intent
     * @param param
     * @return
     */
    public static Intent getWordFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }


    /**
     * android获取一个用于打开Excel文件的intent
     * @param param
     * @return
     */
    public static Intent getExcelFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }


    /**
     * android获取一个用于打开PPT文件的intent
     * @param param
     * @return
     */
    public static Intent getPptFileIntent(String param)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

}