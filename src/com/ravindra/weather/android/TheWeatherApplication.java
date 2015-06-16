package com.ravindra.weather.android;

import com.ravindra.weather.android.client.VolleyController;

import android.app.Application;

public class TheWeatherApplication  extends Application
{

	@Override
	public void onCreate() {
		super.onCreate();
		VolleyController.initVolleyController(getApplicationContext());
	}
}
