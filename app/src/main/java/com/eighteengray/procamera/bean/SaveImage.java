package com.eighteengray.procamera.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class SaveImage implements Serializable
{

	private static final long serialVersionUID = 1L;
	public String saveImagePath;

	public String subtitle;
	public boolean isZhefu;

	public Bitmap yinjiBitmap;
	public int x;
	public int y;

	public String getSaveImagePath()
	{
		return saveImagePath;
	}

	public void setSaveImagePath(String saveImagePath)
	{
		this.saveImagePath = saveImagePath;
	}

	public String getSubtitle()
	{
		return subtitle;
	}

	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}

	public boolean isZhefu()
	{
		return isZhefu;
	}

	public void setZhefu(boolean isZhefu)
	{
		this.isZhefu = isZhefu;
	}

	public Bitmap getYinjiBitmap()
	{
		return yinjiBitmap;
	}

	public void setYinjiBitmap(Bitmap yinjiBitmap)
	{
		this.yinjiBitmap = yinjiBitmap;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "SaveImage [saveImagePath=" + saveImagePath + ", subtitle="
				+ subtitle + ", isZhefu=" + isZhefu + ", yinjiBitmap="
				+ yinjiBitmap + ", x=" + x + ", y=" + y + "]";
	}

}
