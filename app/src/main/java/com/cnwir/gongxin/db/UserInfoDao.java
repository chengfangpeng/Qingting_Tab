package com.cnwir.gongxin.db;

import android.content.Context;

import com.cnwir.gongxin.bean.UserInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class UserInfoDao {
	
	private Context context;
	private DbUtils db;
	//由于用户是唯一的默认ID为1
	public static final int ID = 1;
	
	
	public UserInfoDao(Context context){
		
		this.context = context;
		 db = DbHelper.getDbUtils(context);
	}
	public void saveUserInfo(UserInfo userInfo){
		
		
		try {
			
				db.save(userInfo);
				
			
			
			
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public UserInfo getUserInfo(){
		UserInfo userInfo = null;
		try {
			 userInfo = db.findById(UserInfo.class, ID);
			 
		} catch (DbException e) {
			e.printStackTrace();
		}
			
			return userInfo;
		
		
	}
	public void deleteUserInfo(UserInfo userInfo){
		
		try {
			if(userInfo != null)
			db.deleteById(UserInfo.class, ID);
			
			
		} catch (DbException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
