package com.eighteengray.procamera.business;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.eighteengray.procamera.R;
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
    public static Observable<List<ImageFolder>> getImageFolder(final Context context, final boolean isRadio, final boolean isTakeCamera, final boolean isShowAdd)
    {
        final Map<String, ImageFolder> imageFolderHashMap = new HashMap(); //用于防止同一个文件夹的多次扫描
        final List<ImageFolder> imageFolderList = new ArrayList<>();

        Observable<List<ImageFolder>> imageFolderListObserverable =
                SystemModel.getImagesCursor(context.getContentResolver()).flatMap(
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

                                            if(isTakeCamera){ // 第一个图，相机图
                                                ImageFolder.ImageItem takeCameraItem = new ImageFolder.ImageItem();
                                                takeCameraItem.resource = R.mipmap.take_camera_gray_48dp;
                                                takeCameraItem.showCheckBox = false;
                                                takeCameraItem.isChecked = false;
                                                allPicImageFolder.getImagePathList().add(0, takeCameraItem);
                                            }
                                            if(isShowAdd){ // 最后一个图，添加图
                                                ImageFolder.ImageItem addImageItem = new ImageFolder.ImageItem();
                                                addImageItem.resource = R.mipmap.add_grey_24dp;
                                                addImageItem.showCheckBox = false;
                                                addImageItem.isChecked = false;
                                                allPicImageFolder.addImagePath(addImageItem);
                                            }
                                            isSetAllPicFolder = true;
                                        }

                                        // 设置中间的图片
                                        ImageFolder.ImageItem imageItem = new ImageFolder.ImageItem();
                                        imageItem.imagePath = imagePath;
                                        if(isRadio){
                                            imageItem.showCheckBox = false;
                                            imageItem.isChecked = false;
                                        }else {
                                            imageItem.showCheckBox = true;
                                            imageItem.isChecked = false;
                                        }
                                        allPicImageFolder.addImagePath(imageItem);


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
                                            ImageFolder.ImageItem imageItem1 = new ImageFolder.ImageItem();
                                            imageItem1.imagePath = imagePath;
                                            if(isRadio){
                                                imageItem1.showCheckBox = false;
                                                imageItem1.isChecked = false;
                                            }else {
                                                imageItem1.showCheckBox = true;
                                                imageItem1.isChecked = false;
                                            }
                                            imageFolder.addImagePath(imageItem1);
                                        }
                                    }

                                    // 遍历完成后，需要根据是否显示相机按钮和是否显示添加按钮，对每个文件夹的图像列表，
                                    // 在开头和结尾添加相机按钮和添加按钮
                                    for(Map.Entry<String, ImageFolder> entry: imageFolderHashMap.entrySet())
                                    {
                                        ImageFolder currentFolder = entry.getValue();
                                        if(isTakeCamera){ // 第一个图，相机图
                                            ImageFolder.ImageItem takeCameraItem = new ImageFolder.ImageItem();
                                            takeCameraItem.resource = R.mipmap.take_camera_gray_48dp;
                                            takeCameraItem.showCheckBox = false;
                                            takeCameraItem.isChecked = false;
                                            currentFolder.getImagePathList().add(0, takeCameraItem);
                                        }
                                        if(isShowAdd){ // 最后一个图，添加图
                                            ImageFolder.ImageItem addImageItem = new ImageFolder.ImageItem();
                                            addImageItem.resource = R.mipmap.add_grey_24dp;
                                            addImageItem.showCheckBox = false;
                                            addImageItem.isChecked = false;
                                            currentFolder.addImagePath(addImageItem);
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
