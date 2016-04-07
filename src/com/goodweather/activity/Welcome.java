package com.goodweather.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

public class Welcome extends Activity {

	private String ATG = "Welcome";

//	private static final int MSG_SUCCESS = 0;
//	private static final int MSG_FAILURE = 1;

	// Whether there is a Wi-Fi connection.
//	private static boolean wifiConnected = false;
	// Whether there is a mobile connection.
//	private static boolean mobileConnected = false;
//	private static final String WIFI = "Wi-Fi";
//	private static final String ANY = "Any";
	// Whether the display should be refreshed.
	//public static boolean reIsNetwork = true;
	// The user's current network preference setting.
//	public static String sPref = null;

	// The BroadcastReceiver that tracks network connectivity changes.
	// private NetworkReceiver receiver = new NetworkReceiver();

//	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		// initNetwork();

		Runnable runnable = new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				if (isFirstUse()) {
					Intent mIntent = new Intent(Welcome.this, KaKaLauncherActivity.class);
					startActivity(mIntent); 
					finish(); 
				} else {
					Intent mIntent = new Intent(Welcome.this, MainActivity.class);
					startActivity(mIntent);
					finish();
				}
				
			}
		};
		Handler mHandler = new Handler();
		mHandler.postDelayed(runnable, 3 * 1000);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/*
		 * if (receiver != null) { this.unregisterReceiver(receiver); }
		 */

	}

	private Boolean isFirstUse() {
		boolean is = false;
		SharedPreferences mPreferences = getSharedPreferences(MyApplication.getWeatherinfo(), Context.MODE_PRIVATE);
		if (mPreferences.getInt(MyApplication.getOne(), 0) != 1) {
			Editor mEditor = mPreferences.edit();
			mEditor.putInt(MyApplication.getOne(), 1);
			mEditor.commit();
			is = true;
		} else {
			is = false;
		}
		Log.d(ATG, "is --->" + is);
		return is;

	}
	/*
	 * private void init() { // updateConnectedFlags(); if (reIsNetwork) {
	 * mThread = new Thread(mRunnable); mThread.start(); } else {
	 * Toast.makeText(getApplicationContext(), "请检查网络连接！",
	 * Toast.LENGTH_LONG).show(); } Log.d(ATG, "reIsNetwork --->" +
	 * reIsNetwork);
	 * 
	 * }
	 * 
	 * private Handler mHandler = new Handler() { public void
	 * handleMessage(Message message) { switch (message.what) { case
	 * MSG_SUCCESS: if (isFirstUse()) { Intent mIntent = new
	 * Intent(getApplicationContext(), KaKaLauncherActivity.class);
	 * startActivity(mIntent); finish(); } else { Intent mIntent = new Intent();
	 * mIntent.setClass(Welcome.this, MainActivity.class);
	 * startActivity(mIntent); finish(); } break; case MSG_FAILURE:
	 * 
	 * break; default: break; } } };
	 * 
	 * Runnable mRunnable = new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub try {
	 * String info = HttpUntil.DownloadUrl(initUrl(initCityName()));
	 * saveDownloadInfo(info);
	 * mHandler.obtainMessage(MSG_SUCCESS).sendToTarget(); } catch (Exception e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } } };
	 * 
	 * 
	 * 
	 * private void initNetwork() { IntentFilter filter = new
	 * IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION); receiver = new
	 * NetworkReceiver(); this.registerReceiver(receiver, filter); } // public
	 * Boolean isNetwork() { // IntentFilter filter = new //
	 * IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION); // receiver = new
	 * NetworkReceiver(); // this.registerReceiver(receiver, filter); // if
	 * (reIsNetwork) { // return true; // } else { // return false; // } // // }
	 * 
	 * private void updateConnectedFlags() { // TODO Auto-generated method stub
	 * ConnectivityManager connMgr = (ConnectivityManager)
	 * getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo activeInfo =
	 * connMgr.getActiveNetworkInfo(); if (activeInfo != null &&
	 * activeInfo.isConnected()) { wifiConnected = activeInfo.getType() ==
	 * ConnectivityManager.TYPE_WIFI; mobileConnected = activeInfo.getType() ==
	 * ConnectivityManager.TYPE_MOBILE; } else { wifiConnected = false;
	 * mobileConnected = false; } }
	 * 
	 * public class NetworkReceiver extends BroadcastReceiver {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { // TODO
	 * Auto-generated method stub ConnectivityManager mConnectivityManager =
	 * (ConnectivityManager) context
	 * .getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo mNetworkInfo
	 * = mConnectivityManager.getActiveNetworkInfo(); if (WIFI.equals(sPref) &&
	 * mNetworkInfo != null && mNetworkInfo.getType() ==
	 * ConnectivityManager.TYPE_WIFI) { reIsNetwork = true; } else if
	 * (ANY.equals(sPref) && mNetworkInfo != null) { reIsNetwork = true; } else
	 * { reIsNetwork = false; } if (mNetworkInfo != null &&
	 * mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) { reIsNetwork =
	 * true; } else if ( mNetworkInfo != null) { reIsNetwork = true; } else {
	 * reIsNetwork = false; }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * public void saveDownloadInfo(String weatherinfo) throws Exception {
	 * String filename = MyApplication.getFilename(); FileOutputStream
	 * outputStream = null; outputStream = openFileOutput(filename,
	 * MODE_PRIVATE); outputStream.write(weatherinfo.getBytes());
	 * outputStream.close(); }
	 * 
	 * private String initCityName() { SharedPreferences mPreferences =
	 * getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
	 * String cityname = mPreferences.getString(MyApplication.getCityname(),
	 * "上海"); return cityname; }
	 * 
	 */
}
