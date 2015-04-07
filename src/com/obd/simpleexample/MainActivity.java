package com.obd.simpleexample;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.comobd.zyobd.api.OBDAPI;
import com.obd.app.bean.JsonMsg;
import com.obd.observer.BObserver;
import com.obd.service.DataSyncService;
import com.obd.utils.MyLog;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, DataSyncService.class));
		MyLog.E("MainActivity onCreate");
	}

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.start:
			EventBus.getDefault().post("IGNITION");
			break;
		case R.id.stop:
			JsonMsg msg = new JsonMsg();
			msg.what = 2;
			String json = getString(R.string.stop);
			MyLog.D( json);
			
			try {
				msg.obj = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			EventBus.getDefault().post(msg);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mHandler.postDelayed(mSyncRunnable, 3000);
		MobclickAgent.onResume(this);
	}
	
	Handler mHandler = new Handler();
	
	Runnable mSyncRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Runtime.getRuntime().exec("sync");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MyLog.E("sync 执行成功");			
		}
		
	};
	
}
