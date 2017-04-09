package com.eighteengray.procameralibrary.camera;

import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by yuyidong on 15-1-9.
 */
public class ImageSaver implements Runnable
{

    private ImageReader mImageReader;

    public ImageSaver(ImageReader mImageReader)
    {
        this.mImageReader = mImageReader;
    }

    @Override
    public void run()
    {
        Image image = mImageReader.acquireLatestImage();
        checkParentDir();
        File file;
        checkJpegDir();
        file = createJpeg();
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
        } catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    /**
     * 判断父文件是否存在
     */
    private void checkParentDir()
    {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/");
        if (!dir.exists())
        {
            dir.mkdir();
        }
    }

    /**
     * 判断文件夹是否存在
     */
    private void checkJpegDir()
    {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/jpeg/");
        if (!dir.exists())
        {
            dir.mkdir();
        }
    }


    /**
     * 创建jpeg的文件
     *
     * @return
     */
    private File createJpeg()
    {
        long time = System.currentTimeMillis();
        int random = new Random().nextInt(1000);
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/jpeg/");
        Log.i("JpegSaver", time + "_" + random + ".jpg");
        return new File(dir, time + "_" + random + ".jpg");
    }


    /**
     * 保存
     *
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
