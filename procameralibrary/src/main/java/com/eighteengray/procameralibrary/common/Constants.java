package com.eighteengray.procameralibrary.common;

import android.os.Environment;

import java.io.File;

public class Constants
{
	public static final String path = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator + "MyCamera" + File.separator;
	
	


	//Handler
	public static final int SAVEIMAGE = 105;
	public static final int IMAGEPROCESS = 106;
	public static final int SAVERESULTIMAGE = 107;
	public static final int SAVETAKEDPIC = 108;
	public static final int ADDIMAGE = 109;
	public static final int OKSELECT = 110;
	public static final int GOCAMERA = 111;
	public static final int CUTPIC = 112;
	public static final int NOPIC = 113;
	
	
	//Intent Bundle Key
	public static final String CROPIMAGEPATH = "cropimagepath";
	public static final String IMAGEFOLDERS = "imagefolders";
	public static final String CURRENTFOLDERNUM = "currentfoldernum";


	//startActivityForResult--requestCode
	public static final int SUBTITLE = 102;
	public static final int CUT_IMAGE = 103;




	//EventBus常量
	public static final int MODE_CAMERA = 10001;
	public static final int MODE_RECORD = 10002;

	public static final int FLASH_AUTO = 10003;
	public static final int FLASH_ON = 10004;
	public static final int FLASH_OFF = 10005;
	public static final int FLASH_FLARE = 10006;

	public static final int DELAY_3 = 10009;
	public static final int DELAY_5 = 100010;
	public static final int DELAY_8 = 100011;
	public static final int DELAY_10 = 10012;


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
