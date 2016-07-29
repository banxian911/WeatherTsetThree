package com.goodweather.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import com.goodweather.activity.MyApplication;
import android.util.Log;

public class HttpUntil {

	private static String TAG = "HttpUntil";
	
	public static void DownloadUrl(String cityname) throws Exception {
		String url = MyApplication.getUrl() + cityname + "&key=" + MyApplication.getHttpurlkey();
		Log.d(TAG, "url --->" + url);
		BufferedReader mBufferedReader = null;
		String result = null;
		String strRead = null;
		StringBuffer mStringBuffer = new StringBuffer();
		InputStream stream = null;
		
		URL murl = new URL(url);//创建一个URL对象
		//利用HttpsURLConnection对象从网络中获取网页数据
		HttpsURLConnection connection = (HttpsURLConnection) murl.openConnection();
		connection.setReadTimeout(10000);//设置连接超时
		connection.setRequestMethod("GET");//设置使用GET的方式发送
		int code = connection.getResponseCode();
		//对响应码进行判断
		if (code == 200) {
			stream = connection.getInputStream();
		} else {
			//Toast.makeText(get, "", )
		}
		mBufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

		while ((strRead = mBufferedReader.readLine()) != null) {
			mStringBuffer.append(strRead);
			mStringBuffer.append("\r\n");
		}
		mBufferedReader.close();
		result = mStringBuffer.toString();
		saveDownloadInfo(MyApplication.getFilepath(),MyApplication.getFilename(),result);	
	}
	
	/**
	 * 保存获取的天气数据
	 * 
	 * @param weatherinfo
	 * @throws Exception
	 */
	public static void saveDownloadInfo(String filePath,String fileName ,String fileContent) throws Exception {
		if (fileContent != null) {
			File file = new File(filePath,fileName); 
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(fileContent.getBytes());
			outputStream.close();
		} else {

		}

	}
}
