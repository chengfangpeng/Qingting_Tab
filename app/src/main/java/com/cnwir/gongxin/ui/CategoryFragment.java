package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.view.PullToRefreshLayout;
import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
import com.cnwir.gongxin.view.expandablelistview.PullableListView;
import com.cnwir.gongxin.view.expandablelistview.SlideExpandableListAdapter;
import com.google.gson.Gson;

/**
 * 分类列表
 * 
 * @author wangwm 2015年4月22日 下午2:20:29
 */
public class CategoryFragment extends ParentFragment {

	private final static String TAG = "CategoryFragment";

	private PullableListView listview;
	private List<QAppInfo> datas;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ExpandbleListViewAdapter adapter;
	private String url;
	private PullToRefreshLayout pull;
	private int start;
	private boolean isAll;
	
	private Activity mActivity;

	@Override
	protected int getLayoutId() {
		return R.layout.category_list_;
	}

	@Override
	protected void setupViews(View view) {
		pull = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		pull.setOnRefreshListener(new MyListener());
		listview = (PullableListView) view.findViewById(R.id.eListView);
		mAbPullToRefreshView = (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);

	}

	@Override
	protected void initialized() {
		url = (String) params[0];

		datas = new ArrayList<QAppInfo>();

		adapter = new ExpandbleListViewAdapter(mActivity, datas, null);
		listview.setAdapter(adapter);
		threadTask();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	private boolean isFirstIn = true;

	@Override
	protected void threadTask() {

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isFirstIn) {
					pull.autoRefresh();
					isFirstIn = false;
				}
			}
		}, 500);

	}

	private void getData(String key, final PullToRefreshLayout pullToRefreshLayout) {
		datas = new ArrayList<QAppInfo>();
		String url = RequestURL.http.concat(Constant.CATEGORY).concat(key).concat(Constant.CATEGORYLIST);
		if (start > 0)
			url = url.concat("?start=" + start);

		String result = ConfigCacheUtil.getUrlCache(url);
		if (TextUtils.isEmpty(result)) {
			getDataFromServer(url, pullToRefreshLayout);
			
		} else {
			updateView(result, pullToRefreshLayout);
		}

	}

	private void getDataFromServer(final String url, final PullToRefreshLayout pullToRefreshLayout) {
		JsonObjectRequest request = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				String result = response.toString();
				try {
					if (TextUtils.isEmpty(result)) {
						Toast.makeText(mActivity, R.string.request_error, 0).show();
						return;
					}
					JSONObject o = new JSONObject(result);
					if (o.has("error")) {
						if (o.getInt("error") != 0) {
							Toast.makeText(mActivity, R.string.request_error, 0).show();
							return;
						}
					}
					// 添加缓存

					ConfigCacheUtil.setUrlCache(result, url);
					updateView(result, pullToRefreshLayout);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e(TAG, error.toString());
				if (start > 0) {
					start -= 15;
					pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
				} else
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			}
		});
		ClientTask.getInstance(mActivity).addToRequestQueue(request);

	}

	private void updateView(String result, final PullToRefreshLayout pullToRefreshLayout) {
		try {
			Gson gson = new Gson();
			JSONObject response = new JSONObject(result.toString());
			if (response != null) {
				if (response.has("data")) {
					JSONArray array = response.getJSONObject("data").getJSONArray("list");
					int s = array.length();
					for (int i = 0; i < s; i++) {
						datas.add(gson.fromJson(array.getJSONObject(i).toString(), QAppInfo.class));
					}
					if (datas.size() < 15) {
						isAll = true;
					} else {
						isAll = false;
					}
					pullToRefreshLayout.canLoadMore(!isAll);
					if (start > 0) {
						adapter.addData(datas);
						pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					} else {
						adapter.updateData(datas);
						pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
					}
				}
			}
		} catch (Exception e) {
			if (start > 0) {
				start -= 15;
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
			} else
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			e.printStackTrace();
		}

	}

	private class MyListener implements OnRefreshListener {

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			// 下拉刷新操作
			start = 0;
			getData(url, pullToRefreshLayout); // 千万别忘了告诉控件刷新完毕了哦！
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			// 加载操作
			if (isAll) {
				Toast.makeText(mActivity, getString(R.string.load_all_data), Toast.LENGTH_SHORT).show();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				return;
			}
			start += 15;
			getData(url, pullToRefreshLayout);
		}

	}

}
