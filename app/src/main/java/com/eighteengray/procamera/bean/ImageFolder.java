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
     * 第一张图片的路径
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String name;

    /**
     * 文件夹内所有图片路径
     */
    public List<String> imagePathList = new ArrayList<String>();


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

    public List<String> getImagePathList()
    {
        return imagePathList;
    }

    public void setImagePathList(List<String> imagePathList)
    {
        this.imagePathList = imagePathList;
    }


}
