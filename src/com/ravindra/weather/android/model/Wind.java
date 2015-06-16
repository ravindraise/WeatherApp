package com.ravindra.weather.android.model;

import com.google.gson.annotations.SerializedName;

public class Wind {
	
	@SerializedName("speed")
	private float Speed;
	
	@SerializedName("deg")
	private float Direction;

	public float getSpeed() {
		return Speed;
	}

	public void setSpeed(float speed) {
		Speed = speed;
	}

	public float getDirection() {
		return Direction;
	}

	public void setDirection(float direction) {
		Direction = direction;
	}
}
