package com.ravindra.weather.android.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class WeatherForecast 
{	
	@SerializedName("list")
	private ArrayList<Forecast> ForecastList;
	
	@SerializedName("count")
	private int Count;
	

	public ArrayList<Forecast> getForecastList() {
		return ForecastList;
	}

	public void setForecastList(ArrayList<Forecast> forecastList) {
		ForecastList = forecastList;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

	
}
