package com.eighteengray.procamera.imageprocess.bean;


import com.eighteengray.imageprocesslibrary.bitmapfilter.IBitmapFilter;


public class FilterInfo {
    public int resourceId;
    public IBitmapFilter filter;
    public String filterName;

    public FilterInfo(int r, IBitmapFilter filter, String n) {
        this.resourceId = r;
        this.filter = filter;
        this.filterName = n;
    }
}
