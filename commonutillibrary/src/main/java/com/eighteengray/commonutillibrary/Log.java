package com.eighteengray.commonutillibrary;



public class Log
{

	private static boolean isPrintLog = BuildConfig.isShowLog;
    private static boolean isPrintLogError = isPrintLog;

	public static void isPrintLog(boolean isPrint) {
		isPrintLog = isPrint;
	}

	public static void e(String tag, String msg) {
		if (isPrintLogError && msg != null)
			android.util.Log.e(tag, msg);
	}

    public static void e(String tag, String msg, Exception e) {
        if (isPrintLog)
            android.util.Log.e(tag, msg, e);
    }

	public static void i(String tag, String msg) {
		if (isPrintLog && msg != null)
			android.util.Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (isPrintLog && msg != null)
			android.util.Log.w(tag, msg);

	}

	public static void d(String tag, String msg) {
		if (isPrintLog && msg != null)
			android.util.Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isPrintLog && msg != null)
			android.util.Log.v(tag, msg);
	}

	public static void println(String msg) {
		if (isPrintLog && msg != null)
			System.out.println(msg);
	}

    private static long time = 0;
    public static void time(String msg){
            long newtime = System.currentTimeMillis();
            android.util.Log.v("Log Time", msg + ":" + (newtime - time));
            time =  System.currentTimeMillis();

    }

	public static String getStackTraceString(Throwable tr){
		return android.util.Log.getStackTraceString(tr);
	}
}
