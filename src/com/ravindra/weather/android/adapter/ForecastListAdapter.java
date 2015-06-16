package com.ravindra.weather.android.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ravindra.weather.android.R;
import com.ravindra.weather.android.client.VolleyController;
import com.ravindra.weather.android.model.Forecast;
import com.ravindra.weather.android.utility.Constants;
import com.ravindra.weather.android.widget.CustomNetworkImageView;

public class ForecastListAdapter extends BaseAdapter 
{
	private ArrayList<Forecast> mForecastList;
	private Context mContext;
	
	
	public ForecastListAdapter(Context context,ArrayList<Forecast> list) 
	{
		mForecastList=list;
		mContext=context;
	}
	
	public void resetAdapter(ArrayList<Forecast> list)
	{
		mForecastList=list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mForecastList.size();
	}

	@Override
	public Forecast getItem(int postion) 
	{
		return mForecastList.get(postion);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{	
		ViewHoder hoder;
		if(convertView==null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
	        convertView = inflater.inflate(R.layout.forecast_list_item, parent, false);
	        hoder=new ViewHoder();
	        hoder.image=(CustomNetworkImageView) convertView.findViewById(R.id.iv_forecast_image);
	        hoder.desc=(TextView) convertView.findViewById(R.id.tv_focast_desc);
	        hoder.temp=(TextView) convertView.findViewById(R.id.tv_forecast_temp);
	        convertView.setTag(hoder);
		}
		else
		hoder=(ViewHoder) convertView.getTag();	
		Forecast forecast=getItem(position);
		String desc=forecast.getWeather().get(0).getDescription();
		desc = desc.substring(0,1).toUpperCase(Locale.getDefault()) + desc.substring(1);
		desc=desc+" on "+getDate(forecast.getTimestamp());
		hoder.desc.setText(desc);
		hoder.temp.setText(Math.round(forecast.getTemperature().getDay())+""+(char) 0x00B0 +"C");
		
		hoder.image.setDefaultImageResId(R.drawable.forecst_icon_default);
		hoder.image.setImageUrl(Constants.FORECAST_ICON_URL+forecast.getWeather().get(0).getIcon()+".png"
								, VolleyController.getVolleyController().getImageLoader());
		return convertView;
	}
	
	private String getDate(long timestamp)
	{
		timestamp=timestamp*1000; //msec
		Date date=new Date(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d yyyy",Locale.getDefault()); 
		sdf.setTimeZone(TimeZone.getDefault()); 
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
	private static class ViewHoder 
	{
		TextView desc;
		
		TextView temp;
		
		CustomNetworkImageView image;
		
		
	}
	

}
