package com.eighteengray.procamera.card.baserecycler;




public class BaseDataBean<T> {

    public int viewModelNum;
    public T data;

    public BaseDataBean(int vmn, T d)
    {
        this.viewModelNum = vmn;
        this.data = d;
    }

}
