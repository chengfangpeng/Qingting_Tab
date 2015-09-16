package com.cnwir.gongxin.ui.loading;

import java.util.HashMap;
import java.util.Map;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.UserInfo;
import com.cnwir.gongxin.db.UserInfoDao;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.MainActivity;
import com.cnwir.gongxin.ui.login.LoginActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.util.SharedPrefUtils;
import com.cnwir.gongxin.util.VersionManager;
import com.cnwir.gongxin.view.dialog.DialogUtils;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoadingActivity extends Activity {

	private RelativeLayout mLaunchLayout;
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);
		mLaunchLayout = (RelativeLayout) findViewById(R.id.launch);
		init();

		setListener();

	}


	private void setListener() {

		mFadeIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				// mLaunchLayout.startAnimation(mFadeInScale);
				 Intent intent = new Intent();
				 intent.setClass(LoadingActivity.this, MainActivity.class);
				 startActivity(intent);
				
				 overridePendingTransition(R.anim.welcome_fade_in,
				 R.anim.welcome_fade_out);

				finish();

			}
		});
		// mFadeInScale.setAnimationListener(new AnimationListener() {
		//
		// public void onAnimationStart(Animation animation) {
		//
		// }
		//
		// public void onAnimationRepeat(Animation animation) {
		//
		// }
		//
		// public void onAnimationEnd(Animation animation) {
		//
		// // mLaunchLayout.startAnimation(mFadeOut);
		//
		//
		// }
		// });

		// mFadeOut.setAnimationListener(new AnimationListener() {
		//
		// public void onAnimationStart(Animation animation) {
		//
		//
		// }
		//
		// public void onAnimationRepeat(Animation animation) {
		//
		// }
		//
		// public void onAnimationEnd(Animation animation) {
		//
		//
		// }
		// });

	}

	private void init() {
		initAnim();
		initData();
		mLaunchLayout.startAnimation(mFadeIn);
	}

	private void initData() {
		getHomeData();

		if (MyApplication.getInstance().isLogined()) {
			getUserInfoFromLocalServer(MyApplication.getInstance().getUserInfo().getToken());
		}

	}

	/**
	 * 获取首页的数据，存到缓存
	 * 
	 * */

	private void getHomeData() {
		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getMingZhanInfo(), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						String result = response.toString();
						try {
							if (TextUtils.isEmpty(result)) {
								return;
							}
							JSONObject o = new JSONObject(result);
							if (o.has("error")) {
								if (o.getInt("error") != 0) {
									return;
								}
							}
							// 添加缓存

							ConfigCacheUtil.setUrlCache(result, RequestURL.getMingZhanInfo());

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});

		ClientTask.getInstance(LoadingActivity.this).addToRequestQueue(request);

	}

	private void initAnim() {
		mFadeIn = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.welcome_fade_in_scale_zoom_out);
		mFadeIn.setDuration(2000);
		// mFadeInScale = AnimationUtils.loadAnimation(LoadingActivity.this,
		// R.anim.welcome_fade_out);
		// mFadeInScale.setDuration(2000);
		// mFadeOut = AnimationUtils.loadAnimation(LoadingActivity.this,
		// R.anim.welcome_fade_out);
		// mFadeOut.setDuration(1000);
	}

	/**
	 * 
	 * 通过token 从本地的服务器上获去用户的信息
	 *
	 * @param token
	 * 
	 * */

	private void getUserInfoFromLocalServer(final String token) {

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getUserInfoFromLocalServer(token), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject result) {

						System.out.println("from localserver result = " + result);

						try {
							JSONObject jsonObject = new JSONObject(result.toString());
							JSONObject jsonObject_data = (JSONObject) jsonObject.get("data");
							UserInfo userInfo = new UserInfo();
							userInfo.setNickName(jsonObject_data.getString("nickname"));
							userInfo.setHeadImgUrl(jsonObject_data.getString("headimgurl"));
							userInfo.setSex(jsonObject_data.getInt("sex") + "");
							userInfo.setToken(token);
							userInfo.setId(UserInfoDao.ID);

							JSONObject jsonObject_collect = jsonObject_data.optJSONObject("collect");
							JSONObject jsonObject_create = jsonObject_data.optJSONObject("userapp");

							JSONArray jsonArray_collect = jsonObject_collect.optJSONArray("list");
							int collectCount = jsonObject_collect.getInt("count");
							JSONArray jsonArray_create = jsonObject_create.optJSONArray("list");
							int createCount = jsonObject_create.getInt("count");
							userInfo.setCollectCount(collectCount);
							userInfo.setCreateCount(createCount);
							Map<Integer, CollectApp> collect_apps = new HashMap<Integer, CollectApp>();
							Map<Integer, CreateApp> create_apps = new HashMap<Integer, CreateApp>();
							Gson gson = new Gson();
							if (jsonArray_collect != null) {

								for (int i = 0; i < jsonArray_collect.length(); i++) {
									CollectApp app = gson.fromJson(jsonArray_collect.getJSONObject(i).toString(),
											CollectApp.class);
									collect_apps.put(app.getId(), app);

								}

							}
							if (jsonArray_create != null) {
								for (int i = 0; i < jsonArray_create.length(); i++) {

									CreateApp app = gson.fromJson(jsonArray_create.getJSONObject(i).toString(),
											CreateApp.class);
									create_apps.put(app.getId(), app);
								}

							}

							MyApplication.getInstance().saveUser(userInfo);
//							MyApplication.getInstance().setCollectAppList(collect_apps);
							MyApplication.getInstance().setCreateAppList(create_apps);
							Intent intent = new Intent(Constant.ACITON_UPDATE_USER_INFO);
							LoadingActivity.this.sendBroadcast(intent);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		ClientTask.getInstance(LoadingActivity.this).addToRequestQueue(request);

	}
}
