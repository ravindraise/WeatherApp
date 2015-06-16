package com.ravindra.weather.android.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Forecast
{
	@SerializedName("dt")
	private long Timestamp;
	
	@SerializedName("temp")
	private Temperature Temperature;
	
	@SerializedName("pressure")
	private float Pressure;
	
	@SerializedName("humidity")
	private float Humidity;
	
	@SerializedName("weather")
	private List<Weather> Weather;

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

	public float getPressure() {
		return Pressure;
	}

	public void setPressure(float pressure) {
		Pressure = pressure;
	}

	public float getHumidity() {
		return Humidity;
	}

	public void setHumidity(float humidity) {
		Humidity = humidity;
	}

	public List<Weather> getWeather() {
		return Weather;
	}

	public void setWeather(List<Weather> weather) {
		Weather = weather;
	}
	
}
