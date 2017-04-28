package com.eighteengray.procamera.common;

import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import com.eighteengray.procameralibrary.dataevent.ImageAvailableEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;


public class ImageSaver implements Runnable
{
    private ImageReader mImageReader;
    private File file;


    public ImageSaver(ImageReader mImageReader, File f)
    {
        this.mImageReader = mImageReader;
        this.file = f;
    }

    @Override
    public void run()
    {
        Image image = mImageReader.acquireNextImage();
        try
        {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            try
            {
                save(bytes, file);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            image.close();

            ImageAvailableEvent.ImagePathAvailable imagePathAvailable = new ImageAvailableEvent.ImagePathAvailable();
            imagePathAvailable.setImagePath(file.getAbsolutePath());
            EventBus.getDefault().post(imagePathAvailable);
        } catch (Exception e)
        {
            e.getStackTrace();
        }
    }



    /**
     * 保存
     * @param bytes
     * @param file
     * @throws IOException
     */
    private void save(byte[] bytes, File file) throws IOException
    {
        Log.i("JpegSaver", "save");
        OutputStream os = null;
        try
        {
            os = new FileOutputStream(file);
            os.write(bytes);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

}
