package com.obd.simpleexample;

import android.util.Log;

import com.comobd.zyobd.listener.StatusVehicleListener;
import com.obd.utils.MyLog;

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
		//EventBus.getDefault().post(result);
		MyLog.E("callBack result="+result);
		StatusInface.getInstance().vehicleResult(result);
		return result;
	}

}
