package com.eighteengray.procameralibrary.common;




public class Constants {
	//Intent Bundle Key
	public static final String CROPIMAGEPATH = "cropimagepath";
	public static final String IMAGEFOLDERS = "imagefolders";
	public static final String CURRENTFOLDERNUM = "currentfoldernum";
	public static final String VERTICAL_RECYCLER_ITEM = "verticalRecyclerItem";
    public static final String HORIZONTAL_RECYCLER_ITEM = "horizontalRecyclerItem";

    public static final String IS_RADIO = "isRadio";
    public static final String IS_TAKE_CAMERA = "isTakeCamera";
    public static final String IS_SHOW_ADD = "isShowAdd";

	public static final String IMAGE_PATH = "imagePath";

	// 分享相关
    public static final String SHARE_DIALOG_TYPE = "shareDialogType";
    public static final String SHARE_TITLE = "shareTitle";
    public static final String SHARE_CONTENT = "shareContent";
    public static final String SHARE_URL = "shareUrl";
    public static final String SHARE_IMAGE_URI = "shareImageUri";

	//Handler Message What
	public static final int IMAGEPROCESS = 106;
	public static final int CUTPIC = 112;


	//startActivityForResult--requestCode
	public static final int CUT_IMAGE = 103;


	// SharePreference要保存的key
    public static final String SETTINGS = "settings";
    public static final String IMAGE_QUALITY = "imageQuality";
    public static final String IMAGE_FORMAT = "imageFormat";
    public static final String IMAGE_GRID = "imageGrid";
    public static final String IMAGE_BALANCE = "imageBalance";
    public static final String IMAGE_ANTI_SHAKE = "imageAntiShake";
    public static final String IMAGE_MUTE = "imageMute";
    public static final String IMAGE_HISTOGRAM = "imageHistogram";
    public static final String IMAGE_SIGN = "imageSign";
    public static final String IMAGE_SIGN_DATE = "imageSignDate";
    public static final String IMAGE_LOCATION = "imageLocation";
    public static final String IMAGE_SIGN_NAME = "imageSignName";
    public static final String IMAGE_SIGN_TEXT_COLOR = "imageSignTextColor";
    public static final String IMAGE_SIGN_TEXT_SIZE = "imageSignTextSize";

    public static final String VIDEO_QUALITY = "videoQuality";

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

	// Card的时候必须要传递的包名
	public  static String viewModelPackage = "com.eighteengray.procamera.viewmodel";
}
