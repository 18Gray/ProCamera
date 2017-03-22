package com.eighteengray.procameralibrary.camera;



public interface ICameraOperation
{
	//主体功能
	/**
	 * 拍照
	 */
	public void takePicture();



	//镜头相关
	/**
	 * 切换前置和后置相机
	 */
	public void switchCamera();


	/**
	 * 相机最大缩放级别
	 */
	public int getMaxZoom();


	/**
	 * 设置当前缩放级别
	 * @param zoom
	 */
	public void setZoom(int zoom);


	/**
	 * 获取当前缩放级别
	 */
	public int getZoom();


	/**
	 * 手动对焦
	 */
    public void manualFocus();


	/**
	 * 手动测光
	 */
	public void manualMeter();


	/**
	 * 手动同时进行对焦和测光
	 */
	public void manualFocusMeter();


	/**
	 * 人脸识别
	 */
	public void faceRecognize();




	//辅助功能
	/**
	 * 获取当前闪光灯模式
	 * @return
	 */


	/**
	 * 设置闪光灯模式，包括手电筒模式
	 * @param flashMode
	 */


    /**
	 * 设置HDR
	 */
	public void setHDR();


	/**
	 * 设置GPU滤镜
	 */
	public void setGPUFilter();


	/**
	 * 设置拍摄延时
	 */
	public void setDelayTime(int time);


    /**
	 * 比例调节
	 */
	public void setRatio(float ratio);


}
