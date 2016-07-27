package com.goodweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingPreferenceUtils {
	
	public static void setPreferBoolen(Context context,String key,Boolean defaultValue){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, defaultValue).commit();
	}
	
	public static Boolean getPerferBoolen(Context context,String key,Boolean defaultValue){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static void setPreferString(Context context, String key,String defaultValue){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putString(key, defaultValue).commit();
	}
	
	public static String getPreferString(Context context,String key,String defaultValue){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

}
