package com.eighteengray.imageprocesslibrary.imagefilter;



public class FeatherFilter implements IImageFilter
{
	public float Size = 0.8f;

	
	@Override
	public Image process(Image image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int ratio = width > height ? height * 32768 / width : width * 32768
				/ height;

		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - Size));
		int diff = max - min;

		int R, G, B;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				R = image.getRComponent(x, y); 
				G = image.getGComponent(x, y);
				B = image.getBComponent(x, y);

				// Calculate distance to center and adapt aspect ratio
				int dx = cx - x;
				int dy = cy - y;
				if (width > height)
				{
					dx = (dx * ratio) >> 15;
				} else
				{
					dy = (dy * ratio) >> 15;
				}
				int distSq = dx * dx + dy * dy;
				float v = ((float) distSq / diff) * 255;
				R = (int) (R + (v));
				G = (int) (G + (v));
				B = (int) (B + (v));
				R = (R > 255 ? 255 : (R < 0 ? 0 : R));
				G = (G > 255 ? 255 : (G < 0 ? 0 : G));
				B = (B > 255 ? 255 : (B < 0 ? 0 : B));
				image.setPixelColor(x, y, R, G, B);
			}
		}
		return image;

	}

}
