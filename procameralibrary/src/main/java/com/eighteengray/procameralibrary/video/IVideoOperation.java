package com.eighteengray.procameralibrary.video;




public interface IVideoOperation
{
	/**
	 * 开始录像
	 * @return 是否成功开始录像
	 */
	public void startRecord();

	/**
	 * 停止录像
	 * @return 录像缩略图
	 */
	public void stopRecord();



}
