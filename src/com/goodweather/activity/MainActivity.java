package com.goodweather.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.goodweather.adapter.WeatherForecastListAdapter;
import com.goodweather.adapter.WeatherSuggestionListAdapter;
import com.goodweather.debug.LogDebug;
import com.goodweather.mod.WeatherInfo.HeWeatherBean;
import com.goodweather.mod.WeatherInfo.HeWeatherBean.DailyForecastBean;
import com.goodweather.mod.WeatherInfo.HeWeatherBean.SuggestionBean;
import com.goodweather.mod.WeatherInfoData;
import com.goodweather.utils.HttpUntil;
import com.goodweather.utils.LocationUtils;
import com.goodweather.utils.LocationUtils.LocationListener;
import com.goodweather.utils.NetUtil;
import com.goodweather.utils.ReadWeatherTXTInfo;
import com.goodweather.utils.SettingPreferenceUtils;
import com.goodweather.utils.SpeechSynthesisUtils;
import com.goodweather.utils.ui.DialogUtil;
import com.goodweather.utils.ui.WeatherImg;
import com.iflytek.cloud.ErrorCode;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//private String TAG = "MainActivity";
	private LogDebug.Tag TAG = new LogDebug.Tag("MainActivity");


	public static final String UPDATE_WIDGET_WEATHER_ACTION = "com.goodweather.action.update_weather";
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
	private ImageView location;

	private SwipeRefreshLayout refresh;

	private TextView updateTimeText;
	private ScrollView scrollView;
	private RelativeLayout aqiView;

	// private ImageView weatherIcon;
	private TextView currentTemperatureText;
	private TextView currentWeatherText;
	private TextView peopletemperatureText;
	private TextView windText;
	private TextView humText;
	private ListView weatherForecastList;
	private ListView weatherSuggestionList;
	private ImageView speech;
	// private ProgressDialog dialog;
	private Dialog mdialog;
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
			LogDebug.d(TAG, "city--->" + city);
			saveCityname(city);
			initdata();
			mdialog.dismiss();
			Toast.makeText(MainActivity.this, R.string.getlocation_success, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void failed() {
			// TODO Auto-generated method stub
			mdialog.dismiss();
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

	private void saveCityname(String cityname) {
		// SharedPreferences mPreferences =
		// getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
		// Editor mEditor = mPreferences.edit();
		// mEditor.putString(MyApplication.getCityname(), cityname);
		// mEditor.commit();
		SettingPreferenceUtils.setPreferString(this, MyApplication.getCityname(), cityname);
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
		LogDebug.d(TAG, "onStart--->");
		if (ReadWeatherTXTInfo.isFileExist(MyApplication.getWeatherinfotxt())) {
			initWeatherdate();
		} else {
			initdata();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void initView() {
		// 新界面初始化
		mainView = (LinearLayout) findViewById(R.id.weather_bg);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		scrollView.smoothScrollTo(0, 10);
		initTitleView();// 初始化Title
		initRefershView();// 初始化下拉刷新
		initNowView();// 初始化实时天气
		initForecastView();// 初始化预报信息
		initAqiView();// 初始化Aqi信息
		initSuggestionView();// 初始化指数信息
	}

	private void initWeatherdate() {
		mHeWeatherBean = WeatherInfoData.initHeWeatherData();
		if (mHeWeatherBean != null) {
			initTitleData();// 初始化title
			initNowData();// 初始化实时天气数据
			initAqiData();// 初始化Aqi数据
			initViewData();
		} else {
			Toast.makeText(MainActivity.this, "获取数据失败，请稍后重试！", Toast.LENGTH_LONG).show();
		}

	}

	private void initViewData() {
		initTitleViewData();// 设置title
		initNowViewData();// 设置实时天气显示数据
		initForecastData();// 设置预报列表
		initAqiViewData();// 设置Aqi数据显示
		initSuggestionData();// 设置指数数据
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

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SUCCESS:
				refresh.setRefreshing(false);
				initWeatherdate();
				updateWidgetWeather();
				// initViewData();
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
				HttpUntil.DownloadUrl(initCityName());
				mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			} catch (Exception e) {
				// TODO: handle exception
			//	mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
			}
		}
	};

	private void dialogShow(int tag) {
		/*
		 * dialog = new ProgressDialog(MainActivity.this);
		 * dialog.setMessage(info); dialog.setCanceledOnTouchOutside(false);
		 * dialog.show();
		 */
		mdialog = DialogUtil.getCustomeDialog(MainActivity.this, R.style.load_dialog, R.layout.custom_progress_dialog);
		TextView titleTxtv = (TextView) mdialog.findViewById(R.id.dialogText);
		if (tag == 1) {
			titleTxtv.setText(R.string.getlocation_wait);
		} else if (tag == 2) {
			titleTxtv.setText(R.string.please_wait);
		}
		mdialog.show();
	}

	/**
	 * 初始化title
	 */
	private void initTitleView() {
		changeCity = (LinearLayout) findViewById(R.id.change_city_layout);
		cityText = (TextView) findViewById(R.id.city);
		share = (ImageView) findViewById(R.id.share);
		about = (ImageView) findViewById(R.id.about);
		location = (ImageView) findViewById(R.id.location);
		updateTimeText = (TextView) findViewById(R.id.update_time);

		changeCity.setOnClickListener(new ButtonListener());
		share.setOnClickListener(new ButtonListener());
		about.setOnClickListener(new ButtonListener());
		// refresh.setOnClickListener(new ButtonListener());
		location.setOnClickListener(new ButtonListener());
	}

	private void initTitleData() {
		cityname = mHeWeatherBean.getBasic().getCity().toString();
		updateTime = mHeWeatherBean.getBasic().getUpdate().getLoc().toString();
	}

	private void initTitleViewData() {
		cityText.setText(cityname);
		updateTimeText.setText(updateTime);
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_city_layout:// 更换城市
				intent = new Intent();
				intent.setClass(MainActivity.this, FindOtherCityInfo.class);
				startActivityForResult(intent, 100);
				// finish();
				break;
			case R.id.share:// 分享按钮
				share();
				break;
			case R.id.about:// 关于本软的开发信息
				AboutInfo();
				break;
			/*
			 * case R.id.refresh:// 更新按钮 dialogShow(2); initdata();
			 * mdialog.dismiss(); break;
			 */
			case R.id.location:
				dialogShow(1);
				startLocation(mCityNameStatus);
				break;
			case R.id.speechviem:
				speechPlay();
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 下拉刷新
	 */
	private void initRefershView() {
		refresh = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
		refresh.setSize(SwipeRefreshLayout.DEFAULT);
		refresh.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (ReadWeatherTXTInfo.isDeleteFolder(MyApplication.getWeatherinfotxt())) {
					initdata();
				} else {
					refresh.setRefreshing(false);
					Toast.makeText(MainActivity.this, "刷新失败，请重新尝试！", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	/**
	 * 语音合成
	 */
	private void speechPlay() {
		String infoOne = "现在开始为您播报:" + cityText.getText().toString();
		String infoTwo = "，今天天气:" + currentWeatherText.getText().toString();
		String infoThree = ",温度:" + currentTemperature + "摄氏度";
		String infoFour = ",体感温度:" + peopletemperature + "摄氏度";
		String infoFive = ",风力情况：" + windText.getText().toString();
		String infoSix = ",湿度:百分之" + hum;
		String weatherInfo = infoOne + infoTwo + infoThree + infoFour + infoFive + infoSix;
		SpeechSynthesisUtils.setParam(MainActivity.this);
		int code = SpeechSynthesisUtils.mTts.startSpeaking(weatherInfo, SpeechSynthesisUtils.mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
				// mInstaller.install();
				dialogShow(1);
			} else {
				// showTip("语音合成失败,错误码: " + code);
				dialogShow(2);
			}
		}
	}

	/**
	 * 初始化实时天气信息
	 */
	private void initNowView() {
		currentTemperatureText = (TextView) findViewById(R.id.current_temperature);
		currentWeatherText = (TextView) findViewById(R.id.current_weather);
		peopletemperatureText = (TextView) findViewById(R.id.people_temperature);
		windText = (TextView) findViewById(R.id.wind);
		humText = (TextView) findViewById(R.id.humidity);
		speech = (ImageView) findViewById(R.id.speechviem);
		speech.setOnClickListener(new ButtonListener());
	}

	private void initNowData() {
		currentTemperature = mHeWeatherBean.getNow().getTmp().toString() + "°";
		currentWeather = mHeWeatherBean.getNow().getCond().getTxt().toString();
		peopletemperature = mHeWeatherBean.getNow().getFl().toString() + "°";
		windDir = mHeWeatherBean.getNow().getWind().getDir().toString();
		windSc = mHeWeatherBean.getNow().getWind().getSc().toString();
		hum = mHeWeatherBean.getNow().getHum().toString();
	}

	private void initNowViewData() {
		currentTemperatureText.setText(currentTemperature);
		currentWeatherText.setText(currentWeather);
		peopletemperatureText.setText("体感温度：" + peopletemperature);
		windText.setText(windDir + windSc + "级");
		humText.setText("湿度" + hum + "%");
	}

	/**
	 * 初始化天气预报信息
	 * 
	 */
	private void initForecastView() {
		weatherForecastList = (ListView) findViewById(R.id.weather_forecast_list);
		weatherForecastList.setFocusable(false);
	}

	private void initForecastData() {
		weatherForecastList.setAdapter(new WeatherForecastListAdapter(this));
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
			map.put("img", WeatherImg.getWeatherImg(weather));
			map.put("weather", weather);
			map.put("temperature", tmp);
			map.put("wind", wind);
			list.add(map);
		}
		return list;
	}

	/**
	 * 初始化aqi信息
	 */
	private void initAqiView() {
		aqiView = (RelativeLayout) findViewById(R.id.aqi_view);
		aqiView.setVisibility(View.GONE);
		AqiLever = (TextView) findViewById(R.id.aqi_level);
		Aqi = (TextView) findViewById(R.id.aqi);
		Pm25 = (TextView) findViewById(R.id.pm25);
		Pm10 = (TextView) findViewById(R.id.pm_10);
		So2 = (TextView) findViewById(R.id.so_2);
	}

	private void initAqiData() {
		if (mHeWeatherBean.getAqi() != null) {
			aqiView.setVisibility(View.VISIBLE);
			aqilever = mHeWeatherBean.getAqi().getCity().getQlty().toString();
			aqi = mHeWeatherBean.getAqi().getCity().getAqi().toString();
			pm25 = mHeWeatherBean.getAqi().getCity().getPm25().toString();
			pm10 = mHeWeatherBean.getAqi().getCity().getPm10().toString();
			so2 = mHeWeatherBean.getAqi().getCity().getSo2().toString();
		} else {
		}
	}

	private void initAqiViewData() {
		AqiLever.setText(aqilever);
		Aqi.setText(aqi);
		Pm25.setText(pm25);
		Pm10.setText(pm10);
		So2.setText(so2);
	}

	/**
	 * 初始化指数信息
	 */
	private void initSuggestionView() {
		weatherSuggestionList = (ListView) findViewById(R.id.weather_suggestion_list);
		weatherSuggestionList.setFocusable(false);
	}

	private void initSuggestionData() {
		weatherSuggestionList.setAdapter(new WeatherSuggestionListAdapter(this));
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
		map.put("desc", cwBrf);
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
	 * 初始化cityname
	 * 
	 * @return
	 */
	private String initCityName() {
		// SharedPreferences mPreferences =
		// getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
		// String cityname = mPreferences.getString(MyApplication.getCityname(),
		// "北京");
		String cityname = SettingPreferenceUtils.getPreferString(this, MyApplication.getCityname(), "北京");
		return cityname;
	}

	/**
	 * 截图分享设置
	 */
	private void share() {
		// 启动异步操作
		new AsyncTask<Void, Void, File>() {
			Dialog mdialog;

			// 这里是最终用户调用Excute时的接口，当任务执行之前开始调用此方法，可以在这里显示进度对话框。
			protected void onPreExecute() {
				super.onPreExecute();
				mdialog = DialogUtil.getCustomeDialog(MainActivity.this, R.style.load_dialog,
						R.layout.custom_progress_dialog);
				TextView titleTxtv = (TextView) mdialog.findViewById(R.id.dialogText);
				titleTxtv.setText(R.string.please_wait);
				mdialog.show();
			};

			// 后台执行，比较耗时的操作都可以放在这里。注意这里不能直接操作UI。此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
			@Override
			protected File doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					new File(getFilesDir(), "share.png").deleteOnExit();
					FileOutputStream fileOutputStream = openFileOutput("share.png", 1);
					mainView.setDrawingCacheEnabled(true);
					mainView.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
					return new File(getFilesDir(), "share.png");
				} catch (FileNotFoundException e) {
					// TODO: handle exception
				}
				return null;
			}

			// 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。
			// 此方法在主线程执行，任务执行的结果作为此方法的参数返回
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				mdialog.dismiss();
				if (result == null) {
					Toast.makeText(MainActivity.this, R.string.share_fail, Toast.LENGTH_SHORT).show();
					return;
				}
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");
				intent.putExtra(Intent.EXTRA_TEXT, "我正在使用Good天气，可以随时随地查看天气信息，是您出差、旅行的贴心助手！推荐你也试试~");
				intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(result));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, "好友分享"));

			};

		}.execute();
	}

	/**
	 * 关于
	 */
	private void AboutInfo() {
		LayoutInflater inflater = getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.weather_dialog,
				(ViewGroup) findViewById(R.layout.weather_dialog));
		TextView version = (TextView) dialogLayout.findViewById(R.id.version);

		version.setText(R.string.about_info);
		builder = new Builder(MainActivity.this);
		builder.setTitle("关于");
		builder.setView(dialogLayout);
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		builder.show();
	}

	private void updateWidgetWeather() {
		sendBroadcast(new Intent(UPDATE_WIDGET_WEATHER_ACTION));
	}

}
