package com.ravindra.weather.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ravindra.weather.android.R;

public class DrawerListAdapter extends BaseAdapter
{
	
	private int mClickedPosition = -1;
	private Context mContext;
	private String []mListItem;
	public DrawerListAdapter(Context context,String []listItems) 
	{
		mContext=context;
		mListItem=listItems;
		mClickedPosition=0;
	}
	public void setClickedPosition(int position) {
	    mClickedPosition = position;
	}
  
  	public int getClickedPosition() {
    return mClickedPosition;
  	}
		  
	@Override
	public int getCount() {
		return mListItem.length;
	}

	@Override
	public String getItem(int position) {
		return mListItem[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder hoder;
		if(convertView==null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
	        convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
	        hoder=new ViewHolder();
	        hoder.textView=(TextView) convertView.findViewById(R.id.drawer_list_text);
	        convertView.setTag(hoder);
		}
		else
		hoder=(ViewHolder) convertView.getTag();	
		if(position==mClickedPosition)
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.drawer_list_select_item));
		else
			convertView.setBackgroundColor(Color.TRANSPARENT);
		hoder.textView.setText(mListItem[position]);
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView textView;
	}

}
