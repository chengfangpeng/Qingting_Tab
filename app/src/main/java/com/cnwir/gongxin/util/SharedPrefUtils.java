package com.cnwir.gongxin.util;


import com.cnwir.gongxin.BuildConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class SharedPrefUtils {
	
	  public static int getVersionCode(Context  context, String prefName){
	        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

	        int versionCode = -1;
	        if(sp != null && context != null){

	            versionCode = sp.getInt(prefName, -1);
	        }

	        return versionCode;

	    }


	    public static void putVersionCode(int versionCode,String prefName, Context context){

	        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
	        SharedPreferences.Editor editor = sp.edit();
	        editor.putInt(prefName, versionCode);
	        if(Build.VERSION.SDK_INT > 9){

	            editor.apply();
	        }else{

	            editor.commit();
	        }

	    }

}
