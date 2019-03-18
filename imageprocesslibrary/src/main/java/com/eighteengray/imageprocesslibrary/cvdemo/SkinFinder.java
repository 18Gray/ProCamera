package com.eighteengray.imageprocesslibrary.cvdemo;

/**
 * Created by zhigang on 2017/12/16.
 */

public interface SkinFinder {

    /**
     *  <p>find skin pixel in YCrCb color space<p/>
     * @param y 16~235
     * @param Cr 15~240
     * @param Cb 15~240
     * @return
     */
    public boolean yCrCbSkin(int y, int Cr, int Cb);
    /**
     * <p>find skin pixel in RGB color space <p/>
     * @param r 16~235
     * @param g 15~240
     * @param b 15~240
     * @return
     */
    public boolean rgbSkin(int r, int g, int b);
}
