package com.obd.app.bean;

import com.obd.utils.MyLog;

public class RSOInfo {
	public String MPH;
	public String MILE_T;
	public String VBAT;
	public String ECT;
	public String TIMES;
	public String SPD;
	public String RPM;
	public String LOD;
	public void setLOD(String arg){
		LOD = arg;
	}
	public void setRPM(String arg){
		RPM = arg;
	}
	public void setSPD(String arg){
		SPD = arg;
	}		
	public void setTIMES(String arg){
		TIMES = arg;
	}		
	public void setECT(String arg){
		ECT = arg;
	}	
	public void setVBAT(String arg){
		VBAT = arg;
	}	
	public void setMPH(String arg){
		MPH = arg;
	}
	public void setMILE_T(String arg){
		MILE_T = arg;
	}	
	public int getMPH(){
		int mph=0;
		try{
			mph = (int)(Float.parseFloat(MPH)*1000);
		}catch (Exception e) {
			// TODO: handle exception
		}
		MyLog.D("mph="+mph);
		return mph;		
	}
	public int getMILE_T(){
		int miles = 0;
		try{
			miles = Integer.parseInt(MILE_T)*100;
		}catch (Exception e) {
			// TODO: handle exception
		}

		return miles;		
	}
	public String getR(){
		String ret = null;
		ret = String.format("&R,%s,%s,%d,%d", getMILE_T(),0,getMPH(),getTimes());
		return ret;
	}
	
	
	public int getTimes(){
		int times = 0;
		try{
			times = Integer.parseInt(TIMES);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return times;
	}
	public String getQ(){
		String ret = null;
		int ect=0,vbat=0,rmp=0,spd=0;
		try{
			ect = Integer.parseInt(ECT) + 40;
			vbat = (int)(Float.parseFloat(VBAT)*1000);
			rmp = (int)(Float.parseFloat(RPM)*4);
			spd = Integer.parseInt(SPD);
		}catch (Exception e) {}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("&Q");
		//buffer.append(String.format("0x050x%02x",ect));
		buffer.append((char)(0x05));
		buffer.append((char)(ect));
		
		//buffer.append(String.format("0x420x%02x0x%02x",vbat/256,vbat%256));
		buffer.append((char)(0x42));
		buffer.append((char)(vbat/256));	
		buffer.append((char)(vbat%256));	
		
		//buffer.append(String.format("0x0C0x%02x0x%02x",rmp/256,rmp%256));
		buffer.append((char)(0x0C));
		buffer.append((char)(rmp/256));	
		buffer.append((char)(rmp%256));		
		
		//buffer.append(String.format("0x0D0x%02x",spd));
		buffer.append((char)(0x0D));
		buffer.append((char)(spd));
		
		ret = buffer.toString();
		
		MyLog.D("ret = "+ret+",len = "+ret.length());
		return ret;
	}
	
	/*
	 * 获取速度，单位两节
	 */
	public String getSpd(){
		String ret = "0";
		float spd = 0.0f;
		int spd_j = 0;
		try {
			spd = Float.parseFloat(SPD);
			spd_j = (int)(spd*0.54f/2);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ret = String.format("%02d", spd_j);
		MyLog.I("getSpd="+ret);
		return ret;
	}
}
