package com.eighteengray.procamera.DataEvent;

/**
 * Created by lutao on 2017/4/12.
 */

public class TextureViewTouchEvent
{

    public static class TextureClick
    {
        private float x, y;

        public float getX()
        {
            return x;
        }

        public void setX(float x)
        {
            this.x = x;
        }

        public float getY()
        {
            return y;
        }

        public void setY(float y)
        {
            this.y = y;
        }
    }


    public static class TextureLongClick
    {
        private float x, y;

        public float getX()
        {
            return x;
        }

        public void setX(float x)
        {
            this.x = x;
        }

        public float getY()
        {
            return y;
        }

        public void setY(float y)
        {
            this.y = y;
        }
    }


    public static class TextureOneDrag
    {
        private float distance;

        public float getDistance()
        {
            return distance;
        }

        public void setDistance(float distance)
        {
            this.distance = distance;
        }
    }


    public static class TextureTwoDrag
    {
        private float scale;

        public float getScale()
        {
            return scale;
        }

        public void setScale(float scale)
        {
            this.scale = scale;
        }
    }


}
