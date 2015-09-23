package com.obd.simpleexample;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.obd.app.bean.DROInfo;
import com.obd.app.bean.RSOInfo;
import com.obd.utils.DBmanager;
import com.obd.utils.MyLog;
import com.obd.utils.QuickShPref;
import com.obd.widget.GetLoaction;

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
			onStart(true);
		}else if(STOP.equalsIgnoreCase(result)){
			
		}
	}

	public String mSpd = "00";
	public long curTime = 0;
	public void RSOinface(JSONObject jsonString){
		long time = System.currentTimeMillis();
		if( (time - curTime) >= 60*1000 ){
			curTime = time;
			
			String ret = jsonString.toString().replace("-", "_");
			RSOInfo info = JSON.parseObject(ret, RSOInfo.class);
			
			String content = String.format("%s%s#", getHead(),info.getQ());
			DBmanager.getInase().insert(content);
			
			content = String.format("%s%s#", getHead(),info.getR());
			DBmanager.getInase().insert(content);
			
			mSpd = info.getSpd();
		}
	}
	
	public void DROinface(JSONObject jsonString){
		String ret = jsonString.toString().replace("-", "_");
		
		DROInfo info = JSON.parseObject(ret, DROInfo.class);
		info.note = jsonString.toString();
		MyLog.D("STARTS="+info.STARTS);
		
		onStop(info);

	}
	
    public String getTime(){
    	Date date = new Date();
    	MyLog.D("getYear="+date.getYear());
    	if(date.getYear()<113){
    		String time = QuickShPref.getString(QuickShPref.Time);
    		if(time == null)
    			time = null;
    		return time;
    	}
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	    String t=format.format(new Date());
	    QuickShPref.putValueObject(QuickShPref.Time, t);
	    return t;
    }
    
    synchronized public void onStart(boolean force){
    	MyLog.E("onStart enter"+isStartOn+",force="+force);	
    	if(isStartOn){
    		MyLog.W("警告：收到启动消息时1 isStartOn="+isStartOn+",force="+force);	
    		if(force){
    			doOnstart();
    		}
    	}else{
    		
    		doOnstart();
    	}
    }
    
    public boolean hasEvent(){
    	boolean ret = false;
    	if(startS1!=null || mDROInfo!=null){
    		ret = true;
    	}
    	return ret;
    }
    
    public String startS1 = null; 
    public String getStartS1(){
    	String ret = startS1;
    	startS1 = null;
    	return ret;
    }
    public void doOnstart(){
		String content = String.format("%s&S1,0,,%s#", getShortHead(),getTime());
		startS1 = content;
//		DBmanager.getInase().insert(content);
//		DBmanager.getInase().insertBackup(content); 
		isStartOn = true;
		QuickShPref.putValueObject(QuickShPref.IsStartOn, isStartOn);
    }
    
    DROInfo mDROInfo;
    public DROInfo getDROInfo(){
    	DROInfo info = mDROInfo;
    	mDROInfo = null;
    	return info;
    }
    public void onStop(DROInfo info){
    	if(!isStartOn){
    		MyLog.W("警告：收到熄火消息时 isStartOn="+isStartOn);
    	}
    	mDROInfo = info;
//    	String content = String.format("%s&S2,%s,%sE,%sN,%d,%d,5,%s,%s,%s,%s,0#", getShortHead(),getTime(),
//    			FloatToString(QuickShPref.getFloat(QuickShPref.LON)),FloatToString(QuickShPref.getFloat(QuickShPref.LAT)),
//    			info.getMILESM(),info.getFuleSmL(),info.RACLS,info.BRAKES,info.STARTS,info.STARTS);
//		DBmanager.getInase().insert(content);
//		DBmanager.getInase().insertBackupNote(content, info.note);
		isStartOn = false;
		QuickShPref.putValueObject(QuickShPref.IsStartOn, isStartOn);
		mSpd = "00";
    }
    
    public String FloatToString(float lat){
		int latDu = (int) lat;
		float latFen = (float) (lat - latDu) * 60;
		int latFenInt = (int) latFen;
		MyLog.D("FloatToString lat="+lat);
		String latString = String.format("%02d%02d%04d", latDu, latFenInt,
				(int) ((latFen - latFenInt) * 10000));
		MyLog.D("FloatToString latString="+latString);
		return latString;
    }
   public String mIEMI;
   
   public String getShortHead(){
	   	if(mIEMI == null){
			mIEMI = QuickShPref.getString(QuickShPref.IEMI);
		}	   
	    return String.format("*MG201%s,BA",mIEMI);
   }
   
    public String getHead(){
    	if(GetLoaction.GetLoactionHead == null){
	    	if(mIEMI == null){
	    		mIEMI = QuickShPref.getString(QuickShPref.IEMI);
	    	}
	    	return String.format("*MG201%s,BA",mIEMI);
		 }else{
	    	return GetLoaction.GetLoactionHead;
	    }
    }
}
