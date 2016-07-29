package com.goodweather.utils.ui;

import com.goodweather.activity.R;

public class WeatherImg {

	/**
	 * 根据天气信息设置天气图片
	 * 
	 * @param weather
	 *            天气信息
	 * @return 对应的天气图片id
	 */
	public static int getWeatherImg(String weather) {
		int img = 0;
		if (weather.contains("转")) {
			weather = weather.substring(0, weather.indexOf("转"));
		}
		if (weather.contains("晴")) {
			img = R.drawable.qing00;
		} else if (weather.contains("多云")) {
			img = R.drawable.duoyun01;
		} else if (weather.contains("阴")) {
			img = R.drawable.yin02;
		} else if (weather.contains("阵雨")) {
			img = R.drawable.zhenyu03;
		} else if (weather.contains("雷阵雨")) {
			img = R.drawable.leizhenyu04;
		} else if (weather.contains("雷阵雨伴有冰雹")) {
			img = R.drawable.leibing05;
		} else if (weather.contains("雨夹雪")) {
			img = R.drawable.yujiaxue06;
		} else if (weather.contains("小雨")) {
			img = R.drawable.xiaoyu07;
		} else if (weather.contains("中雨")) {
			img = R.drawable.zhongyu08;
		} else if (weather.contains("大雨")) {
			img = R.drawable.dayu09;
		} else if (weather.contains("暴雨")) {
			img = R.drawable.baoyu10;
		} else if (weather.contains("大暴雨")) {
			img = R.drawable.dabaoyu11;
		} else if (weather.contains("特大暴雨")) {
			img = R.drawable.tedabao12;
		} else if (weather.contains("阵雪")) {
			img = R.drawable.zhenxue13;
		} else if (weather.contains("小雪")) {
			img = R.drawable.xiaoxue14;
		} else if (weather.contains("中雪")) {
			img = R.drawable.zhongxue15;
		} else if (weather.contains("大雪")) {
			img = R.drawable.daxue16;
		} else if (weather.contains("暴雪")) {
			img = R.drawable.baoxue17;
		} else if (weather.contains("雾")) {
			img = R.drawable.wu18;
		} else if (weather.contains("冻雨")) {
			img = R.drawable.dongyu19;
		} else if (weather.contains("沙尘暴")) {
			img = R.drawable.shachengbao20;
		} else if (weather.contains("浮尘")) {
			img = R.drawable.fuchen29;
		} else if (weather.contains("扬沙")) {
			img = R.drawable.yangsha30;
		} else if (weather.contains("强沙尘暴")) {
			img = R.drawable.qiangshachen31;
		} else if (weather.contains("霾")) {
			img = R.drawable.qiangshachen31;
		} else {
			img = R.drawable.qing00;
		}
		return img;
	}

	
}
