package com.eighteengray.procamera.business;


import android.content.Context;

import com.eighteengray.cardlibrary.bean.BaseDataBean;
import com.eighteengray.commonutils.SharePreferenceUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.Settings;
import com.eighteengray.procamera.imageprocess.bean.HorizontalRecyclerItem;
import com.eighteengray.procamera.imageprocess.bean.VerticalRecyclerItem;
import com.eighteengray.procameralibrary.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutao on 2017/9/30.
 * 目前用于生成数据的类，后期会替换成JSON数据
 */

public class GenerateDataUtils
{
    // 通用生成最终带view type的数据列表
    public static <T> List<BaseDataBean<T>> generateDataBeanList(int viewModelType, List<T> dataBeanList){
        List<BaseDataBean<T>> list = new ArrayList<>();
        int size = dataBeanList.size();
        for(int i = 0; i < size; i++){
            BaseDataBean<T> baseDataBean = new BaseDataBean<>(viewModelType, dataBeanList.get(i));
            list.add(baseDataBean);
        }
        return list;
    }


    // 生成设置相关数据列表
    public static List<BaseDataBean<Settings>> generateSettingsList(Context context){
        List<BaseDataBean<Settings>> settingsDataList = new ArrayList<>();
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_QUALITY, "1920 * 1080", "图片质量", R.mipmap.image_black_24dp, 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_FORMAT, "jpg", "图片格式", R.mipmap.format_black_24dp,5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_DATE, "2018-5-25 14:00", "签名日期", R.mipmap.history_black_24dp, 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_LOCATION, "北京市海淀区中关村", "位置", R.mipmap.location_black_24dp, 7));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_NAME, "EighteenGray", "签名版权", R.mipmap.copyright_black_24dp,7));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_TEXT_SIZE, "20", "签名字体大小", R.mipmap.text_size_24dp, 5));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN_TEXT_COLOR, "#FFFFFF", "签名字体颜色", R.mipmap.palette_black_24dp, 7));

        settingsDataList.add(createDataSettings(context, Constants.IMAGE_GRID, "1", "九宫格", R.mipmap.grid_black_24dp, 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_BALANCE, "1", "矫衡器", R.mipmap.balance_black_24dp, 6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_ANTI_SHAKE, "1", "防手抖", R.mipmap.antishake_black_24dp,6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_MUTE, "1", "拍摄静音", R.mipmap.mute_black_24dp,6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_HISTOGRAM, "1", "实时直方图", R.mipmap.histogram_black_24dp,6));
        settingsDataList.add(createDataSettings(context, Constants.IMAGE_SIGN, "1", "开启签名", R.mipmap.sign_black_24dp, 6));
        return settingsDataList;
    }

    private static BaseDataBean<Settings> createDataSettings(Context context, String key, String defaultValut, String funcName, int resourceId, int viewModelType){
        Settings settings = new Settings();
        settings.funcName = funcName;
        settings.value = SharePreferenceUtils.getInstance(context, Constants.SETTINGS).getString(key, defaultValut);
        settings.resourceId = resourceId;
        BaseDataBean<Settings> baseDataBean = new BaseDataBean<>(viewModelType, settings);
        return baseDataBean;
    }


    public static List<HorizontalRecyclerItem> generateRedoMenuList(){
        List<HorizontalRecyclerItem> horizontalRecyclerItemList = new ArrayList<>();
        HorizontalRecyclerItem horizontalRecyclerItem1 = new HorizontalRecyclerItem();
        horizontalRecyclerItem1.resourceId = R.mipmap.redo_menu_white_24dp;
        horizontalRecyclerItem1.title = "取消";
        horizontalRecyclerItem1.content = "取消操作";
        horizontalRecyclerItemList.add(horizontalRecyclerItem1);

        HorizontalRecyclerItem horizontalRecyclerItem2 = new HorizontalRecyclerItem();
        horizontalRecyclerItem2.resourceId = R.mipmap.redo_menu_white_24dp;
        horizontalRecyclerItem2.title = "取消";
        horizontalRecyclerItem2.content = "取消操作";
        horizontalRecyclerItemList.add(horizontalRecyclerItem2);

        HorizontalRecyclerItem horizontalRecyclerItem3 = new HorizontalRecyclerItem();
        horizontalRecyclerItem3.resourceId = R.mipmap.redo_menu_white_24dp;
        horizontalRecyclerItem3.title = "取消";
        horizontalRecyclerItem3.content = "取消操作";
        horizontalRecyclerItemList.add(horizontalRecyclerItem3);
        return horizontalRecyclerItemList;
    }

    public static List<HorizontalRecyclerItem> generateOutputMenuList(){
        List<HorizontalRecyclerItem> horizontalRecyclerItemList = new ArrayList<>();
        HorizontalRecyclerItem horizontalRecyclerItem1 = new HorizontalRecyclerItem();
        horizontalRecyclerItem1.resourceId = R.mipmap.redo_menu_white_24dp;
        horizontalRecyclerItem1.title = "取消";
        horizontalRecyclerItem1.content = "取消操作";
        horizontalRecyclerItemList.add(horizontalRecyclerItem1);

        HorizontalRecyclerItem horizontalRecyclerItem2 = new HorizontalRecyclerItem();
        horizontalRecyclerItem2.resourceId = R.mipmap.redo_menu_white_24dp;
        horizontalRecyclerItem2.title = "取消";
        horizontalRecyclerItem2.content = "取消操作";
        horizontalRecyclerItemList.add(horizontalRecyclerItem2);

        HorizontalRecyclerItem horizontalRecyclerItem3 = new HorizontalRecyclerItem();
        horizontalRecyclerItem3.resourceId = R.mipmap.redo_menu_white_24dp;
        horizontalRecyclerItem3.title = "取消";
        horizontalRecyclerItem3.content = "取消操作";
        horizontalRecyclerItemList.add(horizontalRecyclerItem3);
        return horizontalRecyclerItemList;
    }


    public static List<VerticalRecyclerItem> generateFilterMenuList(){
        List<VerticalRecyclerItem> filterMenuList = new ArrayList<>();

        VerticalRecyclerItem imageFilterMenuItem1 = new VerticalRecyclerItem();
        imageFilterMenuItem1.resourceId = R.mipmap.filter_baohe;
        imageFilterMenuItem1.title = "饱和度";
        filterMenuList.add(imageFilterMenuItem1);

        VerticalRecyclerItem imageFilterMenuItem2 = new VerticalRecyclerItem();
        imageFilterMenuItem2.resourceId = R.mipmap.filter_blackwhite;
        imageFilterMenuItem2.title = "白平衡";
        filterMenuList.add(imageFilterMenuItem2);

        VerticalRecyclerItem imageFilterMenuItem3 = new VerticalRecyclerItem();
        imageFilterMenuItem3.resourceId = R.mipmap.filter_blue;
        imageFilterMenuItem3.title = "蓝调";
        filterMenuList.add(imageFilterMenuItem3);

        VerticalRecyclerItem imageFilterMenuItem4 = new VerticalRecyclerItem();
        imageFilterMenuItem4.resourceId = R.mipmap.filter_feather;
        imageFilterMenuItem4.title = "锐化";
        filterMenuList.add(imageFilterMenuItem4);

        VerticalRecyclerItem imageFilterMenuItem5 = new VerticalRecyclerItem();
        imageFilterMenuItem5.resourceId = R.mipmap.filter_film;
        imageFilterMenuItem5.title = "电影";
        filterMenuList.add(imageFilterMenuItem5);

        VerticalRecyclerItem imageFilterMenuItem6 = new VerticalRecyclerItem();
        imageFilterMenuItem6.resourceId = R.mipmap.filter_lomo;
        imageFilterMenuItem6.title = "罗摩";
        filterMenuList.add(imageFilterMenuItem6);

        return filterMenuList;
    }

    public static List<VerticalRecyclerItem> generateProcessToolsMenuList(){
        List<VerticalRecyclerItem> filterMenuList = new ArrayList<>();

        VerticalRecyclerItem imageFilterMenuItem1 = new VerticalRecyclerItem();
        imageFilterMenuItem1.resourceId = R.mipmap.filter_baohe;
        imageFilterMenuItem1.title = "饱和度";
        filterMenuList.add(imageFilterMenuItem1);

        VerticalRecyclerItem imageFilterMenuItem2 = new VerticalRecyclerItem();
        imageFilterMenuItem2.resourceId = R.mipmap.filter_blackwhite;
        imageFilterMenuItem2.title = "白平衡";
        filterMenuList.add(imageFilterMenuItem2);

        VerticalRecyclerItem imageFilterMenuItem3 = new VerticalRecyclerItem();
        imageFilterMenuItem3.resourceId = R.mipmap.filter_blue;
        imageFilterMenuItem3.title = "蓝调";
        filterMenuList.add(imageFilterMenuItem3);

        VerticalRecyclerItem imageFilterMenuItem4 = new VerticalRecyclerItem();
        imageFilterMenuItem4.resourceId = R.mipmap.filter_feather;
        imageFilterMenuItem4.title = "锐化";
        filterMenuList.add(imageFilterMenuItem4);

        VerticalRecyclerItem imageFilterMenuItem5 = new VerticalRecyclerItem();
        imageFilterMenuItem5.resourceId = R.mipmap.filter_film;
        imageFilterMenuItem5.title = "电影";
        filterMenuList.add(imageFilterMenuItem5);

        VerticalRecyclerItem imageFilterMenuItem6 = new VerticalRecyclerItem();
        imageFilterMenuItem6.resourceId = R.mipmap.filter_lomo;
        imageFilterMenuItem6.title = "罗摩";
        filterMenuList.add(imageFilterMenuItem6);

        return filterMenuList;
    }


}
