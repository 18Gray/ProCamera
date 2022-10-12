package com.eighteengray.procamera.imageprocess.bean;



public class MarkInfo {
    private int markAdapterResource;
    private int markResource;

    public MarkInfo(int markAdapterResource, int markResource) {
        this.markAdapterResource = markAdapterResource;
        this.markResource = markResource;
    }

    public int getMarkAdapterResource() {
        return markAdapterResource;
    }

    public void setMarkAdapterResource(int markAdapterResource) {
        this.markAdapterResource = markAdapterResource;
    }

    public int getMarkResource() {
        return markResource;
    }

    public void setMarkResource(int markResource) {
        this.markResource = markResource;
    }
}
