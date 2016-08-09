package com.goodweather.servers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goodweather.activity.MainActivity;
import com.goodweather.activity.R;
import com.goodweather.mod.WeatherInfo.HeWeatherBean;
import com.goodweather.mod.WeatherInfoData;
import com.goodweather.utils.time.Lunar;
import com.goodweather.utils.time.TimeUtil;
import com.goodweather.widget.WeatherWidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

public class WeatherWidgetServer extends Service{
	
	private static final int TIME_FORMAT_24 = 0;
	private static final int TIME_FORMAT_AM = 1;
	private static final int TIME_FORMAT_PM = 2;
	
	private String cityname;
	private String currentTemperature;
	private String currentWeather;
	
	private RemoteViews remoteViews;
	private int[] timesImg = { R.drawable.nw0, R.drawable.nw1, R.drawable.nw2,
			R.drawable.nw3, R.drawable.nw4, R.drawable.nw5, R.drawable.nw6,
			R.drawable.nw7, R.drawable.nw8, R.drawable.nw9, };

	private int[] dateViews = { R.id.h_left, R.id.h_right, R.id.m_left,
			R.id.m_right };
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 广播接收者去接收系统每分钟的提示广播，来更新时间
	private BroadcastReceiver mTimePickerBroadcast = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(MainActivity.UPDATE_WIDGET_WEATHER_ACTION)) {
				updateWeather();
			} else {
				updateTime();
			}
		}

	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//initWeatherInfo();
		remoteViews = new RemoteViews(getApplication().getPackageName(),
				R.layout.widget_4x2);
		PendingIntent WeatherIconHotAreaPI = PendingIntent.getActivity(this, 0,
				new Intent(this,MainActivity.class), 0);
		remoteViews.setOnClickPendingIntent(R.id.WeatherIconHotArea, WeatherIconHotAreaPI);
		
		// 左边热区点击广播
		Intent active = new Intent(getApplicationContext(),WeatherWidget.class);
		active.setAction(WeatherWidget.TEXTINFO_LEFT_HOTAREA_ACTION);
		PendingIntent TextInfoLeftHotArea = PendingIntent.getBroadcast(this, 1, active, 1);
		remoteViews.setOnClickPendingIntent(R.id.TextInfoLeftHotArea, TextInfoLeftHotArea);
		registerReceiver();// 注册广播
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null) {
			String action = intent.getAction();
			if (!TextUtils.isEmpty(action) && 
					action.endsWith(WeatherWidget.TEXTINFO_LEFT_HOTAREA_ACTION)) {
			}
		}
		updateTime();
		updateWeather();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mTimePickerBroadcast != null) {
			unregisterReceiver(mTimePickerBroadcast);
		}
	}
	
	private void registerReceiver() {
		IntentFilter updateIntent = new IntentFilter();
		updateIntent.addAction(MainActivity.UPDATE_WIDGET_WEATHER_ACTION);
		updateIntent.addAction("android.intent.action.TIME_TICK");
		updateIntent.addAction("android.intent.action.TIME_SET");
		updateIntent.addAction("android.intent.action.DATE_CHANGED");
		updateIntent.addAction("android.intent.action.TIMEZONE_CHANGED");
		registerReceiver(mTimePickerBroadcast, updateIntent);
	}
	
	
	private void updateWeather() {
		// TODO Auto-generated method stub
		initWeatherInfo();
		remoteViews.setTextViewText(R.id.weather_icon_left, cityname + "\r\n"
		+ currentTemperature + " " + currentWeather);
		remoteViews.setViewVisibility(R.id.TextViewMessage, View.GONE);
		ComponentName componentName = new ComponentName(getApplication(),WeatherWidget.class);
		AppWidgetManager.getInstance(getApplication()).updateAppWidget(componentName, remoteViews);
	}
	
	private void initWeatherInfo(){
		HeWeatherBean mHeWeatherBean = WeatherInfoData.initHeWeatherData();
		cityname = mHeWeatherBean.getBasic().getCity().toString();
		if (mHeWeatherBean != null && !cityname.isEmpty()) {
			currentTemperature = mHeWeatherBean.getNow().getTmp().toString() + "°";
			currentWeather = mHeWeatherBean.getNow().getCond().getTxt().toString();
		} else {

		}
	}
	
	private void updateTime() {
		// TODO Auto-generated method stub
		int timeFormat = getTimeFormat();
		SimpleDateFormat dFormat = new SimpleDateFormat("hhmm");
		if (timeFormat == TIME_FORMAT_24) {
			dFormat = new SimpleDateFormat("HHmm");
		}
		// 将当前时间格式化成HHmm的形式
		String timeStr = dFormat.format(new Date(System.currentTimeMillis()));
		for (int i = 0; i < timeStr.length(); i++) {
			// 将第i个数字字符转换为对应的数字
			int num2 = Integer.parseInt(timeStr.substring(i, i+1));
			// 将第i个图片的设为对应的数字图片
			remoteViews.setImageViewResource(dateViews[i], timesImg[num2]);
		}
		
		if (timeFormat == 1 ) {
			remoteViews.setImageViewResource(R.id.am_pm, R.drawable.w_amw);
		}else if (timeFormat == 2) {
			remoteViews.setImageViewResource(R.id.am_pm, R.drawable.w_pmw);
		} else {
			remoteViews.setImageViewResource(R.id.am_pm,
					R.drawable.switch_camera_hide);
		}
		remoteViews.setTextViewText(R.id.weather_icon_right,
				TimeUtil.getZhouWeek() + "\r\n" + Lunar.getDay());
		remoteViews.setViewVisibility(R.id.TextViewMessage, View.GONE);
		ComponentName componentName = new ComponentName(getApplication(),
				WeatherWidget.class);
		AppWidgetManager.getInstance(getApplication()).updateAppWidget(componentName,
				remoteViews);
		
	}

	/**
	 * 获取时间是24小时制还是12小时制
	 */
	private int getTimeFormat() {
		// TODO Auto-generated method stub
		ContentResolver cr = this.getContentResolver();
		String strTimeFormat = android.provider.Settings.System.getString(cr,
				android.provider.Settings.System.TIME_12_24);
		if (strTimeFormat != null) {
			if (strTimeFormat.equals("24")) {
				return TIME_FORMAT_24;
			} else {
				Calendar calendar = Calendar.getInstance();
				if (calendar.get(Calendar.AM_PM) == 0) {
					return TIME_FORMAT_AM;
				} else {
					return TIME_FORMAT_PM;
				}
			}
		}
		return TIME_FORMAT_24;
	}
	
	
}
