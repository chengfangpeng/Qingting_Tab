package com.cnwir.gongxin.db;

import com.lidroid.xutils.DbUtils;

import android.content.Context;

public class DbHelper {
	
	private static DbUtils db;
	private static final String DBUSERINFO_NAME = "db_userinfo_name";
	
	public static DbUtils getDbUtils(Context context){
		if(db == null){
			
			 db = DbUtils.create(context, DBUSERINFO_NAME);
		}
		return db;
		
	} 

}
