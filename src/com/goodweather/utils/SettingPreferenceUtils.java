package com.goodweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingPreferenceUtils {
	private static String TAG = "SettingUtil";
	
	public static void setPreferBoolen(Context context,String key,Boolean defaultValue){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, defaultValue).commit();
	}
	
	public static Boolean getPerferBoolen(Context context,String key,Boolean defaultValue){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

}
