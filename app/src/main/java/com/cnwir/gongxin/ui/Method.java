package com.cnwir.gongxin.ui;

import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import android.content.Context;

public class Method {

	private final String TAG = "Method";
	private Method(){}
	
	private static Method method;
	private Context con;
	
	public static Method getInstance(Context con){
		if(method==null)
			new Method();
		method.con = con;
		return method;
	}
	
	/**
	 * 更新应用添加到桌面快捷方式的次数
	 * @author wangwm
	 * 2015年4月27日 下午1:46:52
	 * @param id  添加的应用的id
	 */
	public void updateAppAdddesktopCount(int id){
		String url = con.getString(R.string.app_host).concat("/"+id).concat(Constant.ADDTODESKTOP);
		JsonObjectRequest request = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d(TAG, "更新应用添加次数数据--\n" + response.toString());
				try {
					if (response != null) {
						if (response.has("error")) {
							int error = response.getInt("error");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e(TAG, error.toString());
			}
		});
		ClientTask.getInstance(con).addToRequestQueue(request);
	}
	/**
	 * 更新应用分享的次数
	 * @author wangwm
	 * 2015年4月27日 下午1:46:52
	 * @param id  分享的应用的id
	 */
	public void updateAppShareCount(int id){
		String url = con.getString(R.string.app_host).concat("/"+id).concat(Constant.SHARE);
		JsonObjectRequest request = new JsonObjectRequest(url, null, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d(TAG, "更新应用分享次数数据--\n" + response.toString());
				try {
					if (response != null) {
						if (response.has("error")) {
							int error = response.getInt("error");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e(TAG, error.toString());
			}
		});
		ClientTask.getInstance(con).addToRequestQueue(request);
	}
	
}
