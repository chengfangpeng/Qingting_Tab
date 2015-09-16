//package com.cnwir.gongxin.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.cnwir.https.JsonGetFromHttp;
//import org.cnwir.mycache.ConfigCacheUtil;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.cnwir.gongxin.R;
//import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
//import com.cnwir.gongxin.bean.QAppInfo;
//import com.cnwir.gongxin.parentclass.ParentFragment;
//import com.cnwir.gongxin.util.Constant;
//import com.cnwir.gongxin.view.ExpandableLayoutListView;
//import com.cnwir.gongxin.view.PullToRefreshLayout;
//import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
//import com.google.gson.Gson;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * 名站
// * 
// * @author wangwm 2015年4月3日 上午9:59:59
// */
//public class One_MingZhanFragment extends ParentFragment {
//
//	private static boolean DEBUG = true;
//
//	public View module;
//	private PullToRefreshLayout pull;
//	private LinearLayout ll;
//	private List<QAppInfo> datas;
//
//	private ExpandbleListViewAdapter mingZhanAdapter;
//
//	@SuppressLint("InflateParams")
//	 @Override
//	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	 Bundle savedInstanceState) {
//	  View view = super.onCreateView(inflater, container,
//	 savedInstanceState);
//	
//	 return view;
//	 }
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		
//
//	}
//
//	@Override
//	protected int getLayoutId() {
//		return R.layout.one_ming_zhan;
//	}
//
//	@Override
//	protected void setupViews(View parentView) {
//		pull = (PullToRefreshLayout) parentView.findViewById(R.id.mingzhan);
//		pull.setOnRefreshListener(refreshListener);
//		ll = (LinearLayout) parentView.findViewById(R.id.one_ming_zhan_container_ll);
//	}
//
//	@Override
//	protected void initialized() {
//
//	}
//
//	@Override
//	protected void threadTask() {
//
//		final String url = getString(R.string.app_host).concat(Constant.SITEINDEX);
//		String result = ConfigCacheUtil.getUrlCache(url);
//		if (TextUtils.isEmpty(result)) {
//			getModule(url);
//		} else {
//			updateView(result);
//		}
//
//	}
//
//	private void getModule(String url) {
//
//		new Async().execute(url);
//
//	}
//
//	class Async extends AsyncTask<String, Integer, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//			return JsonGetFromHttp.GetDownloadJson(params[0]);
//		}
//
//		@SuppressLint("InflateParams")
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//
//			try {
//				if (TextUtils.isEmpty(result)) {
//					Toast.makeText(getActivity(), R.string.request_error, 0).show();
//					return;
//				}
//				JSONObject o = new JSONObject(result);
//				if (o.has("error")) {
//					if (o.getInt("error") != 0) {
//						Toast.makeText(getActivity(), R.string.request_error, 0).show();
//						return;
//					}
//				}
//				// 添加缓存
//
//				String url = getString(R.string.app_host).concat(Constant.SITEINDEX);
//				ConfigCacheUtil.setUrlCache(result, url);
//				updateView(result);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			// 千万别忘了告诉控件刷新完毕了哦！
//			pull.refreshFinish(PullToRefreshLayout.SUCCEED);
//			pull.canLoadMore(false);
//		}
//
//	}
//
//	private void updateView(String result) {
//		try {
//			JSONObject o = new JSONObject(result);
//			JSONArray array = o.getJSONObject("data").getJSONArray("tp");
//			int s = array.length();
//			TextView title;
//			View all;
//			ExpandableLayoutListView listview;
//			JSONObject obj = null;
//
//			Gson gson = new Gson();
//			for (int i = 0; i < s; i++) {
//				datas = new ArrayList<QAppInfo>();
//				obj = array.getJSONObject(i);
//				module = inflater.inflate(R.layout.one_ming_zhan_model, null);
//				title = (TextView) module.findViewById(R.id.tv_title);
//				title.setText(obj.getString("title"));
//				all = module.findViewById(R.id.ll_look_all);
//				all.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO 查看全部
//					}
//				});
//				listview = (ExpandableLayoutListView) module.findViewById(R.id.eListView);
//				JSONArray d = obj.getJSONArray("list");
//				int ss = d.length();
//				for (int j = 0; j < ss; j++) {
//					datas.add(gson.fromJson(d.get(j).toString(), QAppInfo.class));
//				}
//				mingZhanAdapter = new ExpandbleListViewAdapter(getActivity(),
//						datas);
//				listview.setAdapter(mingZhanAdapter);
//				setHeight(listview);
//				ll.addView(module);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private void setHeight(ListView listView) {
//		Adapter adapter = listView.getAdapter();
//		if (adapter == null) {
//			return;
//		}
//		int totalHeight = 0;
//		int size = adapter.getCount();
//		for (int i = 0; i < size; i++) {
//			View listItem = adapter.getView(i, null, listView);
//			listItem.measure(0, 0);
//			totalHeight += listItem.getMeasuredHeight();
//		}
//
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight + (listView.getDividerHeight() * (size - 1));
//		listView.setLayoutParams(params);
//	}
//
//	OnRefreshListener refreshListener = new OnRefreshListener() {
//
//		@Override
//		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//			// 下拉刷新操作
//
//			String url = getString(R.string.app_host).concat(Constant.SITEINDEX);
//			getModule(url);
//
//		}
//
//		@SuppressLint("HandlerLeak")
//		@Override
//		public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//			// 千万别忘了告诉控件加载完毕了哦！
//			pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//		}
//	};
//
//}
