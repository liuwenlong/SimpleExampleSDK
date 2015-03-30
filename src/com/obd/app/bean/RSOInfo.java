package com.obd.app.bean;

public class RSOInfo {
	public String MPH;
	public String TIMES;
	public String SPD;
	public String RPM;
	
	public void setMPH(String arg){
		MPH = arg;
	}
	public void setTIMES(String arg){
		TIMES = arg;
	}
	public void setSPD(String arg){
		SPD = arg;
	}
	public void setRPM(String arg){
		RPM = arg;
	}
	public String getDisplay(){
		String ret = null;
		ret = String.format("&R%s,%s,%s,%s,&F%s", 0,0,0,TIMES,SPD);
		return ret;
	}
}
