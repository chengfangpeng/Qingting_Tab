package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.view.PullToRefreshLayout;
import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
import com.cnwir.gongxin.view.expandablelistview.PullableListView;
import com.cnwir.gongxin.view.expandablelistview.SlideExpandableListAdapter;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 人气
 * 
 * @author Cheng.F.P
 * 
 */
public class One_RenQiFragment extends ParentFragment {

	public View module;
	private PullToRefreshLayout pull;
	private LinearLayout ll;
	private List<QAppInfo> datas;

	private ExpandbleListViewAdapter mingZhanAdapter;

	long startTime;

	long endTime;
	private View view;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {

			view = super.onCreateView(inflater, container, savedInstanceState);
		}

		threadTask();
		endTime = System.currentTimeMillis();
		System.out.println("renqi time =" + System.currentTimeMillis());
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	protected int getLayoutId() {
		return R.layout.one_ming_zhan;
	}

	@Override
	protected void setupViews(View parentView) {
		pull = (PullToRefreshLayout) parentView.findViewById(R.id.mingzhan);
		// 添加下来刷新
		pull.setOnRefreshListener(refreshListener);
		ll = (LinearLayout) parentView.findViewById(R.id.one_ming_zhan_container_ll);
	}

	@Override
	protected void initialized() {

	}

	@Override
	protected void threadTask() {

		String result = ConfigCacheUtil.getUrlCache(RequestURL.getRenqiInfo());
		if (TextUtils.isEmpty(result)) {
			getModule(RequestURL.getRenqiInfo());
		} else {
			updateView(result);
		}

	}

	private void getModule(String url) {

		JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				String result = jsonObject.toString();
				try {
					if (TextUtils.isEmpty(result)) {
						Toast.makeText(getActivity(), R.string.request_error, Toast.LENGTH_SHORT).show();
						return;
					}
					JSONObject o = new JSONObject(result);
					if (o.has("error")) {
						if (o.getInt("error") != 0) {
							Toast.makeText(getActivity(), R.string.request_error, Toast.LENGTH_SHORT).show();
							return;
						}
					}
					// 添加缓存

					ConfigCacheUtil.setUrlCache(result, RequestURL.getRenqiInfo());
					updateView(result);

				} catch (Exception e) {
					e.printStackTrace();
				}
				// 千万别忘了告诉控件刷新完毕了哦！
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.canLoadMore(false);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});
		ClientTask.getInstance(getActivity()).addToRequestQueue(request);

	}


	private void updateView(String result) {
		startTime = System.currentTimeMillis();

		try {
			JSONObject o = new JSONObject(result);
			JSONArray array = o.getJSONObject("data").getJSONArray("tp");
			int s = array.length();
			TextView title;
			View all;

			JSONObject obj = null;

			Gson gson = new Gson();
			for (int i = 0; i < s; i++) {
				datas = new ArrayList<QAppInfo>();
				obj = array.getJSONObject(i);
				module = inflater.inflate(R.layout.one_ming_zhan_model, null);
				title = (TextView) module.findViewById(R.id.tv_title);
				title.setText(obj.getString("title"));
				all = module.findViewById(R.id.ll_look_all);
				all.setTag(obj.getString("url") + "," + obj.getString("title"));
				all.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String tag = (String) v.getTag();
						startActivity(new Intent(getActivity(), CategroyActivity.class)
								.putExtra("url", tag.split(",")[0]).putExtra("title", tag.split(",")[1])
								.putExtra(Constant.COLUMN, Constant.COLUMN_POPULARITY));
						getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

					}
				});
				PullableListView listview = (PullableListView) module.findViewById(R.id.eListView);
				JSONArray d = obj.getJSONArray("list");
				int ss = d.length();
				for (int j = 0; j < ss; j++) {
					datas.add(gson.fromJson(d.get(j).toString(), QAppInfo.class));

				}
				mingZhanAdapter = new ExpandbleListViewAdapter(getActivity(), datas, null);
				listview.setAdapter(mingZhanAdapter);
				setHeight(listview);
				ll.addView(module);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	private void setHeight(ListView listView) {
		Adapter adapter = listView.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		int size = adapter.getCount();
		for (int i = 0; i < size; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (size - 1));
		listView.setLayoutParams(params);

	}

	OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			// 下拉刷新操作
			pull.refreshFinish(PullToRefreshLayout.SUCCEED);
			// getModule(RequestURL.getRenqiInfo());

		}

		@SuppressLint("HandlerLeak")
		@Override
		public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
			// 千万别忘了告诉控件加载完毕了哦！
			pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
		}
	};

}
