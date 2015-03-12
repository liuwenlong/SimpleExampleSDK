package com.obd.app;

import com.obd.utils.DBmanager;
import com.obd.utils.QuickShPref;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MGApp extends Application{
	public String TAG = "MGApp";
	public static MGApp pThis;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();
	}
	
	private void init(){
		Log.d(TAG, "=============>MGApp<============");
		pThis = this;
		DBmanager.init(pThis);
		QuickShPref.init(pThis);
	}

	
}
