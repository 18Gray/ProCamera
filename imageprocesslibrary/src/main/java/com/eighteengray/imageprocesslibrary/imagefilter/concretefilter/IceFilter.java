package com.eighteengray.imageprocesslibrary.imagefilter.concretefilter;


import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;

public class IceFilter implements IImageFilter
{

	@Override
	public Image process(Image image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int R, G, B, pixel;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				R = image.getRComponent(x, y); // ��ȡRGB��ԭɫ
				G = image.getGComponent(x, y);
				B = image.getBComponent(x, y);

				pixel = R - G - B;
				pixel = pixel * 3 / 2;
				if (pixel < 0)
					pixel = -pixel;
				if (pixel > 255)
					pixel = 255;
				R = pixel;

				pixel = G - B - R;
				pixel = pixel * 3 / 2;
				if (pixel < 0)
					pixel = -pixel;
				if (pixel > 255)
					pixel = 255;
				G = pixel;

				pixel = B - R - G;
				pixel = pixel * 3 / 2;
				if (pixel < 0)
					pixel = -pixel;
				if (pixel > 255)
					pixel = 255;
				B = pixel;
				image.setPixelColor(x, y, R, G, B);
			} // x
		} // y

		return image;

	}

}
