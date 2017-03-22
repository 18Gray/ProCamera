package com.eighteengray.imageprocesslibrary.imagefilter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

//�Զ�����ʵ��ͼ����ǿЧ���� ����:���Ͷȡ�ɫ�ࡢ����
public class IncreaseProcessImage
{
	private Bitmap mBitmap;

	// ���캯��
	public IncreaseProcessImage(Bitmap bmp)
	{
		mBitmap = bmp;
	}

	// �Զ������
	private ColorMatrix mSaturationMatrix; // ���Ͷ�
	private ColorMatrix mHueMatrix; // ɫ��
	private ColorMatrix mLumMatrix; // ����
	private ColorMatrix mAllMatrix;
	private float mSaturationValue = 0F;
	private float mHueValue = 0F;
	private float mLumValue = 0F;
	// SeekBar�м�ֵ127 [0-255]
	private static final int MIDDLE_VALUE = 127;
	private static final int MAX_VALUE = 255;

	
	
	/*
	 * ���ñ��Ͷ�ֵ
	 */
	public void setSaturation(int value)
	{
		mSaturationValue = value * 1.0F / MIDDLE_VALUE;
	}

	
	
	
	/*
	 * ����ɫ��ֵ
	 */
	public void SetHue(int value)
	{
		mHueValue = (value - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
	}

	
	
	
	/*
	 * ��������ֵ
	 */
	public void SetLum(int value)
	{
		mLumValue = value * 1.0F / MIDDLE_VALUE;
	}

	
	
	
	/*
	 * ͼ����ǿ ���Ͷȴ��� ɫ�ദ�� ���ȴ��� flag=0��ʾ�Ƿ�ı䱥�Ͷ� flag=1��ʾ�Ƿ�ı�ɫ�� flag=2��ʾ�Ƿ�ı�����
	 */
	public Bitmap IncreaseProcessing(Bitmap bmp, int flag)
	{
		// ����һ����ͬ�ߴ�ɱ��λͼ��,���ڻ�����ͼ
		Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true); // ��Canvas���Ͽ���ݱ�־,��Եƽ������
		if (mAllMatrix == null)
		{
			mAllMatrix = new ColorMatrix();
		}
		if (mSaturationMatrix == null)
		{
			mSaturationMatrix = new ColorMatrix();
		}
		if (mHueMatrix == null)
		{
			mHueMatrix = new ColorMatrix();
		}
		if (mLumMatrix == null)
		{
			mLumMatrix = new ColorMatrix();
		}
		// ͼ����
		if (flag == 0)
		{ // ���Ͷ�
			// ���Ͷ�ֵ��С����Ϊ0,��ʱ��ʾ�Ҷ�ͼ Ϊ1��ʾ���ͶȲ���,���ô���1���ǹ��
			mSaturationMatrix.reset(); // ��ΪĬ��ֵ
			mSaturationMatrix.setSaturation(mSaturationValue);
		} 
		else if (flag == 1)
		{ // ɫ��
			// hueColor��ɫ����ת�Ƕ�,��ֵ��ʾ˳ʱ����ת,��ֵ��ʾ��ʱ����ת
			mHueMatrix.reset();
			mHueMatrix.setRotate(0, mHueValue); // �����ú�ɫ����ɫ������ת�ĽǶ�
			mHueMatrix.setRotate(1, mHueValue); // ��������ɫ����ɫ������ת�ĽǶ�
			mHueMatrix.setRotate(2, mHueValue); // ��������ɫȡ��ɫ������ת�ĽǶ�
		} 
		else if (flag == 2)
		{ // ����
			mLumMatrix.reset();
			// ��������ɫ����ͬ����,������1��ʾ͸���Ȳ���
			mLumMatrix.setScale(mLumValue, mLumValue, mLumValue, 1);
		}
		// ����AllMatrix
		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix); // Ч�����
		mAllMatrix.postConcat(mSaturationMatrix);
		mAllMatrix.postConcat(mLumMatrix);
		// ������ɫ�任Ч��
		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
		canvas.drawBitmap(bmp, 0, 0, paint); // ��ɫ�仯��������´���λͼ��
		return bitmap;
	}


}
