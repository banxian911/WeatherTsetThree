package com.goodweather.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.goodweather.activity.MyApplication;

import android.util.Log;
import android.widget.Toast;

public class ReadWeatherTXTInfo {
	
	private static final String TAG = "ReadWeatherTXTInfo";

	public ReadWeatherTXTInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean isFileExist(String filePath){
		File mFile = new File(filePath);
		if (!mFile.exists()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static Boolean isDeleteFolder(String filePath){
		File mFile = new File(filePath);
		if (isFileExist(filePath)) {
			return mFile.delete();
		} else {
			Log.d(TAG, "文件不存在---！");
			return true;
		}
	}
	
	public static String readTXT() throws IOException{
		BufferedReader mBufferedReader = null;
		String result = null;
		String strRead = null;
		StringBuffer mStringBuffer = new StringBuffer();
		String filePath = MyApplication.getWeatherinfotxt();
		
		if (isFileExist(filePath)) {
			File file = new File(filePath);
			FileInputStream inputStream = new FileInputStream(file);
			mBufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((strRead = mBufferedReader.readLine()) != null ) {
				mStringBuffer.append(strRead);
				mStringBuffer.append("\r\n");
			}
			mBufferedReader.close();
			result = mStringBuffer.toString();
		} else {
			result = null;
		}
		
		return result;
		
	}
}
