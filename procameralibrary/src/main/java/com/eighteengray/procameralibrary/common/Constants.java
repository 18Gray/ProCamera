package com.eighteengray.procameralibrary.common;

import android.os.Environment;

import java.io.File;

public class Constants
{
	public static final String path = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator + "MyCamera" + File.separator;
	
	
	//startActivityForResult
	public static final int GPUFILTER = 101;
	public static final int CUT_FILM = 103;
	public static final int SUBTITLE = 102;
	

	//Handler
	public static final int SAVEIMAGE = 105;
	public static final int IMAGEPROCESS = 106;
	public static final int SAVERESULTIMAGES = 107;
	public static final int SAVETAKEDPIC = 108;
	public static final int ADDIMAGE = 109;
	public static final int OKSELECT = 110;
	public static final int GOCAMERA = 111;
	public static final int CUTPIC = 112;
	public static final int NOPIC = 113;
	
	
	//Intent Bundle
	public static final String IMAGEFOLDERS = "imagefolders";
	public static final String CURRENTFOLDERNUM = "currentfoldernum";


	//EventBus常量
	public static final int MODE_CAMERA = 10001;
	public static final int MODE_RECORD = 10002;

	public static final int FLASH_AUTO = 10003;
	public static final int FLASH_ON = 10004;
	public static final int FLASH_OFF = 10005;
	public static final int FLASH_FLARE = 10006;

	public static final int HDR_ON = 10007;
	public static final int HDR_OFF = 10008;

	public static final int DELAY_3 = 10009;
	public static final int DELAY_5 = 100010;
	public static final int DELAY_8 = 100011;
	public static final int DELAY_10 = 10012;

	//一大波GPU滤镜，从11000开始
	public static final int GPUFILTER_0 = 11000;
	public static final int GPUFILTER_1 = 11001;
	public static final int GPUFILTER_2 = 11002;
	public static final int GPUFILTER_3 = 11003;
	public static final int GPUFILTER_4 = 11004;
	public static final int GPUFILTER_5 = 11005;
	public static final int GPUFILTER_6 = 11006;
	public static final int GPUFILTER_7 = 11007;
	public static final int GPUFILTER_8 = 11008;

	//一大波ratio比例，从12000开始
	public static final int RATIO_NORMAL = 12000;
	public static final int RATIO_SQUARE = 12001;
	public static final int RATIO_4V3 = 12002;
	public static final int RATIO_16V9 = 12003;

	//TextureView触摸事件
	public static final int TOUCH_TEXTURE_CLICK = 10013;
	public static final int TOUCH_TEXTURE_LONGCLICK = 10014;
	public static final int TOUCH_TEXTURE_ONEDRAG = 10015;
	public static final int TOUCH_TEXTURE_TWODRAG = 10016;

	//FocusState
	public static final int FOCUS_FOCUSING = 10017;
	public static final int FOCUS_SUCCEED = 10018;
	public static final int FOCUS_INACTIVE = 10019;
	public static final int FOCUS_FAILED = 10020;
}
