

package com.eighteengray.imageprocesslibrary.imagefilter.concretefilter;

import com.eighteengray.imageprocesslibrary.imagefilter.HslColor;
import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;



public class HslModifyFilter implements IImageFilter
{
	private float HueFactor;//ɫ��
    
    public HslModifyFilter(float HueFactor)
    {
        this.HueFactor = Math.max(0, Math.min(359, HueFactor));
    }

  
    @Override
    public Image process(Image imageIn)
    {
        int r, g, b;
        HslColor hsl = new HslColor(HueFactor, 0, 0);
      
        for (int x = 0; x < imageIn.getWidth(); x++) {
            for (int y = 0; y < imageIn.getHeight(); y++) {
         	    r = imageIn.getRComponent(x, y);
                g = imageIn.getGComponent(x, y);
                b = imageIn.getBComponent(x, y);

                HslColor.RgbToHsl(r, g, b, hsl);
                hsl.h = this.HueFactor;
                int color = HslColor.HslToRgb(hsl);
                imageIn.setPixelColor(x, y, color);
            }
        }
        return imageIn;
    }
}
