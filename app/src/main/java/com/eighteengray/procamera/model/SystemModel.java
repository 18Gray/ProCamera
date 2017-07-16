package com.eighteengray.procamera.model;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SystemModel
{
    public static Observable<Cursor> getImagesCursor(ContentResolver contentResolver)
    {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        /*Cursor cursor = contentResolver.query(imageUri, null, null, null, null);*/


        /*String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);*/


        /*String[] projection = new String[]{MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA, // 图片绝对路径
                "count(" + MediaStore.Images.Media._ID + ")"//统计当前文件夹下共有多少张图片
        };
        String selection = " 0==0) group by bucket_display_name --(";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");*/


        /*String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(contentResolver, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Thumbnails.MINI_KIND, projection);*/


        /*Cursor cursor = contentResolver.query(imageUri, null ,MediaStore.Images.Media.MIME_TYPE + "=? or "
                +MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg","image/png" ,"image/bmp"},
                MediaStore.Images.Media.DATE_ADDED +" DESC");*/

        final Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]
                        {MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");



        int count = cursor.getCount();
        int columnCount = cursor.getColumnCount();
        return Observable.just(cursor);
    }

}
