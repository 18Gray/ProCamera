package com.eighteengray.procamera.model;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import rx.Observable;


public class SystemModel
{
    public static Observable<Cursor> getImagesCursor(ContentResolver contentResolver)
    {
        final Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]
                        {MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        return Observable.just(cursor);
    }

}
