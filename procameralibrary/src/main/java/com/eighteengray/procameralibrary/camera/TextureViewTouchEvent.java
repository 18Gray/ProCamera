package com.eighteengray.procameralibrary.camera;

/**
 * Created by lutao on 2017/4/12.
 */

public class TextureViewTouchEvent
{

    public static class TextureClick
    {
        private float x, y;
        private float rawX, rawY;

        public float getRawX()
        {
            return rawX;
        }

        public void setRawX(float rawX)
        {
            this.rawX = rawX;
        }

        public float getRawY()
        {
            return rawY;
        }

        public void setRawY(float rawY)
        {
            this.rawY = rawY;
        }



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


    public static class FocusState
    {
        private int focusState;

        public int getFocusState()
        {
            return focusState;
        }

        public void setFocusState(int focusState)
        {
            this.focusState = focusState;
        }
    }


}
