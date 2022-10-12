package com.eighteengray.cardlibrary.bean;


public class BaseDataBean<T> {

    public int viewModelNum;  // card类型
    public T data;  // card的数据

    public BaseDataBean(int vmn, T d) {
        this.viewModelNum = vmn;
        this.data = d;
    }

}
