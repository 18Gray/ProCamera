package com.eighteengray.procamera.business;

import com.eighteengray.procamera.card.baserecycler.BaseDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutao on 2017/9/30.
 * 目前用于生成数据的类，后期会替换成JSON数据
 */

public class GenerateDataUtils
{
    public static <T> List<BaseDataBean<T>> generateDataBeanList(int viewModelType, List<T> dataBeanList){
        List<BaseDataBean<T>> list = new ArrayList<>();
        int size = dataBeanList.size();
        for(int i = 0; i < size; i++){
            BaseDataBean<T> baseDataBean = new BaseDataBean<>(viewModelType, dataBeanList.get(i));
            list.add(baseDataBean);
        }
        return list;
    }
}
