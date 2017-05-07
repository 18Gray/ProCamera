package com.eighteengray.procamera.bean;



import com.eighteengray.imageprocesslibrary.bitmapfilter.BaseBitmapFilter;



public class FilterInfo
{
    public int resourceId;
    public BaseBitmapFilter filter;
    public String filterName;

    public FilterInfo(int r, BaseBitmapFilter filter, String n)
    {
        this.resourceId = r;
        this.filter = filter;
        this.filterName = n;
    }
}
