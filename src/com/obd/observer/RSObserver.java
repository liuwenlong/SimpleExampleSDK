package com.obd.observer;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.comobd.zyobd.observer.RPMSPDObserver;
import com.comobd.zyobd.port.Subject;
import com.obd.service.DataSyncService;

import de.greenrobot.event.EventBus;

/**
 * 车辆行驶过程中实时数据上传
 * @author LUFFY
 *
 */
public class RSObserver extends RPMSPDObserver {
	private static final String TAG = RSObserver.class.getName();

	public RSObserver(Subject subject) {
		super(subject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(JSONObject jsonString) {
		// TODO Auto-generated method stub
		super.update(jsonString);
		DataSyncService.postEvent(1, jsonString);
		Log.e(TAG, jsonString.toString());
	}

}
