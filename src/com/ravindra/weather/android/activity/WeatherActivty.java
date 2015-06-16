package com.ravindra.weather.android.activity;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ravindra.local.fragment.stack.BaseLocalStackFragment;
import com.ravindra.weather.android.R;
import com.ravindra.weather.android.adapter.DrawerListAdapter;
import com.ravindra.weather.android.dialog.AboutDialogFragment;
import com.ravindra.weather.android.fragment.ForecastViewFragment;
import com.ravindra.weather.android.fragment.SettingsViewFragment;
import com.ravindra.weather.android.fragment.TodayForecastViewFragment;
import com.ravindra.weather.android.geolocation.LocationData;
@SuppressWarnings("deprecation")
public class WeatherActivty extends BaseLocationActivity
{	
	private static final String ACTION_BAR_TITLE="ActionBarTitle";
	
	private static final String DRAWER_INDICATOR_VALUE="DrawerIndicatorValue";
	
	private static final String DRAWER_MENU_SELECTED_POSITION="DrawerMenuPosition";
	
	private static final String SETTINGS_BUTTON_CLICKED="SettingsButtonClicked";
	
	public static final String LOCATION_DETAILS_DATA="LocationData";
	
	public static final String DEFAULT_CITY_LOCATION="London,UK";
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mListTitles;
    private DrawerListAdapter mDrawerListadapter;
    private boolean isSettingsEnabled=false;
    private LocationData mLocationData;
    
    private boolean isBundleSavedInstanceNull=false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main_layout);
    	disableHardWareMenuButton();
    	mListTitles = getResources().getStringArray(R.array.left_menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerListadapter=new DrawerListAdapter(this, mListTitles);
        mDrawerList.setAdapter(mDrawerListadapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        isBundleSavedInstanceNull=false;
        if(getActionBar()!=null)
        {
        	getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        
        mDrawerToggle = new CustomActionBarDrawerToggle(
                this,                  
                mDrawerLayout,         
                R.drawable.ic_drawer, 
                R.string.drawer_open,  
                R.string.drawer_close 
                );
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_ab_up_compat);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        if(savedInstanceState==null)
        {	
        	isBundleSavedInstanceNull=true;
        	findViewById(R.id.content_loading_progressbar).setVisibility(View.VISIBLE);
        	//Fragment will be added once it receives the location updates
        }
        else
        {	
        	//restore the previous state values
        	findViewById(R.id.content_loading_progressbar).setVisibility(View.GONE);
        	setTitle(savedInstanceState.getString(ACTION_BAR_TITLE));
        	mDrawerToggle.setDrawerIndicatorEnabled(savedInstanceState.getBoolean(DRAWER_INDICATOR_VALUE));
        	mDrawerListadapter.setClickedPosition(savedInstanceState.getInt(DRAWER_MENU_SELECTED_POSITION));
        	isSettingsEnabled=savedInstanceState.getBoolean(SETTINGS_BUTTON_CLICKED, false);
        	mLocationData=(LocationData) savedInstanceState.getSerializable(LOCATION_DETAILS_DATA);
        }
    }
    // will be called by BaseLocationActivity once Location updates are done.
    @Override
    protected void onLoactionUpdateReceived()
    {	
    	if(mLocationData==null)
    		mLocationData=new LocationData();
    	
    	if(getLastKnownLocation()!=null)
    	{
    		mLocationData.setLatitude(getLastKnownLocation().getLatitude());
    		mLocationData.setLongitude(getLastKnownLocation().getLongitude());
    		mLocationData.setCity(null);
    	}
    	//If location is null then we will show weather of default city
    	else
    	{
    		mLocationData.setLatitude(0.0f);
    		mLocationData.setLongitude(0.0f);
    		mLocationData.setCity(DEFAULT_CITY_LOCATION);
    	}
    	// add the first fragment
    	if(isBundleSavedInstanceNull)
    	{
    		mDrawerToggle.setDrawerIndicatorEnabled(true);
        	selectMenuItem(0);
            setTitle(mListTitles[0]);
    	}
    	findViewById(R.id.content_loading_progressbar).setVisibility(View.GONE);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    	try
    	{
			outState.putString(ACTION_BAR_TITLE, getTitle()+"");
			outState.putBoolean(DRAWER_INDICATOR_VALUE, mDrawerToggle.isDrawerIndicatorEnabled());
			outState.putInt(DRAWER_MENU_SELECTED_POSITION, mDrawerListadapter.getClickedPosition());
			outState.putBoolean(SETTINGS_BUTTON_CLICKED,isSettingsEnabled);
			outState.putSerializable(LOCATION_DETAILS_DATA, mLocationData);
			super.onSaveInstanceState(outState);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
    	super.onRestoreInstanceState(savedInstanceState);
    	System.out.println("WeatherActivty.onRestoreInstanceState()");
    }
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    
	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle
    {

		public CustomActionBarDrawerToggle(Activity activity,DrawerLayout drawerLayout, int drawerImageRes,
				int openDrawerContentDescRes, int closeDrawerContentDescRes)
		{
			super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
					closeDrawerContentDescRes);
		}
    	
		@Override
		public void onDrawerClosed(View drawerView)
		{
			super.onDrawerClosed(drawerView);
			toggleDrawerIndicator(mListTitles[mDrawerListadapter.getClickedPosition()], true);
            invalidateOptionsMenu();
		}
		
		@Override
		public void onDrawerOpened(View drawerView) 
		{
			super.onDrawerOpened(drawerView);
			toggleDrawerIndicator(getString(R.string.app_name), false);
            invalidateOptionsMenu();
            
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) 
		{
			if(item.getItemId()==android.R.id.home)
        	{	
				if(isSettingsEnabled)
        		{
					resetDrawerIndicator();
					dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
					return true;
        		}
	        	else
	        	{
	        		toggleDrawerIndicator(mListTitles[mDrawerListadapter.getClickedPosition()], true);
					mDrawerLayout.closeDrawer(mDrawerList);
					return super.onOptionsItemSelected(item);
	        	}
        	}
			else
			return super.onOptionsItemSelected(item);
		}
    }
    
	private void toggleDrawerIndicator(String title,boolean enabled)
	{
		mDrawerToggle.setDrawerIndicatorEnabled(enabled);
		setTitle(title);
	}
	
	
    private void disableHardWareMenuButton()
    {
    	try
    	{
    		  ViewConfiguration config = ViewConfiguration.get(this);
    		  Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
    		  if (menuKeyField != null) 
    		  {
    			  menuKeyField.setAccessible(true);
    			  menuKeyField.setBoolean(config, false);
    		  }
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    	}
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {	
        	if (mDrawerListadapter.getClickedPosition() == position)
        	{
        		mDrawerLayout.closeDrawer(mDrawerList);
        	    return;
        	}
             setTitle(mListTitles[position]);
             mDrawerListadapter.setClickedPosition(position);
             mDrawerListadapter.notifyDataSetChanged();
             mDrawerLayout.closeDrawer(mDrawerList);
             selectMenuItem(position);
        }
    }
    
    
    
    private void selectMenuItem(int position)
    {
    	switch (position) {
		case 0:
			if(getLocalStack().getStack().contains(TodayForecastViewFragment.TAG))
				getLocalStack().navigateToFragment(TodayForecastViewFragment.TAG);
			else
			{
				BaseLocalStackFragment fragment=TodayForecastViewFragment.newInstance();
				Bundle bundle=new Bundle();
				bundle.putSerializable(LOCATION_DETAILS_DATA, mLocationData);
				fragment.setArguments(bundle);
				getLocalStack().addFragmentToStack(TodayForecastViewFragment.TAG, fragment, true);
			}
			break;
		case 1:
			if(getLocalStack().getStack().contains(ForecastViewFragment.TAG))
				getLocalStack().navigateToFragment(ForecastViewFragment.TAG);
			else
			{	
				BaseLocalStackFragment fragment=ForecastViewFragment.newInstance();
				Bundle bundle=new Bundle();
				bundle.putSerializable(LOCATION_DETAILS_DATA, mLocationData);
				fragment.setArguments(bundle);
				getLocalStack().addFragmentToStack(ForecastViewFragment.TAG, fragment, false);
			}
			break;
		default:
			break;
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
      //  boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) 
        {	
        	return true;
        }
        else if(item.getItemId()==R.id.action_about)
        {	
        	showAboutFragment();
        	return true;
        }
        else if(item.getItemId()==R.id.action_settings)
        {	
			toggleDrawerIndicator(getString(R.string.settings_title), false);
			isSettingsEnabled=true;
			//add settings fragment
			if(getLocalStack().getStack().contains(SettingsViewFragment.TAG))
				getLocalStack().navigateToFragment(SettingsViewFragment.TAG);
			else
				getLocalStack().addFragmentToStack(SettingsViewFragment.TAG, SettingsViewFragment.newInstance(), false);
        	return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
    
    public void resetDrawerIndicator()
    {	
    	if(isSettingsEnabled)
    	isSettingsEnabled=false;
    	toggleDrawerIndicator(mListTitles[mDrawerListadapter.getClickedPosition()], true);
    }

    public void resetDrawerMenuItem(int position)
    {	
    	setTitle(mListTitles[position]);
        mDrawerListadapter.setClickedPosition(position);
        mDrawerListadapter.notifyDataSetChanged();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    private void showAboutFragment()
    {
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = AboutDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{	
		//capturing back key event and Local stack fragment will handle the event
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
		{	
			/*
			  This method will back key event depending upon the what user has specified the action while adding the fragment to local fragment stack
			  ie. 1)If user has set  handleBackKey value to true while adding the fragment then the top fragment 
			  	   will be popped from the stack and previous fragment will be shown
			  	  2)If user has set handleBackkey value to false then local fragment stack will delegate the callback to corrosponding fragment. 
			  
			 */
			try
			{	if(getLocalStack().getStack().size()==0||getLocalStack().getStack().size()==1)
				{	
					getLocalStack().destroyStack();
					finish();
				}
				else
				getLocalStack().removeFragmentFromStack();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				finish();
			}
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
