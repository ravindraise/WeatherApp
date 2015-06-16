package com.ravindra.weather.android.geolocation;

import java.io.Serializable;

public class LocationData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4977663779853902878L;
	private double Latitude;
	private double Longitude;
	
	private String City;
	
	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

}
