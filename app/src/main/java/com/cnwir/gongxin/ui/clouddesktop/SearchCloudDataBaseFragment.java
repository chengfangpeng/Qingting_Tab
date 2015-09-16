package com.cnwir.gongxin.ui.clouddesktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.CloudDesktopGVAdapter;
import com.cnwir.gongxin.adapter.SearchCloudAppsListViewAdapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.AppsItemInfo;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.NormalPostRequest;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.view.progressbar.RateTextCircularProgressBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * 
 * @author heaven
 * 
 * 
 * 
 * */

public class SearchCloudDataBaseFragment extends Fragment {

	private ListView appsListView;

	private PackageManager pManager;

	private StringBuilder allAppNames;

	private Activity mActivity;

	private List<AppsItemInfo> allApps;

	List<PackageInfo> appList;

	private SearchCloudAppsListViewAdapter adapter;

	private RateTextCircularProgressBar mRateTextCircularProgressBar;

	private int progress = 0;

	private TextView appPackageName_tv;

	/**
	 * 
	 * 扫描了多少应用
	 * 
	 */

	private TextView appsNum_tv;
	/**
	 * 从服务器匹配成功后返回的app的json数据
	 */
	private String resultJson;

	private int progressDelay = 100;

	/**
	 * 判断是否获得数据，从而控制进度表的速度
	 */

	private boolean isGetData = false;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mRateTextCircularProgressBar.setProgress(msg.what);
			if (progress >= appList.size()) {

				// 如果获得数据跳转到数据展示页面否者跳转到空数据页面
				if (!TextUtils.isEmpty(resultJson)) {
					replaceFragment(R.id.content_frame,
							new CloudAppSearchResultFragment(), resultJson);
					mActivity.overridePendingTransition(R.anim.slide_bottom_in,
							R.anim.slide_top_out);

				} else {

					replaceFragment(R.id.content_frame,
							new CloudAppSearchResultFragment(), resultJson);
					mActivity.overridePendingTransition(R.anim.slide_bottom_in,
							R.anim.slide_top_out);
				}

				return;
			}
			PackageInfo pinfo = appList.get(msg.what);

			AppsItemInfo shareItem = new AppsItemInfo();
			// 设置图片
			shareItem.setIcon(pManager
					.getApplicationIcon(pinfo.applicationInfo));
			// 设置应用程序名字
			shareItem.setLabel(pManager.getApplicationLabel(
					pinfo.applicationInfo).toString());
			// 设置应用程序的包名
			shareItem.setPackageName(pinfo.applicationInfo.packageName);
			appPackageName_tv.setText("匹配中：" + shareItem.getPackageName());
			appsNum_tv.setText(msg.what + "款");
			allApps.add(shareItem);
			appsListView.setSelection(msg.what);
			adapter.notifyDataSetChanged();
			if (isGetData) {

				mHandler.sendEmptyMessageDelayed(++progress,
						progressDelay -= 10);
			} else {
				mHandler.sendEmptyMessageDelayed(++progress,
						progressDelay += 10);
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(
				R.layout.search_cloud_fragment_layout, null);
		initView(contentView);
		initialized();
		return contentView;
	}

	private void initView(View contentView) {

		mRateTextCircularProgressBar = (RateTextCircularProgressBar) contentView
				.findViewById(R.id.rt_circularPb);

		mRateTextCircularProgressBar.getCircularProgressBar()
				.setCircleWidth(10);
		appPackageName_tv = (TextView) contentView
				.findViewById(R.id.appPackageName);
		appsNum_tv = (TextView) contentView.findViewById(R.id.app_progress_num);
		appsListView = (ListView) contentView.findViewById(R.id.app_listView);

	}

	protected void initialized() {
		pManager = MyApplication.getInstance().getPackageManager();
		appList = getAllApps(MyApplication.getInstance());
		allAppNames = new StringBuilder();
		allApps = new ArrayList<AppsItemInfo>();

		for (int i = 0; i < appList.size(); i++) {
			PackageInfo pinfo = appList.get(i);
			// 设置应用程序名字
			String appName = pManager
					.getApplicationLabel(pinfo.applicationInfo).toString();

			if (i != appList.size() - 1) {

				allAppNames.append(appName + ",");
			} else {

				allAppNames.append(appName);
			}

		}
		mRateTextCircularProgressBar.setMax(appList.size());
		mHandler.sendEmptyMessageDelayed(0, 100);
		adapter = new SearchCloudAppsListViewAdapter(mActivity, allApps);

		appsListView.setAdapter(adapter);
		getInstallAppInfo();
	}

	public static List<PackageInfo> getAllApps(Context context) {

		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		// 获取手机内所有应用
		List<PackageInfo> packlist = pManager.getInstalledPackages(0);
		for (int i = 0; i < packlist.size(); i++) {
			PackageInfo pak = (PackageInfo) packlist.get(i);

			// 判断是否为非系统预装的应用程序
			// 这里还可以添加系统自带的，这里就先不添加了，如果有需要可以自己添加
			// if()里的值如果<=0则为自己装的程序，否则为系统工程自带
			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
				// 添加自己已经安装的应用程序
				apps.add(pak);
			}

		}
		return apps;
	}

	private void getInstallAppInfo() {

		Map<String, String> params = new HashMap<String, String>();

		params.put("appname", allAppNames.toString().trim());

		NormalPostRequest request = new NormalPostRequest(
				RequestURL.getInstallAppInfo(), new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							JSONObject obj = new JSONObject(response.toString());
							int error = obj.getInt("error");
							if (error == 0) {
								isGetData = true;
								JSONArray jsonArray = obj.getJSONArray("data");
								if (jsonArray != null) {

									resultJson = jsonArray.toString();
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						System.out.println(arg0.toString());
					}
				}, params);

		ClientTask.getInstance(mActivity).addToRequestQueue(request);

	}

	protected void replaceFragment(int viewId, Fragment fragment, String json) {
		FragmentManager fragmentManager = ((FragmentActivity) mActivity)
				.getSupportFragmentManager();
		Bundle bundle = new Bundle();
		bundle.putString("appJson", json);
		bundle.putInt("scanAppCount", allApps.size());
		
		fragment.setArguments(bundle);

		fragmentManager.beginTransaction().replace(viewId, fragment).commit();
	}

}
