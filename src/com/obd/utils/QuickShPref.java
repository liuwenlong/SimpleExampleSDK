package com.obd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class QuickShPref {
public static String IEMI = "imei";
public static String PASS_WORD = "password";
public static String Last_Laoren_objectId = "lastObjectID";
public static String USER_NAME= "user_name";
public static String USER_ID= "user_id";
public static String PEOPLE_ON= "people_on";
public static String TOKEN= "token";
public static String Image= "image";
public static String isLogin= "islogin";
public static String DisplayName= "DisplayName";

public static String Msg_sound= "Msg_sound";
public static String Msg_Vibrate= "Msg_Vibrate";

public static String IsStartOn = "IsStartOn";
public static String LAT = "lat";
public static String LON = "lon";
public static String TimeLastLoc = "TimeLastLoc";
public static String Time = "time";
public static String TimeGPS = "TimeGPS";

public static String StartTme = "StartTme";
public static String StartLat = "StartLat";
public static String StartLon = "StartLon";

private static SharedPreferences sSharedPreferences;
private static Editor sEditor;



	
	public static void  init(Context c){
		sSharedPreferences = c.getSharedPreferences(c.getPackageName(),Context.MODE_PRIVATE);
		sEditor = sSharedPreferences.edit();
	}
	
	public static void putValueObject(String key ,Object obj){
		if(obj instanceof String){
			sEditor.putString(key, (String)obj);
		}else if(obj instanceof Integer){
			sEditor.putInt(key, (Integer)obj);
		}else if(obj instanceof Boolean){
			sEditor.putBoolean(key, (Boolean)obj);
		}else if(obj instanceof Float){
			sEditor.putFloat(key, (Float)obj);
		}else{
			return;
		}
		sEditor.commit();
	}
	
	public static String getString(String key){
		return sSharedPreferences.getString(key, null);
	}
	public static int getInt(String key){
		return sSharedPreferences.getInt(key, -1);
	}
	public static boolean getBoolean(String key){
		return sSharedPreferences.getBoolean(key, false);
	}	
	public static Float getFloat(String key){
		return sSharedPreferences.getFloat(key, 0);
	}	
}
