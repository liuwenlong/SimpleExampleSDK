package com.obd.simpleexample;

import android.util.Log;

import com.comobd.zyobd.listener.StatusVehicleListener;

import de.greenrobot.event.EventBus;


public class StatusVehicle implements StatusVehicleListener{
	private static final String TAG = StatusVehicle.class.getName();

	public StatusVehicle() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String callBack(String result) {
		// TODO Auto-generated method stub
		Log.e(TAG, result);
		//StatusInface.getInstance().postVehicle(result);
		EventBus.getDefault().post(result);
		return result;
	}

}
