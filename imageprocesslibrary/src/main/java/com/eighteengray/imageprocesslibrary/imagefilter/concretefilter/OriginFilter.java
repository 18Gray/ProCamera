package com.eighteengray.imageprocesslibrary.imagefilter.concretefilter;


import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;

public class OriginFilter implements IImageFilter
{

	@Override
	public Image process(Image imageIn)
	{
		return imageIn;
	}

}
