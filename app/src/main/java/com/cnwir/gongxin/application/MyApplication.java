
package com.cnwir.gongxin.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.CardInfo;
import com.cnwir.gongxin.bean.CardInfo_1;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.UserInfo;
import com.cnwir.gongxin.db.CollectDao;
import com.cnwir.gongxin.db.CreateAppDao;
import com.cnwir.gongxin.db.ModuleDbDao;
import com.cnwir.gongxin.db.UserInfoDao;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

public class MyApplication extends Application {

	private static MyApplication mInstance;
	public UserInfo userInfo;

	public Map<Integer, CollectApp> collectApps;

	public Map<Integer, CreateApp> createApps;

	/** 通过Tencent类访问腾讯开放的OpenAPI */
	public Tencent mTencent;

	public IWXAPI wApi;

	public ModuleDbDao db;
	private UserInfoDao userInfoDao;

	private CollectDao collectDao;

	private CreateAppDao createDao;

	public boolean cardSortIschange;

	public static MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		com.umeng.socialize.utils.Log.LOG = true;
		mInstance = this;

		db = new ModuleDbDao(getApplicationContext());
		userInfoDao = new UserInfoDao(getApplicationContext());
		collectDao = new CollectDao(getApplicationContext());
		createDao = new CreateAppDao(getApplicationContext());

		List<CardInfo> data = db.getAllCardInfo();
		if (data == null || data.size() == 0) {
			initCardInfo();
		}
	}

	/**
	 * 
	 * 判读用户是否登陆过
	 * */

	public boolean isLogined() {
		if (userInfo != null) {

			return true;
		} else if (userInfoDao.getUserInfo() != null) {

			return true;
		} else {

			return false;
		}

	}

	public void saveUser(UserInfo userInfo) {

		// 存入内存
		this.userInfo = userInfo;
		// 存入数据库
		if (userInfoDao.getUserInfo() == null) {

			userInfoDao.saveUserInfo(userInfo);
		}

	}

	/**
	 * 
	 * 获取登录用户信息
	 * */

	public UserInfo getUserInfo() {

		if (userInfo != null) {

			return userInfo;
		} else {
			userInfo = userInfoDao.getUserInfo();
			return userInfo;
		}

	}

	/**
	 * 
	 * 删除登录用户
	 * */

	public void deleteUserInfo() {

		userInfoDao.deleteUserInfo(userInfo);
		userInfo = null;
	}

	public Map<Integer, CreateApp> getCreateAppList() {
		if (createApps != null) {
			return createApps;
		} else {
			createApps = createDao.findCreatetApps();
			return createApps;
		}
	}

	public void setCreateAppList(Map<Integer, CreateApp> createAppList) {

		if (createApps == null) {
			this.createApps = new HashMap<Integer, CreateApp>();
		}

		this.createApps = createAppList;
		createDao.saveCreateApps(createApps);
	}

	public void deleteCreateApps() {

		createDao.deleteCreateApps(createApps);
		createApps = null;
	}

	public void deleteCreateApp(CreateApp app) {
		if (app == null)
			return;
		if (getCreateAppList() == null)

			return;

		createApps.remove(app.getId());
		createDao.deleteCreateApp(app);
	}

	public Map<Integer, CollectApp> getCollectAppList() {
		if (collectApps != null) {
			return collectApps;
		} else {

			return collectDao.findCollectApps();
		}

	}

	public void setCollectAppList(Map<Integer, CollectApp> collectAppList) {
		if (collectApps == null) {
			this.collectApps = new HashMap<Integer, CollectApp>();
		}

		this.collectApps = collectAppList;
		collectDao.saveCollectApps(collectApps);

	}

	public void deleteCollectAppList() {

		collectDao.deleteCollectApps(collectApps);
		collectApps = null;

	}

	public void initCardInfo() {
		List<CardInfo> cardList = new ArrayList<CardInfo>();
		CardInfo info = null;
		info = new CardInfo();
		info.setDesc(getString(R.string.text_youliao_desc));
		info.setImg(getResources().getDrawable(R.drawable.youliao_icon));
		info.setModuleKey(getText(R.string.key_youliao).toString());
		info.setModuleText(getText(R.string.text_youliao).toString());
		info.setSort(0);
		cardList.add(info);

		info = new CardInfo();
		info.setDesc(getString(R.string.text_rebo_desc));
		info.setImg(getResources().getDrawable(R.drawable.hot_icon));
		info.setModuleKey(getText(R.string.key_rebo).toString());
		info.setModuleText(getText(R.string.text_rebo).toString());
		info.setSort(2);
		cardList.add(info);

		// info = new CardInfo();
		// info.setDesc(getString(R.string.text_today_weather_desc));
		// info.setImg(getResources().getDrawable(R.drawable.weather_icon));
		// info.setModuleKey("");
		// info.setModuleText(getText(R.string.text_today_weather).toString());
		// info.setSort(4);
		// cardList.add(info);

		info = new CardInfo();
		info.setDesc(getString(R.string.text_xiaoshuo_book_desc));
		info.setImg(getResources().getDrawable(R.drawable.book_icon));
		info.setModuleKey(getText(R.string.key_xiaoshuo_book).toString());
		info.setModuleText(getText(R.string.text_xiaoshuo_book).toString());
		info.setSort(5);
		cardList.add(info);

		info = new CardInfo();
		info.setDesc(getString(R.string.text_douniyixiao_desc));
		info.setImg(getResources().getDrawable(R.drawable.happy_icon));
		info.setModuleKey(getText(R.string.key_douniyixiao).toString());
		info.setModuleText(getText(R.string.text_douniyixiao).toString());
		info.setSort(3);
		cardList.add(info);

		info = new CardInfo();
		info.setDesc(getString(R.string.text_zhainan_fuli_desc));
		info.setImg(getResources().getDrawable(R.drawable.lady_icon));
		info.setModuleKey(getText(R.string.key_zhainan_fuli).toString());
		info.setModuleText(getText(R.string.text_zhainan_fuli).toString());
		info.setSort(7);
		cardList.add(info);

		info = new CardInfo();
		info.setDesc(getString(R.string.text_haibao_desc));
		info.setImg(getResources().getDrawable(R.drawable.haibao_icon));
		info.setModuleKey(getText(R.string.key_haibao).toString());
		info.setModuleText(getText(R.string.text_haibao).toString());
		info.setSort(1);
		cardList.add(info);

		info = new CardInfo();
		info.setDesc(getString(R.string.text_yulu_desc));
		info.setImg(getResources().getDrawable(R.drawable.yulu_icon));
		info.setModuleKey(getText(R.string.key_yulu).toString());
		info.setModuleText(getText(R.string.text_yulu).toString());
		info.setSort(6);
		cardList.add(info);
		db.insertCard(cardList);
	}

}
