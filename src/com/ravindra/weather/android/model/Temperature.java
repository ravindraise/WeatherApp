package com.ravindra.weather.android.model;

import com.google.gson.annotations.SerializedName;

public class Temperature
{
	@SerializedName("day")
	private float Day;
	
	@SerializedName("temp")
	private float Temperature;
	
	@SerializedName("min")
	private float Min;
	
	@SerializedName("max")
	private float Max;
	
	@SerializedName("night")
	private float Night;
	
	@SerializedName("eve")
	private float Evening;
	
	@SerializedName("morn")
	private float Morning;

	@SerializedName("pressure")
	private float Pressure;
	
	@SerializedName("humidity")
	private int Humidity;

	public float getDay() {
		return Day;
	}

	public void setDay(float day) {
		Day = day;
	}

	public float getTemperature() {
		return Temperature;
	}

	public void setTemperature(float temperature) {
		Temperature = temperature;
	}

	public float getMin() {
		return Min;
	}

	public void setMin(float min) {
		Min = min;
	}

	public float getMax() {
		return Max;
	}

	public void setMax(float max) {
		Max = max;
	}

	public float getNight() {
		return Night;
	}

	public void setNight(float night) {
		Night = night;
	}

	public float getEvening() {
		return Evening;
	}

	public void setEvening(float evening) {
		Evening = evening;
	}

	public float getMorning() {
		return Morning;
	}

	public void setMorning(float morning) {
		Morning = morning;
	}

	public float getPressure() {
		return Pressure;
	}

	public void setPressure(float pressure) {
		Pressure = pressure;
	}

	public int getHumidity() {
		return Humidity;
	}

	public void setHumidity(int humidity) {
		Humidity = humidity;
	}

}
