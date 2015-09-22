package com.obd.widget;

import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.obd.app.bean.DROInfo;
import com.obd.service.DataSyncService;
import com.obd.simpleexample.StatusInface;
import com.obd.utils.DBmanager;
import com.obd.utils.MyLog;
import com.obd.utils.PositionUtil;
import com.obd.utils.PositionUtil.Gps;
import com.obd.utils.QuickShPref;


public class GetLoaction {
	public static final boolean isDebug = true;
	
	public String mIMEI = "33333333333";
	int mGSMsingle;
	DBmanager mDBmanager;
	public static String GetLoactionHead = null;
	
	
	public GetLoaction(){
		mDBmanager = DBmanager.getInase();
		mIMEI = QuickShPref.getString(QuickShPref.IEMI);
	}
	
	public String uploadPos(BDLocation location) {
		if(location == null || location.getTime() == null){
			MyLog.E("定位数据为空,不上报");
			return null;
		}
		Date temDate = new Date();
		if(temDate == null || temDate.getYear() < 113){
			MyLog.E("当前系统时间无效,不上报");
		}
		
		String time = location.getTime();
		
//		if(time!=null) time = formatTime(time);
		
		if(time == null || time.length()<19){
			MyLog.E("定位数据有误,不上报:time="+time);
			return null;
		}
		double lon = location.getLongitude();
		double lat =	location.getLatitude();//gps.mLat; // 纬度
		
		// 如果经纬度为0，则不上报
		if (lon == Double.MIN_VALUE || lat == Double.MIN_VALUE) {
			MyLog.E("定位数据有误,不上报:"+time);
			return null;
		}
		
		if(isDebug){
			Random random = new Random();
			lon = lon+0.0001f*random.nextInt(100);
			lat = lat+0.0001f*random.nextInt(100);
		}
		
		int SatelliteNumber = location.getSatelliteNumber();
		/**
		 * location.getLocType() 的值 61 ： GPS定位结果 62 ： 扫描整合定位依据失败。此时定位结果无效。 63 ：
		 * 网络异常，没有成功向服务器发起请求。此时定位结果无效。 65 ： 定位缓存的结果。 161： 表示网络定位结果 162~167：
		 * 服务端定位失败
		 */
		byte f;
		if (location.getLocType() < 100) {
			f = 0x06; // GPS 6
		} else {
			f = 0x0F; // 基站 15
		}

		if(DataSyncService.USE_GPS_LOCATION){
			Gps gps84 = PositionUtil.gcj_To_Gps84(lat, lon);
			lat = gps84.mLat;
			lon = gps84.mLon;
		}else{
			f = 0x6f;
		}


		int lonDu = (int) lon;
		float lonFen = (float) (lon - lonDu) * 60;
		int lonFenInt = (int) lonFen;
		String lonString = String.format("%03d%02d%04d", lonDu, lonFenInt,
				(int) ((lonFen - lonFenInt) * 10000));
		String lonStartString = String.format("%03d%02d.%04d", lonDu, lonFenInt,
				(int) ((lonFen - lonFenInt) * 10000));
		
		int latDu = (int) lat;
		float latFen = (float) (lat - latDu) * 60;
		int latFenInt = (int) latFen;
		String latString = String.format("%02d%02d%04d", latDu, latFenInt,
				(int) ((latFen - latFenInt) * 10000));
		String latStartString = String.format("%02d%02d.%04d", latDu, latFenInt,
				(int) ((latFen - latFenInt) * 10000));

		String speed = String.format("%02d",
				Math.round(location.getSpeed()*3.6f / 3.704f));
		speed = StatusInface.getInstance().mSpd;
		
		int derect = Math.round(0) / 10;
		if (derect < 0) {
			derect = 0;
		}
		String direction = String.format("%02d", derect);

		StringBuffer buffer = new StringBuffer();
		buffer.append("*MG201" + mIMEI + ",BA&A"); // imei号登录就上传imei号

		buffer.append(time.subSequence(11, 13));
		buffer.append(time.subSequence(14, 16));
		buffer.append(time.subSequence(17, 19));
		buffer.append(latString);
		buffer.append(lonString);
		buffer.append((char)f);
		buffer.append(speed);
		buffer.append(direction);
		buffer.append(time.subSequence(8, 10));
		buffer.append(time.subSequence(5, 7));
		buffer.append(time.subSequence(2, 4));
		if(SatelliteNumber<0)
			SatelliteNumber = 0;
		buffer.append("&B0000000000");

		GetLoactionHead = buffer.toString();

		if(StatusInface.getInstance().getStartS1() != null){
			buffer.append("&S1,0,,"+StatusInface.getInstance().getTime());
			DBmanager.getInase().insertBackup(buffer.toString()); 
			QuickShPref.putValueObject(QuickShPref.StartTme, StatusInface.getInstance().getTime());
			QuickShPref.putValueObject(QuickShPref.StartLat, latStartString);
			QuickShPref.putValueObject(QuickShPref.StartLon, lonStartString);
		}else{
			DROInfo mDROInfo = StatusInface.getInstance().getDROInfo();
			if(mDROInfo != null){
				String startLon = QuickShPref.getString(QuickShPref.StartLon);
				String startLat = QuickShPref.getString(QuickShPref.StartLat);
				String startTime = QuickShPref.getString(QuickShPref.StartTme);
				
				if(strNotEmpty(startTime) && strNotEmpty(startLat) && strNotEmpty(startLon)){
					buffer.append(String.format("&S2,%s,%sE,%sN,%d,%d,5,%s,%s,%s,%s,0", startTime,
							startLon,startLat,mDROInfo.getMILESM(),mDROInfo.getFuleSmL(),
							mDROInfo.RACLS,mDROInfo.BRAKES,mDROInfo.STARTS,mDROInfo.STARTS));
					DBmanager.getInase().insertBackupNote(buffer.toString(),mDROInfo.note); 
					QuickShPref.putValueObject(QuickShPref.StartTme, "");
					QuickShPref.putValueObject(QuickShPref.StartLat, "");
					QuickShPref.putValueObject(QuickShPref.StartLon, "");					
				}else{
					MyLog.E("startTime="+startTime+",startLat="+startLat+",startLon="+startLon);
				}
			}else{
				buffer.append("&O"+SatelliteNumber);
				buffer.append("&N"+mGSMsingle);				
			}
		}

		//buffer.append("&S2,20150318111758,11356.1966E,2233.1303N,13300,6100,5,6,7,1200,900,5");
		buffer.append('#');
		
		mDBmanager.insert(buffer.toString());
		
		sync();
		
		return buffer.toString();
	}
	public boolean strNotEmpty(String str){
		if(str !=null && str.length()>0){
			return true;
		}else{
			return false;
		}
	}
	public void sync(){
		try {
			Runtime.getRuntime().exec("sync");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String genHead(){
		return "*MG200" + mIMEI + ",BA%s#";
	}
	public void setGSMsingle(int single){
		mGSMsingle = single;
	}
	public void  uploadGSMsingle(int single){
		String sig = String.format(genHead(), "&N"+single);
		mDBmanager.insert(sig);
	}
	public void  uploadBattery(int bat){
		String sig = String.format(genHead(), "&M"+800);
		mDBmanager.insert(sig);
	}
	public void  uploadGPSnum(int num){
		String sig = String.format(genHead(), "&O"+num);
		mDBmanager.insert(sig);
	}
	/*
	 * 将百度定位的时间转换成标准格式2013-09-01 09:05:02
	 */
	public String formatTime(String time){
			Pattern pattern = Pattern.compile("\\s*(\\d+)-(\\d+)-(\\d+)\\s+(\\d+):(\\d+):(\\d+)\\s*");
			Matcher matcher = pattern.matcher(time);
			String ret = null;
			int y,m,d,h,min,s;
			
			if(matcher.find()){ 
				try {
					y =  Integer.parseInt(matcher.group(1));
					m =  Integer.parseInt(matcher.group(2));
					d =  Integer.parseInt(matcher.group(3));
					h =  Integer.parseInt(matcher.group(4));
					min =  Integer.parseInt(matcher.group(5));
					s =  Integer.parseInt(matcher.group(6)) ;
					
					if(y<2015)
						return null;					
					
					if(isDebug){
						return getTime();
					}
				} catch (Exception e) {
					// TODO: handle exception
					return null;
				}
				ret = String.format("%04d-%02d-%02d %02d:%02d:%02d", y,m,d,h,min,s);
				//Log.d("time", ret);
			}
			return ret;
	}
    public String getTime(){
    	Date date = new Date();
    	
    	if(date.getYear()<114){
    		return null;
    	}
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String t=format.format(new Date());

	    return t;
    }
}
