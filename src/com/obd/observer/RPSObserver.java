package com.obd.observer;

import org.json.JSONObject;

import android.util.Log;

import com.comobd.zyobd.observer.RPMSPDObserver;
import com.comobd.zyobd.port.Subject;

public class RPSObserver extends RPMSPDObserver {
	private static final String TAG = RPSObserver.class.getName();

	public RPSObserver(Subject subject) {
		super(subject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(JSONObject jsonString) {
		// TODO Auto-generated method stub
		super.update(jsonString);
		Log.e(TAG, jsonString.toString());
	}

}
