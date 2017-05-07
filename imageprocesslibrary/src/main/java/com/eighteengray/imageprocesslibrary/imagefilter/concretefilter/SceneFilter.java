
package com.eighteengray.imageprocesslibrary.imagefilter.concretefilter;


import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;



public class SceneFilter implements IImageFilter
{
    private GradientFilter gradientFx;
    private SaturationModifyFilter saturationFx;

    public SceneFilter(float angle, Gradient gradient)
    {
        gradientFx = new GradientFilter();
        gradientFx.Gradient = gradient;
        gradientFx.OriginAngleDegree = angle;

        saturationFx = new SaturationModifyFilter();
        saturationFx.SaturationFactor = -0.6f;
    }

    @Override
    public Image process(Image imageIn)
    {
        Image clone = imageIn.clone();
        imageIn = gradientFx.process(imageIn);
        ImageBlender blender = new ImageBlender();
        blender.Mode = ImageBlender.BlendMode.Subractive;
        return saturationFx.process(blender.Blend(clone, imageIn));
        //return imageIn;// saturationFx.process(imageIn);
    }
}
