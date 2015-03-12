package com.obd.observer;

import org.json.JSONObject;

import android.util.Log;

import com.comobd.zyobd.observer.BDATObserver;
import com.comobd.zyobd.port.Subject;

public class BObserver extends BDATObserver {
	private static final String TAG = BObserver.class.getName();

	public BObserver(Subject subject) {
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
