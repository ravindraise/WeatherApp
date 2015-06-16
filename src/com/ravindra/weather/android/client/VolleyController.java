package com.ravindra.weather.android.client;

import java.io.File;

import android.content.Context;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ravindra.weather.android.utility.BitmapLruCache;

public class VolleyController
{
	private RequestQueue mRequestQueue;
	private RequestQueue mBitmapRequestQueue;
	private ImageLoader mImageLoader;
	
	private static VolleyController mController;
	
	// Default maximum disk usage in bytes
	private static final int DEFAULT_DISK_USAGE_BYTES = 60 * 1024 * 1024;

	// Default cache folder name
	private static final String DEFAULT_CACHE_DIR = "photos";
		
	public static void initVolleyController(Context context)
	{
		if(mController==null)
			mController=new VolleyController(context);
	}
	
	public static VolleyController getVolleyController()
	{
		return mController;
	}
	
	private VolleyController(Context context)
	{
	     mRequestQueue = Volley.newRequestQueue(context);
	     mBitmapRequestQueue=newRequestQueue(context);
		 mImageLoader = new ImageLoader(mBitmapRequestQueue, new BitmapLruCache());
	}
	public ImageLoader getImageLoader() 
	{
		if (mImageLoader != null) 
			return mImageLoader;
		else 
			throw new IllegalStateException("ImageLoader not initialized");
	}
   public RequestQueue getRequestQueue() {
      return mRequestQueue;
   }
   
   private  RequestQueue newRequestQueue(Context context) 
	{
	    // define cache folder
	    File rootCache = context.getExternalCacheDir();
	    if (rootCache == null) 
	    {
	        rootCache = context.getCacheDir();
	    }

	    File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
	    cacheDir.mkdirs();

	    HttpStack stack = new HurlStack();
	    Network network = new BasicNetwork(stack);
	    DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
	    RequestQueue queue = new RequestQueue(diskBasedCache, network);
	    queue.start();
	    return queue;
	}
	 
}
