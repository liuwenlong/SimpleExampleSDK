package com.obd.observer;

import org.json.JSONObject;

import android.util.Log;

import com.comobd.zyobd.observer.DRONObserver;
import com.comobd.zyobd.port.Subject;
import com.obd.service.DataSyncService;
import com.obd.simpleexample.StatusInface;

import de.greenrobot.event.EventBus;

/**
 * 车辆熄火之后驾驶习惯数据上传
 * @author LUFFY
 *
 */
public class DRObserver extends DRONObserver{
	private static final String TAG = DRObserver.class.getName();

	public DRObserver(Subject subject) {
		super(subject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(JSONObject jsonString) {
		// TODO Auto-generated method stub
		super.update(jsonString);
		//StatusInface.getInstance().postRSO(jsonString);
		DataSyncService.postEvent(2, jsonString);
		Log.e(TAG, jsonString.toString());
	}

}
