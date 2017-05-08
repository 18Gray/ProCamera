package com.eighteengray.procameralibrary.dataevent;




public class BitmapProcess
{

    public static class ContrastEvent
    {
        private int seekBarNum;
        private int progress;

        public int getSeekBarNum()
        {
            return seekBarNum;
        }

        public void setSeekBarNum(int seekBarNum)
        {
            this.seekBarNum = seekBarNum;
        }

        public int getProgress()
        {
            return progress;
        }

        public void setProgress(int progress)
        {
            this.progress = progress;
        }
    }

}
