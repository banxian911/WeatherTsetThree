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
		//获取列表长度
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

	/**
	 * getView()有三个参数，position表示将显示的是第几 行，covertView是从布局文件中inflate来的布局。
	 * 我们写一个类来描述布局文件中的各个组件，比如ImageView，TextView 等，然后判断convertView是否为空，
	 * 如果为空就从inflate中拿到布局，并新建一个ViewHolder，然后从convertView中 拿到布局中的各个组件，
	 * 同时把ViewHolder放到tag中去，下次就不用重写new了，直接从tag中拿就可以了，然后把布局中的各个组件都设上对 应的值，
	 * 这里的Position对应到含有HashMap的List中的position。
	 */
	 
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
	//列表中的各组件
	class ViewHolder {
		TextView date;
		ImageView img;
		TextView weather;
		TextView temperature;
		TextView wind;
	}

}