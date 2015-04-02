package com.obd.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBmanager {
	
    public static final String DB_NAME = "city_cn.s5db";
    public static final String PACKAGE_NAME = "com.mapgoo.snowleopard.business";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME;
    public static final String DATABASE_TABLE = "data";
    private static final String DATABASE_CREATE ="CREATE TABLE IF NOT EXISTS data( _id integer primary key autoincrement,content TEXT,note TEXT,remark TEXT)";
    public static final String DATABACKUP_TABLE = "backup";
    private static final String DATABACKUP_CREATE ="CREATE TABLE IF NOT EXISTS backup( _id integer primary key autoincrement,content TEXT,note TEXT,remark TEXT)";
	private String COLUM_CONTENT = "content";
	private String COLUM_REMARK = "remark";
	private String COLUM_NOTE = "note";
	private String COLUM_ID = "_id"; 
	
    private SQLiteDatabase database;
    private static DBmanager mDBManager;
    private Context mContext;
    
    public static void init(Context context){
    	if(mDBManager == null)
    		mDBManager = new DBmanager(context);
    }
    
    public static DBmanager getInase(){
    	return mDBManager;
    }
    
    private DBmanager(Context context) {
        this.mContext = context;
        openDatabase();
    }
    public void openDatabase() {
    	database = mContext.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    	database.execSQL(DATABASE_CREATE);
    	database.execSQL(DATABACKUP_CREATE);
    }
    
    public void insert(String content){
    	ContentValues values = new ContentValues();
    	values.put(COLUM_CONTENT, content);
    	database.insert(DATABASE_TABLE, null, values);
    	MyLog.D("insert " + content);
    }
    public void insertBackup(String content){
    	ContentValues values = new ContentValues();
    	values.put(COLUM_CONTENT, content);
    	values.put(COLUM_REMARK, getTime());
    	database.insert(DATABACKUP_TABLE, null, values);
    	MyLog.I("backup insert " + content);
    }
    public void insertBackupNote(String content,String note){
    	ContentValues values = new ContentValues();
    	values.put(COLUM_CONTENT, content);
    	values.put(COLUM_NOTE, note);
    	values.put(COLUM_REMARK, getTime());
    	
    	database.insert(DATABACKUP_TABLE, null, values);
    	MyLog.I("backup insert " + content);
    }
    public String getTime(){
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	    String t=format.format(new Date());
	    return t;
    }
    public DbItem getLastContent(){
    	DbItem item = null;
        String[] columns = new String[]{COLUM_ID,COLUM_CONTENT};
        Cursor cursor = database.query(DATABASE_TABLE, columns, null, null, null, null, "_id desc limit 1");
    	if(cursor!=null && cursor.getCount()>0){
    		cursor.moveToFirst();
    		item = new DbItem();
    		item.content = cursor.getString(cursor.getColumnIndex(COLUM_CONTENT)); 
    		item.id = cursor.getInt(cursor.getColumnIndex(COLUM_ID)); 
    	}
        return item;
    }
    
    public void deleteItem(int id){
    	database.delete(DATABASE_TABLE, COLUM_ID + "=" +id, null);
    }
    
    public static class DbItem{
    	public int id;
    	public String content;
    }
}
