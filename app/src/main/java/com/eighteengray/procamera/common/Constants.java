package com.eighteengray.procamera.common;

import java.io.File;

import android.os.Environment;
import android.view.WindowManager;

public class Constants
{
	public static final String path = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator + "MyCamera" + File.separator;
	
	
	//startActivityForResult
	public static final int CUT_FILM = 101;
	public static final int SUBTITLE = 102;
	
	public static final int CAMERA = 103;
	public static final int GALLERY = 104;
	
	
	
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
	
	
	//intent putExtra
	public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
	
	
}
