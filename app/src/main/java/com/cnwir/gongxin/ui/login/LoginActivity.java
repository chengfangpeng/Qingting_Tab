package com.cnwir.gongxin.ui.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.UserInfo;
import com.cnwir.gongxin.db.UserInfoDao;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.NormalPostRequest;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.BaseActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.wxapi.Constants;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 登录选择
 * 
 * @author wangwm 2015年4月1日 下午6:41:06
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements IWXAPIEventHandler,RippleView.OnRippleCompleteListener {

	private static final String TAG = "LoginActivity";

	private UpdateUserInfoReceiver receiver;

	private RippleView weixinRv, qqRv, weiboRv, returnRv;
	// 友盟第三方登录
	UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

	@Override
	public void onComplete(RippleView rippleView) {

		switch (rippleView.getId()) {
			case R.id.login_by_qq:
				// onClickLogin();
				// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this, "100424468",
						"c7394704798a158208a74ab60104f0ba");
				qqSsoHandler.addToSocialSDK();
				login(SHARE_MEDIA.QQ);
				break;
			case R.id.login_by_weibo:
				// initSina();
				// 设置新浪SSO handler
				mController.getConfig().setSsoHandler(new SinaSsoHandler());

				login(SHARE_MEDIA.SINA);

				break;
			case R.id.login_by_weixin:

				// 添加微信平台
				UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, Constants.APP_ID, Constants.APP_SECRET);
				wxHandler.addToSocialSDK();

				login(SHARE_MEDIA.WEIXIN);

				break;

			case R.id.login_return:
				defaultFinish();
				overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
				break;

			default:
				break;
		}

	}

	class UpdateUserInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.ACITON_UPDATE_USER_INFO.equals(action)) {// 更新用户信息
				LoginActivity.this.defaultFinish();
				overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
			}
		}
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.login);
		receiver = new UpdateUserInfoReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACITON_UPDATE_USER_INFO);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void processLogic() {
	}

	@Override
	protected void findViewById() {
		weixinRv = (RippleView) findViewById(R.id.login_by_weixin);
		qqRv = (RippleView) findViewById(R.id.login_by_qq);
		weiboRv = (RippleView) findViewById(R.id.login_by_weibo);
		returnRv = (RippleView) findViewById(R.id.login_return);

		weixinRv.setOnRippleCompleteListener(this);
		qqRv.setOnRippleCompleteListener(this);
		weiboRv.setOnRippleCompleteListener(this);
		returnRv.setOnRippleCompleteListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null)
			unregisterReceiver(receiver);
	}

	public static IWXAPI api;
	private static final String WEIXIN_SCOPE = "snsapi_userinfo";
	private static final String WEIXIN_STATE = "wchat_ziniu";


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp arg0) {

	}

	/**
	 * 获取授权平台的用户信息</br>
	 */
	private void getUserInfo(final SHARE_MEDIA platform) {
		mController.getPlatformInfo(LoginActivity.this, platform, new UMDataListener() {

			@Override
			public void onStart() {

				startProgressDialog();
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (status == 200 && info != null) {

					UserInfo userInfo = new UserInfo();

					if (SHARE_MEDIA.QQ.equals(platform)) {
						userInfo.setChannel("qq");

					} else if (SHARE_MEDIA.WEIXIN.equals(platform)) {
						userInfo.setUnionid((String) info.get("unionid"));
						userInfo.setNickName((String) info.get("nickname"));
						userInfo.setHeadImgUrl((String) info.get("headimgurl"));
						userInfo.setSex((Integer) info.get("sex") + "");
						userInfo.setChannel("weixin");

					} else if (SHARE_MEDIA.SINA.equals(platform)) {
						userInfo.setUnionid((Integer) info.get("uid") + "");
						userInfo.setNickName((String) info.get("screen_name"));
						userInfo.setHeadImgUrl((String) info.get("profile_image_url"));
						userInfo.setSex((Integer) info.get("gender") + "");
						userInfo.setChannel("weibo");
					}

					RegisterLocalServier(userInfo);

				} else {
					showShortToast("登陆失败，请重新登陆");

				}
			}
		});
	}

	private void RegisterLocalServier(UserInfo userInfo) {

		Map<String, String> mapPararms = new HashMap<String, String>();
		mapPararms.put("unionid", userInfo.getUnionid() + "");
		mapPararms.put("channel", userInfo.getChannel());
		mapPararms.put("nickname", userInfo.getNickName());
		mapPararms.put("sex", userInfo.getSex());
		mapPararms.put("headimgurl", userInfo.getHeadImgUrl());

		NormalPostRequest request = new NormalPostRequest(RequestURL.RegisterLocalServier(),
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject result) {

						try {
							JSONObject jsonObject = new JSONObject(result.toString());
							String data = (String) jsonObject.get("data");
							getUserInfoFromLocalServer(data);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						LogUtil.d(TAG, arg0.getMessage());
						showShortToast("登陆失败，请重新登陆");
					}
				}, mapPararms);
		ClientTask.getInstance(LoginActivity.this).addToRequestQueue(request);

	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(LoginActivity.this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				String uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					getUserInfo(platform);
				} else {
					Toast.makeText(LoginActivity.this, "授权失败...", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}
		});
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

						stopProgressDialog();

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
							// MyApplication.getInstance().setCollectAppList(collect_apps);
							MyApplication.getInstance().setCreateAppList(create_apps);
							Intent intent = new Intent(Constant.ACITON_UPDATE_USER_INFO);
							LoginActivity.this.sendBroadcast(intent);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		ClientTask.getInstance(LoginActivity.this).addToRequestQueue(request);

	}

}
