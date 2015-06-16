package com.ravindra.weather.android.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.ravindra.local.fragment.stack.BaseLocalStackFragment;
import com.ravindra.weather.android.R;
import com.ravindra.weather.android.activity.WeatherActivty;
import com.ravindra.weather.android.client.VolleyController;
import com.ravindra.weather.android.geolocation.LocationData;
import com.ravindra.weather.android.model.Temperature;
import com.ravindra.weather.android.model.TodayWeatherForecast;
import com.ravindra.weather.android.model.Weather;
import com.ravindra.weather.android.model.Wind;
import com.ravindra.weather.android.utility.CardinalDirection;
import com.ravindra.weather.android.utility.Constants;
import com.ravindra.weather.android.utility.FormatUtils;

public class TodayForecastViewFragment extends BaseLocalStackFragment 
{
	public static String TAG=TodayForecastViewFragment.class.getName();
	
	private StringRequest mForecastRequest;
	
	private TodayWeatherForecast mWeatherForecast;
	
	private NetworkImageView mImageView;
	
	private String mRequestUrl;
	
	private ProgressBar mProgressBar;
	
	private LocationData mLocationData;
	
	public static BaseLocalStackFragment  newInstance()
	{
		BaseLocalStackFragment fragment=new TodayForecastViewFragment();
		fragment.setContainerViewId(R.id.content_frame);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.today_foreast_view, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{	
		super.onActivityCreated(savedInstanceState);
		mImageView=(NetworkImageView) getView().findViewById(R.id.iv_today_forecast_icon);
		mProgressBar=(ProgressBar) getView().findViewById(R.id.progressbar);
		if(getArguments().getSerializable(WeatherActivty.LOCATION_DETAILS_DATA)!=null)
			mLocationData=(LocationData) getArguments().getSerializable(WeatherActivty.LOCATION_DETAILS_DATA);
		constructRequestURL();
		getForecastData();
	}
	
	private void constructRequestURL()
	{
		mRequestUrl=Constants.FORECAST_BASE_URL+"weather?mode=json&units=metric";
		if(mLocationData==null)
		{
			mRequestUrl=mRequestUrl+"&q="+WeatherActivty.DEFAULT_CITY_LOCATION;
		}
		else
		{
			if(mLocationData.getLatitude()>0.0f)
			{	
				mRequestUrl=mRequestUrl+"&lat="+mLocationData.getLatitude()+"&lon="+mLocationData.getLongitude();
			}
			else
			{
				mRequestUrl=mRequestUrl+"&q="+mLocationData.getCity();
			}
		}
		
	}
	
	
	private void showProgressBar()
	{
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	private void hideProgressBar()
	{
		mProgressBar.setVisibility(View.INVISIBLE);
	}
	
	private void getForecastData()
	{	
		  String cachedResponse = null;
	      try
	      {
	        cachedResponse = new String(VolleyController.getVolleyController().getRequestQueue().getCache().get(mRequestUrl).data, "UTF8");
	      }
	      catch (Exception e) 
	      {
	    	  e.printStackTrace();
	    	  cachedResponse=null;
	      }
	      if(cachedResponse!=null)
	      {
	    	  parseResponse(cachedResponse);
		      cachedResponse=null;
	      }
	      else
	    	  showProgressBar();
	      
		sendHttpRequest();
	}
	
	private void sendHttpRequest()
	{
		mForecastRequest=new StringRequest(Request.Method.GET,mRequestUrl,
				new Response.Listener<String>() 
				{
            		@Override
            		public void onResponse(String response) 
            		{
            			parseResponse(response);
            			response=null;
            		}
				},
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{	
						handleErrorResponse();
					}
				})
				{
					@Override
					public com.android.volley.Request.Priority getPriority() {
						return Priority.HIGH;
					}
		
				}
		;
		mForecastRequest.setShouldCache(true);
		//5 sec is the request  time out
		mForecastRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		VolleyController.getVolleyController().getRequestQueue().add(mForecastRequest);
	}
	
	private void handleErrorResponse()
	{
		Toast.makeText(getActivity(), getString(R.string.network_error_text), Toast.LENGTH_SHORT).show();
		hideProgressBar();
	}
	
	private void parseResponse(String response)
	{	
		if(response!=null)
		{
			try
			{
				mWeatherForecast=new GsonBuilder().create().fromJson(response, TodayWeatherForecast.class);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				mWeatherForecast=null;
			}
		}
		if(mWeatherForecast!=null)
		{
			try 
			{
				updateView();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
			handleErrorResponse();
		
		hideProgressBar();
	}
	
	private void updateView()
	{	
		if(mWeatherForecast.getCityName()!=null)
			((TextView)getView().findViewById(R.id.tv_cityname)).setText(mWeatherForecast.getCityName());
		
		Weather today=mWeatherForecast.getTodayWeather().get(0);
		if(today.getDescription()!=null)
		{
			String desc=today.getDescription();
			desc = desc.substring(0,1).toUpperCase(Locale.getDefault()) + desc.substring(1);
			((TextView)getView().findViewById(R.id.tv_today_desc)).setText(desc);
		}
		
		mImageView.setDefaultImageResId(R.drawable.forecst_icon_default);
		if(today.getIcon()!=null)
			mImageView.setImageUrl(Constants.FORECAST_ICON_URL+today.getIcon(), 
					VolleyController.getVolleyController().getImageLoader());
		
		Temperature temperature=mWeatherForecast.getTemperature();
		
		if(temperature!=null)
		{
			String temp=Math.round(temperature.getTemperature())+""+(char) 0x00B0;
			((TextView)getView().findViewById(R.id.tv_today_temp)).setText(temp);
			
			String humidity=temperature.getHumidity()+"%";
			((TextView)getView().findViewById(R.id.tv_humidity)).setText(humidity);
			
			String pressure=Math.round(temperature.getPressure())+" "+"hPa";
			((TextView)getView().findViewById(R.id.tv_pressure)).setText(pressure);
		}
		
		Wind wind=mWeatherForecast.getWind();
		
		if(wind!=null)
		{
			String speed=wind.getSpeed()+" "+"m/s";
			((TextView)getView().findViewById(R.id.tv_wind_speed)).setText(speed);
			
			float degree=wind.getSpeed();
			
			if(degree>0)
			{
				String direction="";
		        CardinalDirection cd = new CardinalDirection(getActivity(),
		                FormatUtils.normalizeAngle(degree));
		        direction = cd.format();
		        ((TextView)getView().findViewById(R.id.tv_wind_direction)).setText(direction);
			}
		}
	}
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(mForecastRequest!=null)
			mForecastRequest.cancel();
		mForecastRequest=null;
		mWeatherForecast=null;
	}

}
