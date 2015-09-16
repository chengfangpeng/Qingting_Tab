package com.cnwir.gongxin.service;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 
 * 
 * @author cfp
 * */

public class ClientTask {

	private RequestQueue mRequestQueue;
	
	private static ClientTask clientTask;
	
	private ImageLoader mImageLoader;
	
	private static Context mContext;
	
	@SuppressLint("NewApi")
	private ClientTask(Context context){
		
		mContext = context;
	        mRequestQueue = getRequestQueue();

	        mImageLoader = new ImageLoader(mRequestQueue,
	                new ImageLoader.ImageCache() {
	            private final LruCache<String, Bitmap>
	                    cache = new LruCache<String, Bitmap>(20);

	            @SuppressLint("NewApi")
				@Override
	            public Bitmap getBitmap(String url) {
	                return cache.get(url);
	            }

	            @SuppressLint("NewApi")
				@Override
	            public void putBitmap(String url, Bitmap bitmap) {
	                cache.put(url, bitmap);
	            }
	        });
	}

	public RequestQueue getRequestQueue() {
		if(mRequestQueue == null){
			mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
		}
		
		return mRequestQueue;
	}
	 public static synchronized ClientTask getInstance(Context context) {
	        if (clientTask == null) {
	        	clientTask = new ClientTask(context);
	        }
	        return clientTask;
	    }
	 public<T> void addToRequestQueue(Request<T> request){
		 getRequestQueue().add(request);
		 
	 }
	 public ImageLoader getImageLoader(){
		 return mImageLoader;
	 }
}
