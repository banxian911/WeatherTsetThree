package com.goodweather.debug;

import android.os.Build;
import android.util.Log;

public class LogDebug {
	
	public static final String LOGTAG_PREFIX = "GW_";
	private static final LogDebug.Tag TAG = new LogDebug.Tag("Log");
	
	public static boolean isDebug = true;

	public static final class Tag {
		
		// The length limit from Android framework is 23.
		private static final int MAX_TAG_LEN = 23 - LOGTAG_PREFIX.length();
		
		final String mValue;
		
		public Tag(String tag){
			final int lenDiff = tag.length() - MAX_TAG_LEN;
			if (lenDiff > 0) {
				
			}
			mValue = LOGTAG_PREFIX + (lenDiff > 0 ? tag.substring(0,MAX_TAG_LEN) : tag);
		}
		
		public String toString(){
			return mValue;
		}
		
	}
	
   
    public static void i(Tag tag, String msg)  
    {  
        if (isDebug)  
        	Log.i(tag.toString(), msg);  
    }  
  
    public static void d(Tag tag, String msg)  
    {  
        if (isDebug)  
            Log.d(tag.toString(), msg);  
    }  
  
    public static void e(Tag tag, String msg)  
    {  
        if (isDebug)  
        	Log.e(tag.toString(), msg);  
    }  
  
    public static void v(Tag tag, String msg)  
    {  
        if (isDebug)  
        	Log.v(tag.toString(), msg);  
    }  
    
    public static void w(Tag tag, String msg)  
    {  
        if (isDebug)  
        	Log.w(tag.toString(), msg);  
    }  
  
	
	/*public static void suppressLogsForTesting(boolean suppress){
		sSuppressForTesting = suppress;
	}
	
	private static boolean isLoggable(Tag tag,int level){
		if (sSuppressForTesting) {
			return false;
		}
		try {
			if (LogHelper.instance().getOverrideLevel() != 0) {
				return LogHelper.instance().getOverrideLevel() <= level;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e(TAG,"tag too long:" + tag);
			return false;
		}
		//return sSuppressForTesting;
	}
	
	private static boolean shouldLog(Tag tag,int level){
		return android.util.Log.isLoggable(LOGTAG_PREFIX, level) || android.util.Log.isLoggable(tag.toString(), level);
	}
	*/
	
}
