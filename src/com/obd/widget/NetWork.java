package com.obd.widget;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;

import com.obd.utils.DBmanager;
import com.obd.utils.MyLog;
import com.obd.utils.DBmanager.DbItem;

public class NetWork {
	private final String Server_ip = "183.62.138.9";
	private final int Server_port = 2233;
	
	private Socket mSocket;
	private InputStream mSocketReader;
	private OutputStream mSocketWriter;
	private DBmanager mDBmanager;
	private Thread mMainThread;
	private int sendMsgCount = 0;
	private int recvMsgCount = 0;
	private int msgCountMaxDiff = 3;
	public NetWork(){
		mDBmanager = DBmanager.getInase();
	}
	
	public void interruptMain(){
		if(mMainThread!=null){
			mMainThread.interrupt();
		}
	}
	
	public void start(){
		mMainThread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				mainRun();
			}
		};
		mMainThread.start();
	}
	
	public void mainRun(){
		reStartSocket();
		while(true){
			DbItem item = mDBmanager.getLastContent();
			
			if(item != null){
				
				if(sendMsg(item.content)){
					sendMsgCount++;
					mDBmanager.deleteItem(item.id);
					sleep(200);
				}else{
					sleep(30*1000);
					reStartSocket();
				}
				
			}else{
				MyLog.D("没有数据,等待30秒");
				sleep(30*1000);
				
				int diff = sendMsgCount - recvMsgCount;
				MyLog.D("检测发送数量:sendMsgCount="+sendMsgCount+",recvMsgCount="+recvMsgCount+",diff="+diff+",msgCountMaxDiff="+msgCountMaxDiff);
				if(diff >= msgCountMaxDiff){
					reStartSocket();
					sendMsgCount = 0;
					recvMsgCount = 0;
				}
			}
		
		}
	}
	

	
	public boolean sendMsg(String msg){
		boolean ret = false;

		try {
			if(mSocketWriter!=null){
				MyLog.D("尝试发送数据");
				mSocketWriter.write(msg.getBytes());
				MyLog.I("发送数据成功:"+msg);
				
				ret = true;
			}else{
				MyLog.E("尝试发送数据失败,mSocketWriter为空");
			}
		} catch (IOException e) {
			e.printStackTrace();	
			MyLog.E("发送数据失败,IOException");
		}
		
		return ret;
	}
	
	public void reStartSocket(){
		MyLog.W("Socket重连");
		closeNet();
		openNet();
	}
	
	public void closeNet(){
		try {
				if (mSocketReader != null) {
					mSocketReader.close();
					mSocketReader = null;
				}
				if (mSocketWriter != null) {
					mSocketWriter.close();
					mSocketWriter = null;
				}
				if (mSocket != null) {
					mSocket.close();
					mSocket = null;
				}
		} catch (IOException e) {
				e.printStackTrace();
		}
		MyLog.W("Socket关闭");
	}
	
	public void openNet(){
		try {
			mSocket = new Socket(Server_ip, Server_port);
			mSocket.setKeepAlive(true);
			mSocket.setTcpNoDelay(true);
			//mSocket.setSoTimeout(5000);// 设置超时时间(毫秒)

			mSocketReader = mSocket.getInputStream();
			mSocketWriter = mSocket.getOutputStream();	
			
			startRead();
			MyLog.D("socket连接成功");
		} catch (Exception e) {
			MyLog.D("创建Socket连接失败");
			closeNet();
		}
	}
	
	public void sleep(int dur){
		
		try {
			Thread.sleep(dur);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
	private void startRead(){
		  MyLog.D("startRead");
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				byte[] buffer = new byte[512];  
				int temp = 0;
	            try {
					while ((mSocketReader!=null) && ((temp = mSocketReader.read(buffer)) != -1)) {
					    String str = new String(buffer, 0, temp);
					    MyLog.D("str="+str);
					    if(str!=null){
					    	int count = getSubString(str, "*MG20YBA#");
						    recvMsgCount = recvMsgCount + count;
					    	MyLog.I("收到数据发送成功的回应:count="+count);
					    }
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            MyLog.W("startRead EDN");
			}
		}.start();
	}
	
	 public static int  getSubString(String str,String key){
	        int count = 0;
	        int index = 0;
	        while((index=str.indexOf(key,index)) != -1){
	            index = index+key.length();
	            count++;
	        }
	        return count;
	    }
}
