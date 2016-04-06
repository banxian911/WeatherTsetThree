package com.example.testthree;

import com.example.util.LocationUtils;
import com.example.util.LocationUtils.LocationListener;
import com.example.util.NetUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCityName extends Activity implements OnClickListener,TextWatcher{

	private String TAG = "UpdateCityName";
	
	private ImageView mBackBtn;
	private TextView mLocationTV;
	private EditText mQueryCityET;
	private Button mButtonSure;
	private Button mButtonCanal;
	
	protected LocationUtils mLocationUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_query_layout);
		initView();
		
	}

	private void initView(){
		mBackBtn = (ImageView) findViewById(R.id.back_image);
		mLocationTV = (TextView) findViewById(R.id.location_text);
		mQueryCityET = (EditText) findViewById(R.id.queryCityText);
		mBackBtn.setOnClickListener(this);
		mLocationTV.setOnClickListener(this);
		mQueryCityET.addTextChangedListener(this);
		String cityName = getCityname();
		if (TextUtils.isEmpty(cityName)) {
			startLocation(mCityNameStatus);
		} else {
			mLocationTV.setText(formatBigMessage(cityName));
		}
	}
	private void saveCityname(String cityname){
		SharedPreferences mPreferences = getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString(MyApplication.getCityname(), cityname);
		mEditor.commit();
	}
	
	public String getCityname() {
		SharedPreferences mPreferences = getSharedPreferences(MyApplication.getWeatherinfo(), MODE_PRIVATE);
		String cityname = mPreferences.getString(MyApplication.getCityname(), null);
		return cityname;
	} 
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_image:
			finish();
			break;
		case R.id.location_text:
			startLocation(mCityNameStatus);
			break;
		default:
			break;
		}
	}
	
	LocationListener mCityNameStatus = new LocationListener() {
		
		@Override
		public void succeed(String city) {
			// TODO Auto-generated method stub
			Log.d(TAG, "city--->"+ city);
			mLocationTV.setText(formatBigMessage(city));
			saveCityname(city);
		}
		
		@Override
		public void failed() {
			// TODO Auto-generated method stub
			Toast.makeText(UpdateCityName.this,R.string.getlocation_fail, Toast.LENGTH_LONG).show();
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
	
	// This is the message string used in bigText and bigPicture notifications.
		public CharSequence formatBigMessage(String city) {
			final TextAppearanceSpan notificationSubjectSpan = new TextAppearanceSpan(
					this, R.style.NotificationPrimaryText);

			// Change multiple newlines (with potential white space between), into a
			// single new line
			final String message = !TextUtils.isEmpty(city) ? city : "";
			String afterStr = "(点击重新定位)";
			SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
					message);
			if (!TextUtils.isEmpty(afterStr)) {
				spannableStringBuilder.append(afterStr);
				spannableStringBuilder.setSpan(notificationSubjectSpan,
						message.length(), message.length() + afterStr.length(), 0);
			}
			return spannableStringBuilder;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			doBeforeTextChanged();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(s)) {
				//mQueryCityExitBtn.setVisibility(View.GONE);
			} else {
				//mQueryCityExitBtn.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
		private void doBeforeTextChanged() {
//			if (mQueryCityListView.getVisibility() == View.GONE) {
//				mQueryCityListView.setVisibility(View.VISIBLE);
//			}
		}
}
