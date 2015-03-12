package com.obd.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.comobd.zyobd.api.OBDAPI;
import com.obd.observer.BObserver;
import com.obd.observer.RPSObserver;
import com.obd.utils.QuickShPref;
import com.obd.widget.GetLoaction;
import com.obd.widget.NetWork;
import com.umeng.analytics.MobclickAgent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DataSyncService extends Service{
	
	private static String TAG = "DataSyncService";
	private GetLoaction mGetLoaction = new GetLoaction();
	NetWork mNetWork = new NetWork();
	
    public class LocalBinder extends Binder {
        public DataSyncService getService() {
            return DataSyncService.this;
        }
    }

	@Override
	public void onCreate() {
		super.onCreate();
		 Log.i(TAG, "onCreate");
		 MobclickAgent.onResume(this);
		 initOBD();
		 locationInit();
		openADB();
		getIMEI();
		
		mNetWork.start();
	}

    @Override
    public void onDestroy() {
        mLocClient.stop();
        MobclickAgent.onPause(this);
        startService(new Intent(this, DataSyncService.class));
    }
    private final IBinder mBinder = new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        if(intent!=null && intent.getExtras()!=null){

        }
        return START_STICKY;
    }
	
    LocationClient mLocClient;
    MyLocationListenner myListener= new MyLocationListenner();
    // 定位初始化
    private void locationInit(){
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(30*1000);
		mLocClient.setLocOption(option);
		mLocClient.start(); 
		Log.d(TAG,"百度定位 初始化成功");
    }
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)	{Log.e(TAG,"百度定位失败"); return;}
			Log.d(TAG,"百度定位成功:"+location.getLongitude()+","+location.getLatitude());
			mGetLoaction.uploadPos(location);
		}
		public void onReceivePoi(BDLocation poiLocation) {}
	}
	public static void openADB(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				
				String url = "http://127.0.0.1/goform/debug?action=adb&item=1";
				HttpGet httpget = new HttpGet(url);
				HttpClient client = new DefaultHttpClient();
				try{
					HttpResponse resp = client.execute(httpget);
					
					if(resp != null)
						Log.e(TAG,"openADB--------resposeCode:"+resp.getStatusLine().getStatusCode());
					else
						Log.e(TAG,"openADB--------resposeCode null");
				}catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG,"openADB--------exception-------------------");
				}
				
			}
		}.start();
	}
	
	private void initOBD(){
		Log.d(TAG, "initOBD");
		OBDAPI obdapi = OBDAPI.getInstance(this.getApplicationContext());
		RPSObserver rpsObserver = new RPSObserver(obdapi);
		BObserver bObserver = new BObserver(obdapi);
		obdapi.initObserver();
		Log.d(TAG, "initOBD 成功");
	}
	
	public String getIMEI(){
		String imei = QuickShPref.getString(QuickShPref.IEMI);
		
		if(imei == null){
			imei =((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
			Log.d("ieme", imei);
			QuickShPref.putValueObject(QuickShPref.IEMI, imei);
		}

		return imei;
	}
}
