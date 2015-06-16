package com.ravindra.weather.android.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class CustomNetworkImageView extends NetworkImageView
{
	
	public CustomNetworkImageView(Context context)
	{
		super(context);
	}
	public CustomNetworkImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public CustomNetworkImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

}
