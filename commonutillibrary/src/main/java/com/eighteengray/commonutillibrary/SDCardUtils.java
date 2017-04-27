package com.eighteengray.commonutillibrary;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;



/**
 * SDCard相关工具类
 */
public class SDCardUtils
{
    //SDCard
    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardEnable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }


    /**
     * 获取SD卡的剩余容量 单位byte
     */
    public static long getFreeSizeAll()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }


    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     */
    public static long getFreeSizeByPath(String filePath)
    {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath()))
        {
            filePath = getSDCardPath();
        } else
        {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }


    /**
     * 调用该方法会返回应用程序的外部文件系统（Environment.getExternalStorageDirectory()）目录的绝对路径，它是用来存放应用的缓存文件，
     * 它和getCacheDir目录一样，目录下的文件都会在程序被卸载的时候被清除掉。
     */
    public static File getSDCardCache(Context context)
    {
        if (isSDCardEnable())
        {
            return context.getExternalCacheDir();
        }
        return null;
    }


    //系统存储
    /**
     * 获取系统存储路径
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * 返回通过Context.openFileOutput()创建和存储的文件系统的绝对路径，应用程序文件，
     * 这些文件会在程序被卸载的时候全部删掉。
     */
    public static File getAppFile(Context context)
    {
        return context.getFilesDir();
    }


    /**
     * 返回应用程序指定的缓存目录，这些文件在设备内存不足时会优先被删除掉，
     * 所以存放在这里的文件是没有任何保障的，可能会随时丢掉。
     */
    public static File getAppCache(Context context)
    {
        return context.getCacheDir();
    }


    /**
     * 这是一个可以存放你自己应用程序自定义的文件，你可以通过该方法返回的File实例来创建或者访问这个目录，
     * 注意该目录下的文件只有你自己的程序可以访问。
     */
    public static File getAppDir(Context context, String path)
    {
        return context.getDir(path, 0);
    }


    public static String getSystemPicFile(Context context)
    {
        return getSDCardPath() + File.separator + "DCIM" + File.separator +"Camera";
    }


}
