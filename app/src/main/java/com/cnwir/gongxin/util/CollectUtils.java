package com.cnwir.gongxin.util;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.db.CollectDao;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.MainActivity;


/**
 * 
 * 功能描述：设置收藏的工具类
 * @author Cheng.F.P
 * 
 * 
 * */

public class CollectUtils {
	
	private static final String TAG = "CollectUtils";
	
	
	
	
	
	
	public static void collect(final Context context, final QAppInfo info){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				CollectApp app = new CollectApp();
				if(info != null){
									
					app.setCollect(info.getCollect());
					app.setDesc(info.getDesc());
					app.setId(info.getId());
					app.setImage(info.getImage());
					app.setShare(info.getShare());
					app.setStar(info.getStar());
					app.setSummary(info.getSummary());
					app.setTitle(info.getTitle());
					app.setType(info.getType());
					app.setUrl(info.getUrl());
					app.setCollected(info.isCollected());
				}
				Map<Integer, CollectApp> apps = MyApplication.getInstance().getCollectAppList();
				if(app.isCollected()){
					
					apps.put(app.getId(), app);
					
				}else{
					
					apps.remove(app.getId());
				}
				MyApplication.getInstance().setCollectAppList(apps);
				Intent intent = new Intent(Constant.ACITON_UPDATE_USER_INFO);
				context.sendBroadcast(intent);
				
				
				
				
			}
		}).start();
		
		
//		JsonObjectRequest request = new JsonObjectRequest(RequestURL.addCollection(info.getId(), token), null, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject arg0) {
//				
//				
//			}
//		}, new ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError arg0) {
//			}
//		});
//		ClientTask.getInstance(context).addToRequestQueue(request);
	
		
	}
	/**
	 * 
	 * 描述：判读该应用是否被用户收藏
	 * 
	 * @param info
	 * 
	 * 
	 * */
	public static boolean CheckIsCollected(QAppInfo info) {

		boolean isExist = MyApplication.getInstance().getCollectAppList().containsKey(info.getId());

		return isExist;

	}

}
