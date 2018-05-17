package com.eighteengray.procameralibrary.common;




public class Constants
{
	//Intent Bundle Key
	public static final String CROPIMAGEPATH = "cropimagepath";
	public static final String IMAGEFOLDERS = "imagefolders";
	public static final String CURRENTFOLDERNUM = "currentfoldernum";

    public static final String IS_RADIO = "isRadio";
    public static final String IS_TAKE_CAMERA = "isTakeCamera";
    public static final String IS_SHOW_ADD = "isShowAdd";


	//Handler Message What
	public static final int IMAGEPROCESS = 106;
	public static final int CUTPIC = 112;


	//startActivityForResult--requestCode
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

	public static final int RATIO_NORMAL = 12000;
	public static final int RATIO_SQUARE = 12001;
	public static final int RATIO_4V3 = 12002;
	public static final int RATIO_16V9 = 12003;

	public static final int FOCUS_FOCUSING = 10017;
	public static final int FOCUS_SUCCEED = 10018;
	public static final int FOCUS_INACTIVE = 10019;
	public static final int FOCUS_FAILED = 10020;

	public  static String viewModelPackage = "com.eighteengray.procamera.viewmodel";
}
