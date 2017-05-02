package com.eighteengray.procameralibrary.dataevent;


public class CameraConfigure
{
    public static class Flash
    {
        private int flash;
        public int getFlash()
        {
            return flash;
        }
        public void setFlash(int flash)
        {
            this.flash = flash;
        }
    }

    public static class Hdr
    {
        private int hdr;
        public int getHdr()
        {
            return hdr;
        }
        public void setHdr(int hdr)
        {
            this.hdr = hdr;
        }
    }

    public static class GpuFilter
    {
        private int gpufilter;
        public int getGpufilter()
        {
            return gpufilter;
        }
        public void setGpufilter(int gpufilter)
        {
            this.gpufilter = gpufilter;
        }
    }

    public static class Ratio
    {
        private int ratio;
        public int getRatio()
        {
            return ratio;
        }
        public void setRatio(int ratio)
        {
            this.ratio = ratio;
        }
    }

    public static class DelayTime
    {
        private int delaytime;
        public int getDelaytime()
        {
            return delaytime;
        }
        public void setDelaytime(int delaytime)
        {
            this.delaytime = delaytime;
        }
    }
}
