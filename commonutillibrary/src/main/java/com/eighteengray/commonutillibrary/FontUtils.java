package com.eighteengray.commonutillibrary;

import android.graphics.Paint;

/**
 * 字体相关的工具类
 */
public class FontUtils
{
    /**
     * 返回字符串的长度
     * @param paint
     * @param str
     * @return
     */
    public static float getFontlength(Paint paint, String str)
    {
        return paint.measureText(str);
    }


    /**
     * 返回指定笔的文字高度
     * @param paint
     * @return
     */
    public static float getFontHeight(Paint paint)
    {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }


    /**
     * 返回指定笔离文字顶部的基准距离
     * @param paint
     * @return
     */
    public static float getFontLeading(Paint paint)
    {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }
}
