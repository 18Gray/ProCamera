package com.eighteengray.procamera.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class ImageFolder implements Serializable
{
    /**
     * 文件夹路径
     */
    private String folderDir;

    /**
     * 第一张图片的路径，即封面图
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String name;

    /**
     * 文件夹是否被选中
     */
    public boolean isSelected;

    /**
     * 文件夹内所有图片的结构
     */
    public List<ImageItem> imagePathList = new ArrayList<ImageItem>();



    public String getFolderDir()
    {
        return folderDir;
    }

    public void setFolderDir(String folderDir)
    {
        this.folderDir = folderDir;
        int lastIndexOf = this.folderDir.lastIndexOf("/");
        this.name = this.folderDir.substring(lastIndexOf);
    }

    public String getFirstImagePath()
    {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath)
    {
        this.firstImagePath = firstImagePath;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }



    public static class ImageItem{
        public String imagePath;
        public int resource;
        public boolean showCheckBox;
        public boolean isChecked;
    }

    public List<ImageItem> getImagePathList()
    {
        return imagePathList;
    }

    public void setImagePathList(List<ImageItem> imagePathList)
    {
        this.imagePathList = imagePathList;
    }

    public void addImagePath(ImageItem imageItem){
        if(this.imagePathList != null){
            this.imagePathList.add(imageItem);
        }
    }

}
