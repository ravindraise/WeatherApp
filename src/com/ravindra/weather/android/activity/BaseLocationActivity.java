package com.ravindra.weather.android.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ravindra.local.fragment.stack.BaseLocalStackActivity;

public abstract class BaseLocationActivity  extends BaseLocalStackActivity implements ConnectionCallbacks, OnConnectionFailedListener , LocationListener
{
	 /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    
    private Handler mHandler;
    
    @Override
    protected void onCreate(Bundle bundle) 
    {
    	super.onCreate(bundle);
    	mHandler=new Handler();
    	if(checkPlayServices()&&checkLocationEnabled())
    	{
    		buildGoogleApiClient();
    	}
    	else
    	{
    		mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() 
				{
					onLoactionUpdateReceived();
				}
			}, 1000);
    	}
    	 
    }
    
    protected boolean checkLocationEnabled()
    {
    	boolean gps_enabled = false,network_enabled = false;
    	LocationManager lm = null;
        if(lm==null)
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try
        {
        	gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){}
        try
        {
        	network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}
        lm=null;
        
        if(!gps_enabled&&!network_enabled)
        	return false;
        else
        	return true;
        
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null)
        mGoogleApiClient.connect();
    }

    
    /*@Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }
*/
    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient!=null)
        mGoogleApiClient.disconnect();
    }

    private boolean checkPlayServices()
    {
		
    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS)
			return false;
		return true;
	}

    
    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint)
    {
        // Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation==null)
        {
        	createLocationRequest();
        	startLocationUpdates();
        }
        else
        	onLoactionUpdateReceived();
        
    }
    
    @Override
    public void onConnectionFailed(ConnectionResult result) {
    	System.out.println("BaseLocationActivity.onConnectionFailed()");
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
    }
    
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
    	 if(mGoogleApiClient!=null)
        mGoogleApiClient.connect();
    }
    
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        
    }
    
    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
    
    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        stopLocationUpdates();
        onLoactionUpdateReceived();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	try
    	{
			if(mHandler!=null)
				mHandler.removeCallbacksAndMessages(null);
			mHandler=null;
		}
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    }
    
    protected Location getLastKnownLocation()
    {
    	return mLastLocation;
    }
    
    protected abstract void onLoactionUpdateReceived();
}
