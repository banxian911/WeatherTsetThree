package com.goodweather.adapter;

import java.util.zip.Inflater;


import com.goodweather.activity.MainActivity;
import com.goodweather.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherSuggestionListAdapter extends BaseAdapter {

	private Context mContext;
	public WeatherSuggestionListAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext =mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.getSuggestionData().size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.weather_index_item, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView)convertView.findViewById(R.id.index_icon_iv);
			viewHolder.title = (TextView)convertView.findViewById(R.id.index_title_tv);
			viewHolder.desc = (TextView)convertView.findViewById(R.id.index_desc_tv);
			viewHolder.detail = (TextView)convertView.findViewById(R.id.index_detail_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.img.setImageResource((Integer)MainActivity.getSuggestionData().get(position).get("img") );
		viewHolder.title.setText((CharSequence)MainActivity.getSuggestionData().get(position).get("title"));
		viewHolder.desc.setText((CharSequence)MainActivity.getSuggestionData().get(position).get("desc"));
		viewHolder.detail.setText((CharSequence)MainActivity.getSuggestionData().get(position).get("detail"));
		
		return convertView;
	}
	class ViewHolder{
		ImageView img;
		TextView title;
		TextView desc;
		TextView detail;
		
	}

}
