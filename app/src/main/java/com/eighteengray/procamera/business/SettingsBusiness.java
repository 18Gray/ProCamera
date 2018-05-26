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
        List<BaseDataBean<Settings>> settingsDataList = GenerateDataUtils.generateSettingsList(context);
        return Observable.just(settingsDataList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }



}
