package com.eighteengray.procamera.business;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.commonutillibrary.SharePreferenceUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procamera.model.SystemModel;
import com.eighteengray.procameralibrary.common.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 相册相关的业务类，主要作用是处理获取到的Cursor，封装成ImageFolder，然后返回其对应的Observerable
 */
public class SettingsBusiness
{

    //获取Settings的列表
    public static Observable<List<BaseDataBean<Settings>>> getSettingsDataList(Context context)
    {
        List<BaseDataBean<Settings>> settingsDataList = new ArrayList<>();
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_QUALITY, "1920 * 1080", "图片质量", 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_FORMAT, "jpg", "图片格式", 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_DATE, "2018-5-25 14:00", "签名日期", 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_LOCATION, "北京市海淀区中关村", "位置", 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_NAME, "EighteenGray", "签名版权", 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_TEXT_SIZE, "20", "签名字体大小", 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_TEXT_COLOR, "#FFFFFF", "签名字体颜色", 5));

        settingsDataList.add(createDataSettings(context, Constants.IMAGE_GRID, "1", "九宫格", 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_BALANCE, "1", "矫衡器", 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_ANTI_SHAKE, "1", "防手抖", 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_MUTE, "1", "拍摄静音", 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_HISTOGRAM, "1", "实时直方图", 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN, "1", "开启签名", 6));
        return Observable.just(settingsDataList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static BaseDataBean<Settings> createDataSettings(Context context, String key, String defaultValut, String funcName, int viewModelType){
        Settings settings = new Settings();
        settings.funcName = funcName;
        settings.value = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(key, defaultValut);

        BaseDataBean<Settings> baseDataBean = new BaseDataBean<>(viewModelType, settings);
        return baseDataBean;
    }

}
