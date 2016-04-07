package com.goodweather.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.goodweather.activity.MyApplication;

import android.widget.Toast;

public class ReadWeatherTXTInfo {

	public ReadWeatherTXTInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean isFileExist(){
		File mFile = new File(MyApplication.getFilepath());
		if (!mFile.exists()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String readTXT() throws IOException{
		BufferedReader mBufferedReader = null;
		String result = null;
		String strRead = null;
		StringBuffer mStringBuffer = new StringBuffer();
		
		
		if (isFileExist()) {
			File file = new File(MyApplication.getFilepath());
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
