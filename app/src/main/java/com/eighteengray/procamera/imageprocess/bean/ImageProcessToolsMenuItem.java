package com.eighteengray.procamera.imageprocess.bean;

import java.io.Serializable;

public class ImageProcessToolsMenuItem implements Serializable
{
    public int resourceId;
    public String name;

    ImageProcessToolsMenuItem(int resourceId, String name){
        this.resourceId = resourceId;
        this.name = name;
    }

}
