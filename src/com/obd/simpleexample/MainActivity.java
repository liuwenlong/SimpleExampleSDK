package com.obd.simpleexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.comobd.zyobd.api.OBDAPI;
import com.obd.observer.BObserver;
import com.obd.observer.RPSObserver;
import com.obd.service.DataSyncService;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, DataSyncService.class));
		Log.d("TAG", "MainActivity onCreate");
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
//		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onResume(this);
	}

	
}
