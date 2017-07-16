package com.eighteengray.procamera.business;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.model.SystemModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 相册相关的业务类，主要作用是处理获取到的Cursor，封装成ImageFolder，然后返回其对应的Observerable
 */
public class AlbumBusiness
{

    //获取ImageFolder的列表
    public static Observable<List<ImageFolder>> getImageFolder(ContentResolver contentResolver)
    {
        final Map<String, Integer> tmpDir = new HashMap<String, Integer>(); //用于防止同一个文件夹的多次扫描
        final List<ImageFolder> imageFolderList = new ArrayList<>();
        Observable<List<ImageFolder>> imageFolderListObserverable =
                SystemModel.getImagesCursor(contentResolver).
                        flatMap(new Func1<Cursor, Observable<List<ImageFolder>>>() {
                            @Override
                            public Observable<List<ImageFolder>> call(Cursor cursor) {
                                if (cursor.moveToFirst())
                                {
                                    int data = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                    do
                                    {
                                        // 获取图片的路径
                                        String path = cursor.getString(data);

                                        // 获取该图片的父路径名
                                        File parentFile = new File(path).getParentFile();
                                        if (parentFile == null)
                                        {
                                            continue;
                                        }
                                        String dirPath = parentFile.getAbsolutePath();
                                        ImageFolder imageFolder = null;
                                        if (!tmpDir.containsKey(dirPath))
                                        {
                                            // 初始化imageFloder
                                            imageFolder = new ImageFolder();
                                            imageFolder.setFolderDir(dirPath);
                                            imageFolder.setFirstImagePath(path);
                                            imageFolderList.add(imageFolder);
                                            tmpDir.put(dirPath, imageFolderList.indexOf(imageFolder));
                                        } else
                                        {
                                            imageFolder = imageFolderList.get(tmpDir.get(dirPath));
                                        }
                                        imageFolder.getImagePathList().add(path);
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                                return Observable.just(imageFolderList);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        return imageFolderListObserverable;
    }



}
