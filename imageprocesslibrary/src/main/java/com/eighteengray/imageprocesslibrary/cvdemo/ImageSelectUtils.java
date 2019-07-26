package com.eighteengray.imageprocesslibrary.cvdemo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gloomy fish on 2017/6/29.
 */

public class ImageSelectUtils {

    private static String TAG = "ImageSelectUtils";
    public static File getSaveFilePath() {
        String status = Environment.getExternalStorageState();
        if(!status.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "SD Card is not suitable...");
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String name = df.format(new Date(System.currentTimeMillis()))+ ".jpg";
        File filedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myOcrImages");
        filedir.mkdirs();
        String fileName = filedir.getAbsolutePath() + File.separator + name;
        File imageFile = new File(fileName);
        return imageFile;
    }

    public static String getRealPath(Uri uri, Context appContext) {
        String filePath = null;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){//4.4及以上
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = appContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                    sel, new String[] { id }, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }else{//4.4以下，即4.4以上获取路径的方法
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = appContext.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
        }
        Log.i(TAG, "selected image path : " + filePath);
        return filePath;
    }

    public static void saveImage(Mat image) {
        File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "mybook");
        if(!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String name = String.valueOf(System.currentTimeMillis()) + "_book.jpg";
        File tempFile = new File(fileDir.getAbsoluteFile()+File.separator, name);
        Imgcodecs.imwrite(tempFile.getAbsolutePath(), image);
    }

}
