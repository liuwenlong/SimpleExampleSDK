package com.obd.observer;

import org.json.JSONObject;

import android.util.Log;

import com.comobd.zyobd.observer.DRONObserver;
import com.comobd.zyobd.port.Subject;
import com.obd.service.DataSyncService;
import com.obd.simpleexample.StatusInface;
import com.obd.utils.DBmanager;

import de.greenrobot.event.EventBus;

/**
 * ����Ϩ��֮���ʻϰ�������ϴ�
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
		DBmanager.getInase().insertBackupNote(jsonString.toString(),""); 
	}

}
