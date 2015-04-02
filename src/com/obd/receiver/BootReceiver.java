package com.obd.receiver;
import com.obd.service.DataSyncService;
import com.obd.simpleexample.MainActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{
	public static final boolean IS_OBD_SYNC_OPEN = false;
	public static final boolean IS_MG_SERVER_OPEN = true;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.i("tt","onReceive-----"+arg1.getAction());
		//arg0.startService(new Intent(arg0, DataSyncService.class));
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(arg0, MainActivity.class);
		arg0.startActivity(intent);
	}
	
}
