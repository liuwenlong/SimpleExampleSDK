package com.obd.utils;

import android.util.Log;

public class MyLog {
	private static boolean isDebug = true;
	private static String TAG = "mylog";
	
	public static void  D(String msg){
		if(isDebug)
			Log.d(TAG, msg);
	}
	
	public static void  I(String msg){
		if(isDebug)
			Log.i(TAG, msg);
	}
	
	public static void  W(String msg){
		if(isDebug)
			Log.w(TAG, msg);
	}
	
	public static void  E(String msg){
		if(isDebug)
			Log.e(TAG, msg);
	}
}
