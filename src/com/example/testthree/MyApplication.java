package com.example.testthree;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyApplication extends Application {
	
	private static final String WEATHERINFO = "WeatherInfo";//shareadPerference 全局key
	private static final String ONE = "First";//判断软件是否第一次使用的key
	private static final String CITYNAME = "CityName";//获取城市名称
	
	//数据保存路径
	private static final String FilePath = "/data/data/com.example.testthree/files/WeatherInfo.txt";
	
	private static final String FileName = "WeatherInfo.txt";
	
	//和风数据个人认证key
	private static final String httpUrlKey = "0ac6804b137f4f2db569511432afb4fb";
	/**
	 * 和风天气请求URL 
	 * 示例：https://api.heweather.com/x3/weather?city=城市&key=你的认证key
	 * city	城市名称、支持中英文,不区分大小写和空格,城市和国家之间用英文逗号分割
	 */
	public static final String Url = "https://api.heweather.com/x3/weather?city=";
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	
	public static String getFilepath() {
		return FilePath;
	}

	public static String getHttpurlkey() {
		return httpUrlKey;
	}

	public static String getWeatherinfo() {
		return WEATHERINFO;
	}

	public static String getOne() {
		return ONE;
	}

	public static String getCityname() {
		return CITYNAME;
	}

	public static String getFilename() {
		return FileName;
	}

	public static String getUrl() {
		return Url;
	}
	
}
