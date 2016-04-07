package com.goodweather.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


import com.goodweather.adapter.WeatherForecastListAdapter;
import com.goodweather.adapter.WeatherSuggestionListAdapter;
import com.goodweather.mod.WeatherInfoData;
import com.goodweather.mod.WeatherInfo.HeWeatherBean;
import com.goodweather.mod.WeatherInfo.HeWeatherBean.DailyForecastBean;
import com.goodweather.mod.WeatherInfo.HeWeatherBean.SuggestionBean;
import com.goodweather.ui.util.DialogUtil;
import com.goodweather.util.HttpUntil;
import com.goodweather.util.LocationUtils;
import com.goodweather.util.NetUtil;
import com.goodweather.util.LocationUtils.LocationListener;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String TAG = "MainActivity";
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILURE = 1;

	private Thread mThread;

	/**
	 * 初始化数据
	 */
	private String cityname = null;
	private String updateTime = null;
	private String currentTemperature = null;
	private String currentWeather = null;
	private String peopletemperature = null;
	private String windDir = null;
	private String windSc = null;
	private String hum = null;

	private static HeWeatherBean mHeWeatherBean;

	/**
	 * 初始化aqi数据
	 */
	private String aqilever;
	private String aqi;
	private String pm25;
	private String pm10;
	private String so2;

	/*
	 * 切换城市和刷新按钮
	 */
	private Button switchCity;
	private Button refreshWeather;
	private Builder builder;

	public Intent intent;

	/**
	 * 定义布局
	 */
	private LinearLayout mainView;
	private LinearLayout changeCity;
	private TextView cityText;
	private ImageView share;
	private ImageView about;
	private ImageView refresh;
	private ImageView location;
	private ProgressBar refreshing;
	private TextView updateTimeText;
	private ScrollView scrollView;
	private LinearLayout currentWeatherLayout;
	private ImageView weatherIcon;
	private TextView currentTemperatureText;
	private TextView currentWeatherText;
	private TextView peopletemperatureText;
	private TextView windText;
	private TextView humText;
	private TextView dateText;
	private ViewPager mViewPager;
	private ListView weatherForecastList;
	private ListView weatherSuggestionList;
	private ProgressDialog dialog;
	/**
	 * 初始化AqiView
	 */
	private TextView AqiLever;
	private TextView Aqi;
	private TextView Pm25;
	private TextView Pm10;
	private TextView So2;

	/**
	 * 定位
	 */
	protected LocationUtils mLocationUtils;
	// TODO Auto-generated method stub
	LocationListener mCityNameStatus = new LocationListener() {

		@Override
		public void succeed(String city) {
			// TODO Auto-generated method stub
			Log.d(TAG, "city--->" + city);
			// mLocationTV.setText(formatBigMessage(city));
			saveCityname(city);
			initdata();
			dialog.dismiss();
			Toast.makeText(MainActivity.this,R.string.getlocation_success, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void failed() {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, R.string.getlocation_fail, Toast.LENGTH_LONG).show();
		}

		@Override
		public void detecting() {
			// TODO Auto-generated method stub

		}
	};

	public void startLocation(LocationListener cityNameStatus) {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(this, R.string.net_error, Toast.LENGTH_SHORT).show();
			return;
		}
		if (mLocationUtils == null)
			mLocationUtils = new LocationUtils(this, cityNameStatus);
		if (!mLocationUtils.isStarted()) {
			mLocationUtils.startLocation();// 开始定位
		}
	}
	private void saveCityname(String cityname){
		SharedPreferences mPreferences = getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString(MyApplication.getCityname(), cityname);
		mEditor.commit();
	}

	/**
	 * 初始化suggestionView
	 */
	// private TextView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initdata();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}

	private void initdata() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(this, R.string.net_error, Toast.LENGTH_SHORT).show();
			// return;
		} else {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	private void initView() {
		// 新界面初始化
		mainView = (LinearLayout)findViewById(R.id.weather_bg);
		changeCity = (LinearLayout) findViewById(R.id.change_city_layout);
		cityText = (TextView) findViewById(R.id.city);
		share = (ImageView) findViewById(R.id.share);
		about = (ImageView) findViewById(R.id.about);
		refresh = (ImageView) findViewById(R.id.refresh);
		location = (ImageView) findViewById(R.id.location);
		refreshing = (ProgressBar) findViewById(R.id.refreshing);
		updateTimeText = (TextView) findViewById(R.id.update_time);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);

		// weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		currentTemperatureText = (TextView) findViewById(R.id.current_temperature);
		currentWeatherText = (TextView) findViewById(R.id.current_weather);
		peopletemperatureText = (TextView) findViewById(R.id.people_temperature);
		windText = (TextView) findViewById(R.id.wind);
		humText = (TextView) findViewById(R.id.humidity);
		// dateText = (TextView) findViewById(R.id.date);

		weatherForecastList = (ListView) findViewById(R.id.weather_forecast_list);
		initAqiView();
		weatherSuggestionList = (ListView) findViewById(R.id.weather_suggestion_list);
		changeCity.setOnClickListener(new ButtonListener());
		share.setOnClickListener(new ButtonListener());
		about.setOnClickListener(new ButtonListener());
		refresh.setOnClickListener(new ButtonListener());
		location.setOnClickListener(new ButtonListener());
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_city_layout:// 更换城市
				intent = new Intent();
				intent.setClass(MainActivity.this, UpdateCityName.class);
				startActivityForResult(intent, 100);
				// finish();
				break;
			case R.id.share:// 分享按钮
				share();
				break;
			case R.id.about:// 关于本软的开发信息
				AboutInfo();
				break;
			case R.id.refresh:// 更新按钮
				dialogShow("正在更新，请等待！");
				initdata();
				dialog.dismiss();
				break;
			case R.id.location:
				dialogShow("正在定位，请等待！");
				startLocation(mCityNameStatus);
				break;
			default:
				break;
			}
		}

	}
	
	private void AboutInfo(){
		LayoutInflater inflater = getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.weather_dialog,
				(ViewGroup) findViewById(R.layout.weather_dialog));
		TextView version = (TextView) dialogLayout.findViewById(R.id.version);

		version.setText("实现功能如下：\n欢迎使用，谢谢");
		builder = new Builder(MainActivity.this);
		builder.setTitle("关于");
		builder.setView(dialogLayout);
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		builder.show();
	}

	private void initWeatherdate() {
		mHeWeatherBean = WeatherInfoData.initHeWeatherData();
		cityname = mHeWeatherBean.getBasic().getCity().toString();
		updateTime = mHeWeatherBean.getBasic().getUpdate().getLoc().toString();
		currentTemperature = mHeWeatherBean.getNow().getTmp().toString() + "°";
		currentWeather = mHeWeatherBean.getNow().getCond().getTxt().toString();
		peopletemperature = mHeWeatherBean.getNow().getFl().toString() + "°";
		windDir = mHeWeatherBean.getNow().getWind().getDir().toString();
		windSc = mHeWeatherBean.getNow().getWind().getSc().toString();
		hum = mHeWeatherBean.getNow().getHum().toString();
		initAqiData();

	}

	private void initViewData() {
		cityText.setText(cityname);
		updateTimeText.setText(updateTime);
		currentTemperatureText.setText(currentTemperature);
		currentWeatherText.setText(currentWeather);
		peopletemperatureText.setText("体感温度：" + peopletemperature);
		windText.setText(windDir + windSc + "级");
		humText.setText("湿度" + hum + "%");
		weatherForecastList.setAdapter(new WeatherForecastListAdapter(this));
		initAqiViewData();
		weatherSuggestionList.setAdapter(new WeatherSuggestionListAdapter(this));
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SUCCESS:
				initWeatherdate();
				initViewData();
				//dialog.dismiss();
				break;
			case MSG_FAILURE:
				break;

			default:
				break;
			}
		}
	};

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String info = HttpUntil.DownloadUrl(initCityName());
				saveDownloadInfo(info);
				//dialogShow("正在更新");
				mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
	
	private void dialogShow(String info){
		dialog = new ProgressDialog(MainActivity.this);
		dialog.setMessage(info);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
	}

	/**
	 * 获取天气预报信息
	 * 
	 * @return 天气预报list
	 */
	public static ArrayList<HashMap<String, Object>> getForcecastData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		DailyForecastBean mdaily;
		for (int i = 0; i < mHeWeatherBean.getDaily_forecast().size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			mdaily = mHeWeatherBean.getDaily_forecast().get(i);
			String weather = mdaily.getCond().getTxt_d().toString();
			String tmp = mdaily.getTmp().getMin() + "°" + "~" + mdaily.getTmp().getMax() + "°";
			String wind = mdaily.getWind().getDir().toString() + mdaily.getWind().getSc().toString();
			map.put("date", mdaily.getDate().toString());
			map.put("img", getWeatherImg(weather));
			map.put("weather", weather);
			map.put("temperature", tmp);
			map.put("wind", wind);
			list.add(map);
		}
		return list;
	}

	/**
	 * 指数信息
	 */
	public static ArrayList<HashMap<String, Object>> getSuggestionData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SuggestionBean mSuggestionBean = mHeWeatherBean.getSuggestion();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String comfBrf = mSuggestionBean.getComf().getBrf().toString();
		String comfTxt = mSuggestionBean.getComf().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_wind);
		map.put("title", "舒适度指数");
		map.put("desc", comfBrf);
		map.put("detail", comfTxt);
		list.add(map);

		map = new HashMap<String, Object>();
		String cwBrf = mSuggestionBean.getCw().getBrf().toString();
		String cwTxt = mSuggestionBean.getCw().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_carwash);
		map.put("title", "洗车指数");
		map.put("desc", comfBrf);
		map.put("detail", cwTxt);
		list.add(map);

		map = new HashMap<String, Object>();
		String drsgBrf = mSuggestionBean.getDrsg().getBrf().toString();
		String drsgTxt = mSuggestionBean.getDrsg().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_clothes);
		map.put("title", "穿衣指数");
		map.put("desc", drsgBrf);
		map.put("detail", drsgTxt);
		list.add(map);

		map = new HashMap<String, Object>();
		String fluBrf = mSuggestionBean.getFlu().getBrf().toString();
		String fluTxt = mSuggestionBean.getFlu().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_cold);
		map.put("title", "感冒指数");
		map.put("desc", fluBrf);
		map.put("detail", fluTxt);
		list.add(map);

		map = new HashMap<String, Object>();
		String sportBrf = mSuggestionBean.getSport().getBrf().toString();
		String sportTxt = mSuggestionBean.getSport().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_sport);
		map.put("title", "运动指数");
		map.put("desc", sportBrf);
		map.put("detail", sportTxt);
		list.add(map);

		map = new HashMap<String, Object>();
		String travBrf = mSuggestionBean.getTrav().getBrf().toString();
		String travTxt = mSuggestionBean.getTrav().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_tour);
		map.put("title", "旅游指数");
		map.put("desc", travBrf);
		map.put("detail", travTxt);
		list.add(map);

		map = new HashMap<String, Object>();
		String uvBrf = mSuggestionBean.getUv().getBrf().toString();
		String uvTxt = mSuggestionBean.getUv().getTxt().toString();
		map.put("img", R.drawable.ic_lifeindex_ultravioletrays);
		map.put("title", "紫外线指数");
		map.put("desc", uvBrf);
		map.put("detail", uvTxt);
		list.add(map);
		return list;
	}

	/**
	 * 根据天气信息设置天气图片
	 * 
	 * @param weather
	 *            天气信息
	 * @return 对应的天气图片id
	 */
	private static int getWeatherImg(String weather) {
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

	private void initAqiView() {
		AqiLever = (TextView) findViewById(R.id.aqi_level);
		Aqi = (TextView) findViewById(R.id.aqi);
		Pm25 = (TextView) findViewById(R.id.pm25);
		Pm10 = (TextView) findViewById(R.id.pm_10);
		So2 = (TextView) findViewById(R.id.so_2);
	}

	private void initAqiData() {
		aqilever = mHeWeatherBean.getAqi().getCity().getQlty().toString();
		aqi = mHeWeatherBean.getAqi().getCity().getAqi().toString();
		pm25 = mHeWeatherBean.getAqi().getCity().getPm25().toString();
		pm10 = mHeWeatherBean.getAqi().getCity().getPm10().toString();
		so2 = mHeWeatherBean.getAqi().getCity().getSo2().toString();
	}

	private void initAqiViewData() {
		AqiLever.setText(aqilever);
		Aqi.setText(aqi);
		Pm25.setText(pm25);
		Pm10.setText(pm10);
		So2.setText(so2);
	}

	private String initCityName() {
		SharedPreferences mPreferences = getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
		String cityname = mPreferences.getString(MyApplication.getCityname(), "北京");
		return cityname;
	}

	public void saveDownloadInfo(String weatherinfo) throws Exception {
		String filename = MyApplication.getFilename();
		FileOutputStream outputStream = null;
		outputStream = openFileOutput(filename, MODE_PRIVATE);
		outputStream.write(weatherinfo.getBytes());
		outputStream.close();
	}
	
	private void share(){
		new AsyncTask<Void, Void, File>(){
			Dialog mdialog;
			
			protected void onPreExecute() {
				super.onPreExecute();
				mdialog = DialogUtil.getCustomeDialog(MainActivity.this, R.style.load_dialog, R.layout.custom_progress_dialog);
				TextView titleTxtv = (TextView) mdialog
						.findViewById(R.id.dialogText);
				titleTxtv.setText(R.string.please_wait);
				mdialog.show();
			};
			@Override
			protected File doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					new File(getFilesDir(), "share.png").deleteOnExit();
					FileOutputStream fileOutputStream = openFileOutput(
							"share.png", 1);
					mainView.setDrawingCacheEnabled(true);
					mainView.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
					return new File(getFilesDir(),"share.png");
				} catch (FileNotFoundException e) {
					// TODO: handle exception
				}
				return null;
			}
			
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				mdialog.dismiss();
				if (result == null) {
					Toast.makeText(MainActivity.this, R.string.share_fail,
							Toast.LENGTH_SHORT).show();
					return;
				}
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");
				intent.putExtra(Intent.EXTRA_TEXT, "我正在使用Good天气，可以随时随地查看天气信息，是您出差、旅行的贴心助手！推荐你也试试~");
				intent.putExtra("android.intent.extra.STREAM",
						Uri.fromFile(result));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, "好友分享"));
				
			};
			
		}.execute();
	}

}
