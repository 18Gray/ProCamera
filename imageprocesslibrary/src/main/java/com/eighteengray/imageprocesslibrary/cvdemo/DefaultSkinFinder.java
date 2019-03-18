package com.eighteengray.imageprocesslibrary.cvdemo;

/**
 * Created by zhigang on 2017/12/16.
 */

public class DefaultSkinFinder implements SkinFinder {

    @Override
    public boolean yCrCbSkin(int y, int Cr, int Cb) {
        return (y > 80)&& (85 <Cb && Cb < 135) && (135 <Cr && Cr < 180);
    }

    @Override
    public boolean rgbSkin(int tr, int tg, int tb) {
        int max = Math.max(tr, Math.max(tg, tb));
        int min = Math.min(tr, Math.min(tg, tb));
        int rg = Math.abs(tr - tg);

        return tr > 95 && tg > 40 && tb > 20 && rg > 15 &&
                (max - min) > 15 && tr > tg && tr > tb;
    }

}
