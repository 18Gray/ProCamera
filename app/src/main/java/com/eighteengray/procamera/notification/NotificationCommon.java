package com.eighteengray.procamera.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.eighteengray.procamera.MainActivity;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.activity.MineActivity;


public class NotificationCommon
{

    public void sendNotification(Context context){
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher; // 小图标
        notification.largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher); // 大图标
        notification.defaults = Notification.DEFAULT_ALL; // 设置默认的提示音、振动方式、灯光等
        notification.category = "Category";
        notification.when = System.currentTimeMillis(); // 设置通知发送的时间戳
        notification.tickerText = "Ticker Text"; // 设置通知首次弹出时，状态栏上显示的文本
        notification.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知后通知在通知栏上消失
        notification.contentIntent = PendingIntent.getActivity(context, 0x001,
                new Intent(context, MineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); // 设置通知的点击事件
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1,notification); // 发送系统通知
    }

    public void sendNotificationBuilder(Context context){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0x001,
                new Intent(context, MineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)) // 大图标
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setContentText("Content Text") // 内容
                .setSubText("Sub Text") // 在通知中，APP名称的副标题
                .setContentTitle("Content Title") // 标题
                .setTicker("Ticker") // 设置通知首次弹出时，状态栏上显示的文本
                .setWhen(System.currentTimeMillis()) // 设置通知发送的时间戳
                .setAutoCancel(true) // 点击通知后通知在通知栏上消失
                .setDefaults(Notification.DEFAULT_ALL) // 设置默认的提示音、振动方式、灯光等
                .setContentIntent(pendingIntent); // 设置通知的点击事件
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build()); // build()方法需要的最低API为16
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendNotificationRemoteviews(Context context){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0x001, new Intent(context, MineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.item_text_grid);
        remoteView.setTextViewText(R.id.tv_view_model5, "Title");
        remoteView.setTextViewText(R.id.tv_view_model6, "ContentContentContent");
        remoteView.setImageViewResource(R.id.iv_album_camera, R.mipmap.ic_launcher);
        remoteView.setOnClickPendingIntent(R.id.iv_arraw_awb, pendingIntent);
        Notification.Builder nb = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setCustomContentView(remoteView) // 设置自定义的RemoteView，需要API最低为24
                .setWhen(System.currentTimeMillis()) // 设置通知发送的时间戳
                .setAutoCancel(true) // 点击通知后通知在通知栏上消失
                .setDefaults(Notification.DEFAULT_ALL); // 设置默认的提示音、振动方式、灯光等
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, nb.build()); // build()方法需要的最低API为16

    }

}
