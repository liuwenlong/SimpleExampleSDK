package com.obd.app.bean;

import com.obd.utils.DBmanager;
import com.obd.utils.MyLog;
import com.obd.utils.QuickShPref;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class DROInfo {

	public String MAF;
	public String AVGSPD;
	public String STARTS;
	public String MILE_T;
	public String MAXRPM;
	public String POWER;
	public String RACLS;
	public String FUELS;
	public String FUEL_T;
	public String MAXACL;
	public String TIMES;
	public String MAXSPD;
	public String BRAKES;
	public String MINRPM;
	public String MILES;
	public void setMILES(String arg){
		MILES = arg;
	}	
	public void setMINRPM(String arg){
		MINRPM = arg;
	}
	public void setBRAKES(String arg){
		BRAKES = arg;
	}	
	public void setMAXSPD(String arg){
		MAXSPD = arg;
	}	
	public void setTIMES(String arg){
		TIMES = arg;
	}	
	public void setMAXACL(String arg){
		MAXACL = arg;
	}	
	public void setFUEL_T(String arg){
		FUEL_T = arg;
	}	
	public void setFUELS(String arg){
		FUELS = arg;
	}	
	public void setRACLS(String arg){
		RACLS = arg;
	}		
	public void setPOWER(String arg){
		POWER = arg;
	}		
	public void setMAXRPM(String arg){
		MAXRPM = arg;
	}	
	public void setMILE_T(String arg){
		MILE_T = arg;
	}
	public void setSTARTS(String arg){
		STARTS = arg;
	}
	public void setAVGSPD(String arg){
		AVGSPD = arg;
	}
	public void setMAF(String arg){
		MAF = arg;
	}
	
	public int getMILESM(){
		int ret = 0;
		try {
			float f = Float.parseFloat(MILES);
			ret = (int) (f*1000);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.W("累计里程km转换成M失败 MILES="+MILES);
		}
		return ret;
	}
	
	public int getFuleSmL(){
		int ret = 0;
		try {
			float f = Float.parseFloat(FUELS);
			ret = (int) (f*1000);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.W("累计油耗L转换成mL失败 FUELS="+FUELS);
		}
		return ret;
	}
}
