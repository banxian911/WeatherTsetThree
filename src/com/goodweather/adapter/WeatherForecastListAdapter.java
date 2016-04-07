package com.goodweather.adapter;


import com.goodweather.activity.MainActivity;
import com.goodweather.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherForecastListAdapter extends BaseAdapter {

	private Context mContext;

	public WeatherForecastListAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.getForcecastData().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 列表每一项横向的内容
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.weather_forecast_item, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.weather_forecast_date);
			holder.img = (ImageView) convertView.findViewById(R.id.weather_forecast_img);
			holder.weather = (TextView) convertView.findViewById(R.id.weather_forecast_weather);
			holder.temperature = (TextView) convertView.findViewById(R.id.weather_forecast_temperature);
			holder.wind = (TextView) convertView.findViewById(R.id.weather_forecast_wind);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.date.setText(MainActivity.getForcecastData().get(position).get("date").toString());
		holder.img.setImageResource((Integer) MainActivity.getForcecastData().get(position).get("img"));
		holder.weather.setText(MainActivity.getForcecastData().get(position).get("weather").toString());
		holder.temperature.setText(MainActivity.getForcecastData().get(position).get("temperature").toString());
		// holder.temperature.setTypeface(face);
		holder.wind.setText(MainActivity.getForcecastData().get(position).get("wind").toString());
		return convertView;
	}

	class ViewHolder {
		TextView date;
		ImageView img;
		TextView weather;
		TextView temperature;
		TextView wind;
	}

}