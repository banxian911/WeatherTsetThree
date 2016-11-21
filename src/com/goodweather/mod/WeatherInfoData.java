package com.goodweather.mod;

import java.io.IOException;

import com.goodweather.debug.LogDebug;
import com.goodweather.mod.WeatherInfo.HeWeatherBean;
import com.goodweather.utils.ReadWeatherTXTInfo;

public class WeatherInfoData {
	
	//private static String TAG = "WeatherInfoData";
	private static final LogDebug.Tag TAG = new LogDebug.Tag("WeatherInfoData");

	/**
	 * 数据
	 */
	private static String info = null;
	static HeWeatherBean mBean;
	
	
	public static HeWeatherBean initHeWeatherData() {
		try {
			info = ReadWeatherTXTInfo.readTXT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogDebug.d(TAG, "info.length---->" + info.length());
		if (info != null && info.length()>99) {
			WeatherInfo mInfo = WeatherInfo.objectFromData(info);
			for (int i = 0; i < mInfo.getHeWeather().size(); i++) {
				mBean = mInfo.getHeWeather().get(i);
			}
		} else {
		//	Toast.makeText(, "info null", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		//TextView myTextView = (TextView) findViewById(R.id.text1);
		//myTextView.setText(mBean.getAqi().getCity().getAqi().toString());

		LogDebug.d(TAG, "mCityBasicInfo --->" + mBean);
		return mBean;
	}
}
