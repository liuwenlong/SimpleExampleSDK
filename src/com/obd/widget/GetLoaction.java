package com.obd.widget;

import java.nio.Buffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.obd.utils.DBmanager;
import com.obd.utils.PositionUtil;
import com.obd.utils.PositionUtil.Gps;
import com.obd.utils.QuickShPref;


public class GetLoaction {
	String mIMEI = "33333333333";
	DBmanager mDBmanager;
	
	public GetLoaction(){
		mDBmanager = DBmanager.getInase();
		mIMEI = QuickShPref.getString(QuickShPref.IEMI);
	}
	
	public String uploadPos(BDLocation location) {
		
		String time = location.getTime();
		time = formatTime(time);
		if(time == null || time.length()<19){
			if(time!=null)
				Log.d("tag", "定位数据有误,不上报:"+time);
			return null;
		}
		Gps gps = PositionUtil.bd09_To_Gps84(location.getLatitude(), location.getLongitude());
		
		double lon = gps.mLon;//location.getLongitude(); // 经度
		double lat =	gps.mLat; // 纬度

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

			// 如果经纬度为0，则不上报
			if (lon == 0 || lat == 0) {
				return null;
			}


		int lonDu = (int) lon;
		float lonFen = (float) (lon - lonDu) * 60;
		int lonFenInt = (int) lonFen;
		String lonString = String.format("%03d%02d%04d", lonDu, lonFenInt,
				(int) ((lonFen - lonFenInt) * 10000));

		int latDu = (int) lat;
		float latFen = (float) (lat - latDu) * 60;
		int latFenInt = (int) latFen;
		String latString = String.format("%02d%02d%04d", latDu, latFenInt,
				(int) ((latFen - latFenInt) * 10000));

		String speed = String.format("%02d",
				Math.round(location.getSpeed() * 3.6f / 3.704f));
		int derect = Math.round(0) / 10;
		if (derect < 0) {
			derect = 0;
		}
		String direction = String.format("%02d", derect);

		StringBuffer buffer = new StringBuffer();
		buffer.append("*HQ200" + mIMEI + ",BA&A"); // imei号登录就上传imei号

		buffer.append(time.subSequence(11, 13));
		buffer.append(time.subSequence(14, 16));
		buffer.append(time.subSequence(17, 19));
		buffer.append(latString);
		buffer.append(lonString);
		buffer.append((char) f);
		buffer.append(speed);
		buffer.append(direction);
		buffer.append(time.subSequence(8, 10));
		buffer.append(time.subSequence(5, 7));
		buffer.append(time.subSequence(2, 4));


		buffer.append('#');
		
		mDBmanager.insert(buffer.toString());
		return buffer.toString();
	}
	
	/*
	 * 将百度定位的时间转换成标准格式2013-09-01 09:05:02
	 */
	private String formatTime(String time){
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
					s =  Integer.parseInt(matcher.group(6));
				} catch (Exception e) {
					// TODO: handle exception
					return null;
				}
				ret = String.format("%04d-%02d-%02d %02d:%02d:%02d", y,m,d,h,min,s);
				Log.d("time", ret);
			}
			return ret;
	}
}
