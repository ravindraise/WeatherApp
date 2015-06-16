package com.ravindra.weather.android.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.ravindra.local.fragment.stack.BaseLocalStackFragment;
import com.ravindra.weather.android.R;
import com.ravindra.weather.android.activity.WeatherActivty;
import com.ravindra.weather.android.adapter.ForecastListAdapter;
import com.ravindra.weather.android.client.VolleyController;
import com.ravindra.weather.android.geolocation.LocationData;
import com.ravindra.weather.android.model.WeatherForecast;
import com.ravindra.weather.android.utility.Constants;

public class ForecastViewFragment extends BaseLocalStackFragment implements OnRefreshListener//ListFragment 
{	
	public static String TAG=ForecastViewFragment.class.getName();
	
	private StringRequest mForecastRequest;
	private WeatherForecast mWeatherData;
	private ForecastListAdapter mAdapter;
	private String mRequestUrl;
	private ListView mListView;
	private SwipeRefreshLayout mRefreshLayout;
	private LocationData mLocationData;
	public static BaseLocalStackFragment newInstance()
	{	
		BaseLocalStackFragment fragment=new ForecastViewFragment();
		fragment.setContainerViewId(R.id.content_frame);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.forecast_list_view,null);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView=(ListView) getView().findViewById(R.id.lv_forecast);
		mRefreshLayout=(SwipeRefreshLayout) getView().findViewById(R.id.forecast_swipe_refresh_layout);
		mRefreshLayout.setColorSchemeResources(R.color.blue,R.color.purple,R.color.green,R.color.orange);
		mRefreshLayout.setOnRefreshListener(this);
		if(getArguments().getSerializable(WeatherActivty.LOCATION_DETAILS_DATA)!=null)
			mLocationData=(LocationData) getArguments().getSerializable(WeatherActivty.LOCATION_DETAILS_DATA);
		constructRequestURL();
		getForecastData();
	}
	
	private void constructRequestURL()
	{
		mRequestUrl=Constants.FORECAST_BASE_URL+"forecast/daily?mode=json&units=metric&cnt=16";
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
	
	private void getForecastData()
	{		
		//check if json is already cached
		 String cachedResponse = null;
	      try
	      {
	        cachedResponse = new String(VolleyController.getVolleyController().getRequestQueue().getCache().get(mRequestUrl).data, "UTF8");
	      }
	      catch (Exception e) 
	      {
	    	  e.printStackTrace();
	      }
	      if(cachedResponse!=null)
	      {
	    	  parseResponse(cachedResponse);
		      cachedResponse=null;
	      }
	      //show progress bar 
	      else
	      {
	    	  mRefreshLayout.post(new Runnable() {
				@Override
				public void run() 
				{
					 mRefreshLayout.setRefreshing(true);
				}
			});
	      }
	      sendHttpRequest();
	      
	}
	private void sendHttpRequest()
	{	
		//cancel the old request
		if(mForecastRequest!=null)
			mForecastRequest.cancel();
		mForecastRequest=null;
		
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

				};
		mForecastRequest.setShouldCache(true);
		//10 sec is the request  time out
		mForecastRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		VolleyController.getVolleyController().getRequestQueue().add(mForecastRequest);
	}
	
	private void handleErrorResponse()
	{
		Toast.makeText(getActivity(), getString(R.string.network_error_text), Toast.LENGTH_SHORT).show();
		 mRefreshLayout.setRefreshing(false);
	}
	
	private void parseResponse(String response)
	{
		if(response!=null)
		{
			try
			{
				mWeatherData=new GsonBuilder().create().fromJson(response, WeatherForecast.class);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				mWeatherData=null;
			}
		}
		updateView();
	}
	
	private void updateView()
	{
		if(mWeatherData!=null)
		{
			initAdapter();
		}
		else
			handleErrorResponse();
		
		mRefreshLayout.setRefreshing(false);
	}
	
	private void initAdapter()
	{	
		if(mAdapter==null)
		{
			mAdapter=new ForecastListAdapter(getActivity(), mWeatherData.getForecastList());
			mListView.setAdapter(mAdapter);
		}
		else
			mAdapter.resetAdapter(mWeatherData.getForecastList());
	}
	@Override
	public void handleBackKey() 
	{
		super.handleBackKey();
		if(mRefreshLayout!=null)
		mRefreshLayout.setRefreshing(false);
		cancelRequest();
		if(getActivity() instanceof WeatherActivty)
			((WeatherActivty)getActivity()).resetDrawerMenuItem(0);
		getStackActivity().getLocalStack().navigateBack();
	}
	private void cancelRequest()
	{
		if(mForecastRequest!=null)
			mForecastRequest.cancel();
		mForecastRequest=null;
	}
	
	@Override
	public void onDestroyView() 
	{
		super.onDestroyView();
		cancelRequest();
		mWeatherData=null;
		mAdapter=null;
		
	}

	@Override
	public void onRefresh()
	{
		mRefreshLayout.setRefreshing(true);
		sendHttpRequest();
	}
	
	
}
