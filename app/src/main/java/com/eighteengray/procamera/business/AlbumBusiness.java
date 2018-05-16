package com.eighteengray.procamera.business;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

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
        final Map<String, ImageFolder> imageFolderHashMap = new HashMap(); //用于防止同一个文件夹的多次扫描
        final List<ImageFolder> imageFolderList = new ArrayList<>();

        Observable<List<ImageFolder>> imageFolderListObserverable =
                SystemModel.getImagesCursor(contentResolver).flatMap(
                        new Func1<Cursor, Observable<List<ImageFolder>>>() {
                            @Override
                            public Observable<List<ImageFolder>> call(Cursor cursor) {
                                if (cursor != null && cursor.getCount() > 0)
                                {
                                    // 所有图片
                                    boolean isSetAllPicFolder = false;
                                    ImageFolder allPicImageFolder = new ImageFolder();
                                    imageFolderList.add(allPicImageFolder);

                                    // 子文件夹
                                    while (cursor.moveToNext()){
                                        int data = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                        // 获取图片的路径
                                        String imagePath = cursor.getString(data);

                                        //设置所有图片
                                        if(!isSetAllPicFolder){
                                            allPicImageFolder.setName("所有图片");
                                            allPicImageFolder.setFirstImagePath(imagePath);
                                            allPicImageFolder.setSelected(true);
                                            isSetAllPicFolder = true;
                                        }
                                        allPicImageFolder.addImagePath(imagePath);

                                        // 设置子文件夹
                                        File parentFile = new File(imagePath).getParentFile();
                                        String dirPath = parentFile.getAbsolutePath();
                                        if (TextUtils.isEmpty(dirPath)) {
                                            int end = dirPath.lastIndexOf(File.separator);
                                            if (end != -1) {
                                                dirPath = imagePath.substring(0, end);
                                            }
                                        }else {  // 生成文件夹
                                            ImageFolder imageFolder = null;
                                            if (!imageFolderHashMap.containsKey(dirPath))
                                            {
                                                imageFolder = new ImageFolder();
                                                String folderName = dirPath.substring(dirPath.lastIndexOf(File.separator) + 1);
                                                if (TextUtils.isEmpty(folderName)) {
                                                    folderName = "/";
                                                }
                                                imageFolder.setName(folderName);
                                                imageFolder.setFolderDir(dirPath);
                                                imageFolder.setFirstImagePath(imagePath);
                                                imageFolder.setSelected(false);
                                                imageFolderHashMap.put(dirPath, imageFolder);
                                            } else
                                            {
                                                imageFolder = imageFolderHashMap.get(dirPath);
                                            }
                                            // 导入图片地址
                                            imageFolder.addImagePath(imagePath);
                                        }
                                    }
                                    imageFolderList.addAll(imageFolderHashMap.values());
                                }
                                cursor.close();
                                return Observable.just(imageFolderList);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        return imageFolderListObserverable;
    }



}
