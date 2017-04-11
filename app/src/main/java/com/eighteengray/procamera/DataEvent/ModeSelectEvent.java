package com.eighteengray.procamera.DataEvent;

import static android.R.attr.mode;

/**
 * Created by lutao on 2017/4/11.
 */

public class ModeSelectEvent
{
    public static final int MODE_CAMERA = 10001;
    public static final int MODE_RECORD = 10002;

    private int mode;

    public int getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

}
