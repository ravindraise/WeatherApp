package com.ravindra.weather.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ravindra.weather.android.R;

public class SettingsListAdapter extends BaseAdapter
{
	private String [] mSettings_title;
	
	private String [] mSettings_sub_title;
	
	private Context mContext;
	
	public SettingsListAdapter(Context context) 
	{
		mContext=context;
		mSettings_title=mContext.getResources().getStringArray(R.array.settings_item_title);
		mSettings_sub_title=mContext.getResources().getStringArray(R.array.settings_item_subtitle);
	}
	
	@Override
	public int getCount() {
		return mSettings_title.length;
	}

	@Override
	public String getItem(int position) {
		return mSettings_title[position];
	}
	
	private String getSubItem(int position) {
		return mSettings_sub_title[position];
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
	        convertView = inflater.inflate(R.layout.settings_list_item, parent, false);
	        hoder=new ViewHoder();
	        hoder.title=(TextView) convertView.findViewById(R.id.tv_settings_title);
	        hoder.subtitle=(TextView) convertView.findViewById(R.id.tv_settings_subtitle);
	        convertView.setTag(hoder);
		}
		else
		hoder=(ViewHoder) convertView.getTag();	
		
		hoder.title.setText(getItem(position));
		
		hoder.subtitle.setText(getSubItem(position));
		
		return convertView;
	}
	
	private static class ViewHoder 
	{
		TextView title;
		
		TextView subtitle;
	}
}
