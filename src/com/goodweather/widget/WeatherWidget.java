package com.goodweather.widget;

import com.goodweather.servers.WeatherWidgetServer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class WeatherWidget extends AppWidgetProvider{
	
	public static final String TEXTINFO_LEFT_HOTAREA_ACTION = "TextInfoLeftHotArea";
	public static final String WEATHERICON_HOTAREA_ACTION = "WeatherIconHotArea";
	public static final String TEXTINFO_RIGHT_HOTAREA_ACTION = "TextInfoRightHotArea";
	public static final String TIME_LEFT_HOTAREA_ACTION = "TimeLeftHotArea";
	public static final String TIME_RIGHT_HOTAREA_ACTION = "TimeRightHotArea";
	
	/**
	 *  每添加一个小插件调用一次，跟onDeleted对应
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Intent intent = new Intent(context,WeatherWidgetServer.class);
		context.startService(intent);
	}
	
	/**
	 * // 第一个小插件添加时调用，跟onDisabled对应
	 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}
	
	/**
	 *  任何添加删除操作都会调用
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		String action = intent.getAction();
		if (action.equals("android.intent.action.USER_PRESENT")) {
			context.startService(new Intent(context,WeatherWidgetServer.class));
		}else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			context.startService(new Intent(context, WeatherWidgetServer.class));
		}else if (action.equals(TEXTINFO_LEFT_HOTAREA_ACTION)) {
			Intent updateInten = new Intent(context,WeatherWidgetServer.class);
			updateInten.setAction(TEXTINFO_LEFT_HOTAREA_ACTION);
			context.startService(updateInten);
		}
	}

	/**
	 *  最后一个小插件删除时会调用
	 */
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Intent intent = new Intent(context,WeatherWidgetServer.class);
		context.stopService(intent);
	}
	
}
