package com.eighteengray.procamera.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import com.eighteengray.commonutillibrary.DataConvertUtil;
import com.eighteengray.commonutillibrary.ImageProcessUtils;
import com.eighteengray.commonutillibrary.ImageUtils;
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
    private Context context;


    public ImageSaver(ImageReader mImageReader, Context c)
    {
        this.mImageReader = mImageReader;
        this.context = c;
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
            Bitmap bitmap = DataConvertUtil.bytes2Bimap(bytes);
            String imagePath = ImageUtils.saveBitmap2Album(context, bitmap);
            image.close();

            ImageAvailableEvent.ImagePathAvailable imagePathAvailable = new ImageAvailableEvent.ImagePathAvailable();
            imagePathAvailable.setImagePath(imagePath);
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
