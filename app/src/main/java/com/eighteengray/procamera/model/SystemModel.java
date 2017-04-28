package com.eighteengray.procamera.model;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SystemModel
{
    public static Observable<Cursor> getImagesCursor(ContentResolver contentResolver)
    {
        final Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]
                        {MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");

        Observable<Cursor> observable = Observable.just(cursor).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

}
