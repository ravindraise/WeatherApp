package com.ravindra.weather.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ravindra.local.fragment.stack.BaseLocalStackFragment;
import com.ravindra.weather.android.R;
import com.ravindra.weather.android.activity.WeatherActivty;
import com.ravindra.weather.android.adapter.SettingsListAdapter;

public class SettingsViewFragment  extends BaseLocalStackFragment
{	
	public static String TAG=SettingsListAdapter.class.getName();
	
	private SettingsListAdapter mAadapter;
	
	private ListView mListView;
	
	public static BaseLocalStackFragment newInstance()
	{
		BaseLocalStackFragment fragment=new SettingsViewFragment();
		fragment.setContainerViewId(R.id.content_frame);
		return fragment;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mListView=(ListView) getView().findViewById(R.id.lv_settings);
		mAadapter=new SettingsListAdapter(getActivity());
		mListView.setAdapter(mAadapter);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.settings_list_view, null);
	}
	
	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
		mAadapter=null;
	}
	@Override
	public void handleBackKey() 
	{
		super.handleBackKey();
		if(getActivity() instanceof WeatherActivty)
		((WeatherActivty)getActivity()).resetDrawerIndicator();
			getStackActivity().getLocalStack().navigateBack();
	}
}
