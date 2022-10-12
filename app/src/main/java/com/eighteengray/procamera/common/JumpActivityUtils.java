package com.eighteengray.procamera.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eighteengray.procamera.album.AlbumActivity;
import com.eighteengray.procamera.settings.SettingsActivity;
import com.eighteengray.procamera.webview.WebViewActivity;
import com.eighteengray.procameralibrary.common.Constants;



public class JumpActivityUtils {

    public static void jump2AlbumActivity(Context context, boolean isRadio, boolean isTakeCamera, boolean isShowAdd){
        Intent intent = new Intent(context, AlbumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_RADIO, isRadio);
        bundle.putBoolean(Constants.IS_TAKE_CAMERA, isTakeCamera);
        bundle.putBoolean(Constants.IS_SHOW_ADD, isShowAdd);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public static void jump2SettingActivity(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void jump2CircleEditActivity(){

    }

    public static void jump2CutActivity(){

    }

    public static void jump2ImageProcessActivity(Context context, String imagePath){
//        Intent intent = new Intent(context, ImageProcessActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.IMAGE_PATH, imagePath);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
    }

    public static void jump2MineActivity(){

    }

    public static void jump2WebViewActivity(Context context){
        Intent intent = new Intent(context, WebViewActivity.class);
        context.startActivity(intent);
    }

}
