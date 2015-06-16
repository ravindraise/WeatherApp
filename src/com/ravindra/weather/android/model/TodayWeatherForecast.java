package com.ravindra.weather.android.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TodayWeatherForecast {

	@SerializedName("id")
	private long CityId;
	
	@SerializedName("name")
	private String CityName;
	
	@SerializedName("weather")
	private ArrayList<Weather> TodayWeather; 
	
	@SerializedName("dt")
	private long Timestamp;
	
	@SerializedName("main")
	private Temperature Temperature;
	
	@SerializedName("wind")
	private Wind Wind;
	
	public long getCityId() {
		return CityId;
	}

	public void setCityId(long cityId) {
		CityId = cityId;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public ArrayList<Weather> getTodayWeather() {
		return TodayWeather;
	}

	public void setTodayWeather(ArrayList<Weather> todayWeather) {
		TodayWeather = todayWeather;
	}

	public long getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}

	public Temperature getTemperature() {
		return Temperature;
	}

	public void setTemperature(Temperature temperature) {
		Temperature = temperature;
	}

	public Wind getWind() {
		return Wind;
	}

	public void setWind(Wind wind) {
		Wind = wind;
	}

	
	
}
