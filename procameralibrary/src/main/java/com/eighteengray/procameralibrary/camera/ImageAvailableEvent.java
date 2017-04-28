package com.eighteengray.procameralibrary.camera;


import android.media.ImageReader;

public class ImageAvailableEvent
{
    public static class ImageReaderAvailable
    {
        private ImageReader imageReader;

        public ImageReader getImageReader()
        {
            return imageReader;
        }

        public void setImageReader(ImageReader imageReader)
        {
            this.imageReader = imageReader;
        }
    }


    public static class ImagePathAvailable
    {
        private String imagePath;

        public String getImagePath()
        {
            return imagePath;
        }

        public void setImagePath(String imagePath)
        {
            this.imagePath = imagePath;
        }
    }


}
