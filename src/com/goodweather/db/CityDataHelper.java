package com.goodweather.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.goodweather.mod.City;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class CityDataHelper {
	
	public static String DATABASES_DBPATH;//数据库文件路径
	public static String DATABASES_NAME="city.db";//数据库文件名
	public static final String CITY_TABLE_NAME = "city";// 城市表名
	private static CityDataHelper dataHelper;
	private static SQLiteDatabase database;
	private CityDataHelper(Context context){
		DATABASES_DBPATH = "/data/data/"+context.getPackageName()+"/databases/";
	}

	public static CityDataHelper getInstance(Context context){
		if (dataHelper == null) {
			dataHelper = new CityDataHelper(context);
		}
		return dataHelper;
	}
	
//	public static String getDBFilePath(Context context) {
//		return "/data" + Environment.getDataDirectory().getAbsolutePath()
//				+ File.separator + context.getPackageName() + File.separator
//				+ "databases" + File.separator + DATABASES_NAME;
//	}
	
	
	
	/**
	 * 复制数据库文件到指定目录
	 * @param inStream
     * @param fileNme 文件名
     * @param newPath 要复制到的文件夹路径
	 */
	public void copyDBFile(InputStream inputStream,String fileName,String newPath){
		
		//int bytesum = 0;
        //int byteread = 0;
		File file = new File(newPath);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			File newFile = new File(newPath + File.separator + fileName);
			if(newFile.exists()){
	            newFile.delete();
	            newFile.createNewFile();
	        }
	        FileOutputStream fs = new FileOutputStream(newFile);
//	        byte[] buffer = new byte[1024 * 2];
//	        int length;
//	        while ((byteread = inputStream.read(buffer)) != -1) {
//	            bytesum += byteread; //字节数 文件大小
//	            System.out.println(bytesum);
//	            fs.write(buffer, 0, byteread);
//	        }
	        byte[] buffer = new byte[inputStream.available()];
	        inputStream.read(buffer);
	        fs.write(buffer);
	        inputStream.close();
	        fs.close();
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println("复制文件操作出错");
	         e.printStackTrace();
		}
		
	}
	
	/**
     * 打开数据库文件
     * @return
     */
    public SQLiteDatabase openDataBase(){
        database = SQLiteDatabase.openOrCreateDatabase(
        		DATABASES_DBPATH+DATABASES_NAME, null);
        return database;
    }
    
    /**
     * 关闭数据库
     */
    public void close(){
    	if (database != null && database.isOpen()) {
			database.close();
		}
    }

	/**
	 * 获取所有城市数组
	 * 
	 * @param 
	 * @return
	 */
	public static List<City> getAllCities(SQLiteDatabase db) {
		List<City> list = new ArrayList<City>();
		String sqlStr = "SELECT * from " + CITY_TABLE_NAME;
		Cursor c = db.rawQuery(sqlStr, null);
		if (c == null || c.getCount() == 0)
			return list;
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("province"));
			String city = c.getString(c.getColumnIndex("city"));
			String name = c.getString(c.getColumnIndex("name"));
			String pinyin = c.getString(c.getColumnIndex("pinyin"));
			String py = c.getString(c.getColumnIndex("py"));
			String phoneCode = c.getString(c.getColumnIndex("phoneCode"));
			String areaCode = c.getString(c.getColumnIndex("areaCode"));
			String postID = c.getString(c.getColumnIndex("postID"));
			City item = new City(province, city, name, pinyin, py, phoneCode,
					areaCode, postID);
			list.add(item);
		}
		c.close();
		return list;
	}
}
