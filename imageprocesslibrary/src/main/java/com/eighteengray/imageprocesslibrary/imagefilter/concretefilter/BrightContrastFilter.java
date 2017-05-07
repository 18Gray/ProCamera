package com.eighteengray.imageprocesslibrary.imagefilter.concretefilter;

import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;

public class BrightContrastFilter implements IImageFilter
{

    public float BrightnessFactor = 0.25f;

    public float ContrastFactor = 0f;

    @Override
    public Image process(Image imageIn)
    {
        int r, g, b;
        // Convert to integer factors
        int bfi = (int) (BrightnessFactor * 255);
        float cf = 1f + ContrastFactor;
        cf *= cf;
        int cfi = (int) (cf * 32768) + 1;
        for (int x = 0; x < imageIn.getWidth(); x++)
        {
            for (int y = 0; y < imageIn.getHeight(); y++)
            {
                r = imageIn.getRComponent(x, y);
                g = imageIn.getGComponent(x, y);
                b = imageIn.getBComponent(x, y);
                // Modify brightness (addition)
                if (bfi != 0)
                {
                    // Add brightness
                    int ri = r + bfi;
                    int gi = g + bfi;
                    int bi = b + bfi;
                    // Clamp to byte boundaries
                    r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // Modifiy contrast (multiplication)
                if (cfi != 32769)
                {
                    // Transform to range [-128, 127]
                    int ri = r - 128;
                    int gi = g - 128;
                    int bi = b - 128;

                    // Multiply contrast factor
                    ri = (ri * cfi) >> 15;
                    gi = (gi * cfi) >> 15;
                    bi = (bi * cfi) >> 15;

                    // Transform back to range [0, 255]
                    ri = ri + 128;
                    gi = gi + 128;
                    bi = bi + 128;

                    // Clamp to byte boundaries
                    r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                imageIn.setPixelColor(x, y, r, g, b);
            }
        }
        return imageIn;
    }
}
