package com.eighteengray.procamera.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import rx.Observable;


public class SystemModel
{
    public static Observable<Cursor> getImagesCursor(ContentResolver contentResolver)
    {
        final Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA},
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_ADDED + " DESC");
        return Observable.just(cursor);
    }

}
