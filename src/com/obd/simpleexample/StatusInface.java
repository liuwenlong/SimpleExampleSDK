package com.obd.simpleexample;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.obd.app.bean.DROInfo;
import com.obd.utils.DBmanager;
import com.obd.utils.MyLog;
import com.obd.utils.QuickShPref;

import de.greenrobot.event.EventBus;

public class StatusInface {
	private static final String START = "IGNITION";
	private static final String STOP = "FLAMEOUT";
	private static boolean isStartOn;
	
	private static StatusInface mInstance = new StatusInface();
	public static void init(Context c){
		isStartOn = QuickShPref.getBoolean(QuickShPref.IsStartOn);
		EventBus.getDefault().register(c);
	}
	public static void destory(Context c){
		EventBus.getDefault().unregister(c);
	}

	public static StatusInface getInstance(){
		return mInstance;
	}
	
	public void vehicleResult(String result){
		if(START.equalsIgnoreCase(result)){
			onStart();
		}else if(STOP.equalsIgnoreCase(result)){
			
		}
	}

	public void RSOinface(JSONObject jsonString){
		onStart();
		String ret = jsonString.toString().replace("-", "_");
		
		
	}
	
	public void DROinface(JSONObject jsonString){
		String ret = jsonString.toString().replace("-", "_");
		
		DROInfo info = JSON.parseObject(ret, DROInfo.class);
			
		MyLog.D("STARTS="+info.STARTS);
		
		onStop(info);
		
		
	}
	
    public String getTime(){
	    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	    String t=format.format(new Date());
	    return t;
    }
    
    public void onStart(){
    	if(isStartOn){
    		MyLog.W("警告：收到启动消息时 isStartOn="+isStartOn);
    	}else{
			String content = String.format("%s&S1,0,,%s", getHead(),getTime());
			DBmanager.getInase().insert(content);
			DBmanager.getInase().insertBackup(content); 
			isStartOn = true;
			QuickShPref.putValueObject(QuickShPref.IsStartOn, isStartOn);
    	}
    	
    }
    
    public void onStop(DROInfo info){
    	if(!isStartOn){
    		MyLog.W("警告：收到熄火消息时 isStartOn="+isStartOn);
    	}
    	
    	String content = String.format("%s&S2,%s,%sE,%sN,%d,%d,5,%s,%s,%s,%s,0", getHead(),getTime(),
    			FloatToString(QuickShPref.getFloat(QuickShPref.LON)),FloatToString(QuickShPref.getFloat(QuickShPref.LAT)),
    			info.getMILESM(),info.getFuleSmL(),info.RACLS,info.BRAKES,info.STARTS,info.STARTS);
		DBmanager.getInase().insert(content);
		DBmanager.getInase().insertBackup(content); 
		isStartOn = false;
		QuickShPref.putValueObject(QuickShPref.IsStartOn, isStartOn);
    }
    
    public String FloatToString(float lat){
		int latDu = (int) lat;
		float latFen = (float) (lat - latDu) * 60;
		int latFenInt = (int) latFen;
		String latString = String.format("%02d%02d%04d", latDu, latFenInt,
				(int) ((latFen - latFenInt) * 10000));
		return latString;
    }
   public String mIEMI;
    public String getHead(){
    	if(mIEMI == null){
    		mIEMI = QuickShPref.getString(QuickShPref.IEMI);
    	}
    	return String.format("*MG200%s,",mIEMI);
    }
}
