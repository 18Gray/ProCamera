package com.eighteengray.procamera.business;


import android.content.Context;
import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.procamera.bean.Settings;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
