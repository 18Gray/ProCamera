package com.eighteengray.procamera.bean;


import com.eighteengray.imageprocesslibrary.java.bitmapfilter.IBitmapFilter;

public class FilterInfo
{
    public int resourceId;
    public IBitmapFilter filter;
    public String filterName;

    public FilterInfo(int r, IBitmapFilter filter, String n)
    {
        this.resourceId = r;
        this.filter = filter;
        this.filterName = n;
    }
}
