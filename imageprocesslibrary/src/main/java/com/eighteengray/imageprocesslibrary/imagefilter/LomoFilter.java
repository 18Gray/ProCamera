package com.eighteengray.imageprocesslibrary.imagefilter;




public class LomoFilter implements IImageFilter
{
	private BrightContrastFilter contrastFx;
    private GradientMapFilter gradientMapFx;
    private ImageBlender blender = new ImageBlender();
    private VignetteFilter vignetteFX;
    private NoiseFilter noiseFx;
    
    

	@Override
	public Image process(Image image)
	{
		// TODO Auto-generated method stub
		contrastFx = new BrightContrastFilter();
		contrastFx.BrightnessFactor = 0.05f;
        contrastFx.ContrastFactor = 0.5f;
		
        blender.Mixture = 0.5f;
        blender.Mode = ImageBlender.BlendMode.Multiply;
		
        vignetteFX = new VignetteFilter();
        vignetteFX.Size = 0.6f;
        
        
        Image tempImg = contrastFx.process(image);
        noiseFx = new NoiseFilter();
        noiseFx.Intensity = 0.02f;
		tempImg = noiseFx.process(tempImg);
		gradientMapFx = new GradientMapFilter();
		image = gradientMapFx.process(tempImg);
		image = blender.Blend(image, tempImg);
		vignetteFX = new VignetteFilter();
		vignetteFX.Size = 0.6f;
		image = vignetteFX.process(tempImg);
		return image;
	}

}
