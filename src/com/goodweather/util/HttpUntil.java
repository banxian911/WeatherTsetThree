package com.goodweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.goodweather.activity.MyApplication;

import android.util.Log;

public class HttpUntil {

	private static String TAG = "HttpUntil";
	
	public static String DownloadUrl(String cityname) throws IOException {
		String url = MyApplication.getUrl() + cityname + "&key=" + MyApplication.getHttpurlkey();
		Log.d(TAG, "url --->" + url);
		BufferedReader mBufferedReader = null;
		String result = null;
		String strRead = null;
		StringBuffer mStringBuffer = new StringBuffer();
		InputStream stream = null;

		URL murl = new URL(url);
		HttpsURLConnection connection = (HttpsURLConnection) murl.openConnection();
		connection.setReadTimeout(10000);
		connection.setRequestMethod("GET");
		int code = connection.getResponseCode();
		if (code == 200) {
			stream = connection.getInputStream();
		} else {

		}
		mBufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

		while ((strRead = mBufferedReader.readLine()) != null) {
			mStringBuffer.append(strRead);
			mStringBuffer.append("\r\n");
		}
		mBufferedReader.close();
		result = mStringBuffer.toString();

		return result;
	}

}
