package com.goodweather.activity;

import com.iflytek.cloud.SpeechUtility;

import android.app.Application;

public class MyApplication extends Application {

	private static final String WEATHERINFO = "WeatherInfo";// shareadPerference
															// 全局key
	private static final String ONE = "First";// 判断软件是否第一次使用的key
	private static final String CITYNAME = "CityName";// 获取城市名称
	private static final String LOCATIONCITYNAME = "LocationCityName";

	private static String SPEECHID = "5718917b";
	// 数据保存路径
	private static final String FilePath = "/data/data/com.goodweather.activity/files/WeatherInfo.txt";

	private static final String FileName = "WeatherInfo.txt";

	// 和风数据个人认证key
	private static final String httpUrlKey = "0ac6804b137f4f2db569511432afb4fb";
	/**
	 * 和风天气请求URL 示例：https://api.heweather.com/x3/weather?city=城市&key=你的认证key
	 * city 城市名称、支持中英文,不区分大小写和空格,城市和国家之间用英文逗号分割
	 */
	public static final String Url = "https://api.heweather.com/x3/weather?city=";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		speechKeyInit();
		super.onCreate();

	}

	private void speechKeyInit() {
		// 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用半角“,”分隔。
		// 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

		// 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

		SpeechUtility.createUtility(MyApplication.this, "appid=" + SPEECHID);
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

	public static String getLocationcityname() {
		return LOCATIONCITYNAME;
	}

}
