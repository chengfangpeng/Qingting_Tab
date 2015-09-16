//package com.cnwir.gongxin.ui.collect;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.os.Handler;
//import android.view.View;
//import android.widget.Toast;
//
//import com.ab.task.AbTask;
//import com.ab.task.AbTaskItem;
//import com.ab.task.AbTaskListener;
//import com.ab.view.pullview.AbPullToRefreshView;
//import com.android.volley.Response;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.cnwir.gongxin.R;
//import com.cnwir.gongxin.adapter.CategoryListAdapter;
//import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
//import com.cnwir.gongxin.application.MyApplication;
//import com.cnwir.gongxin.bean.QAppInfo;
//import com.cnwir.gongxin.parentclass.ParentFragment;
//import com.cnwir.gongxin.service.ClientTask;
//import com.cnwir.gongxin.service.RequestURL;
//import com.cnwir.gongxin.util.Constant;
//import com.cnwir.gongxin.util.LogUtil;
//import com.cnwir.gongxin.view.ExpandableLayoutListView;
//import com.cnwir.gongxin.view.PullToRefreshLayout;
//import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
//import com.google.gson.Gson;
//
///**
// * 分类列表
// * 
// * @author wangwm 2015年4月22日 下午2:20:29
// */
//public class CollectFragment extends ParentFragment {
//
//	private final static String TAG = "CollectFragment";
//
//	private ExpandableLayoutListView listview;
//	private List<QAppInfo> datas;
//	private AbPullToRefreshView mAbPullToRefreshView = null;
////	private AbLoadDialogFragment mDialogFragment = null;
//	private ExpandbleListViewAdapter adapter;
//	private String url;
//	private PullToRefreshLayout pull;
//
//	@Override
//	protected int getLayoutId() {
//		return R.layout.collect;
//	}
//
//	@Override
//	protected void setupViews(View view) {
//		pull = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
//		pull.setOnRefreshListener(new MyListener());
//		listview = (ExpandableLayoutListView) view.findViewById(R.id.eListView);
//		mAbPullToRefreshView = (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);
//	}
//
//	@Override
//	protected void initialized() {
//
//		datas = new ArrayList<QAppInfo>();
//
//		adapter = new ExpandbleListViewAdapter(CollectFragment.this, listview, getActivity(), datas);
//		listview.setAdapter(adapter);
//
//		threadTask();
//	}
//
//	private boolean isFirstIn = true;
//
//	@Override
//	protected void threadTask() {
//
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				if (isFirstIn) {
//					pull.autoRefresh();
//					isFirstIn = false;
//				}
//			}
//		}, 500);
//		
//	}
//	
//	
//
//	private void getData(String key, final PullToRefreshLayout pullToRefreshLayout) {
//		datas = new ArrayList<QAppInfo>();
////		String url = getString(R.string.app_host).concat(Constant.CATEGORY).concat(key).concat(Constant.CATEGORYLIST);
//		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getCollectAppList(MyApplication.getInstance().getUserInfo().getToken(), start), null, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				LogUtil.d(TAG, "分类列表数据--\n" + response.toString());
//				try {
//					Gson gson = new Gson();
//					if (response != null) {
//						if (response.has("data")) {
//							JSONArray array = response.getJSONObject("data").getJSONArray("list");
//							int s = array.length();
//							for (int i = 0; i < s; i++) {
//								datas.add(gson.fromJson(array.getJSONObject(i).toString(), QAppInfo.class));
//							}
//							if (datas.size() < 15) {
//								isAll = true;
//							} else {
//								isAll = false;
//							}
//							pullToRefreshLayout.canLoadMore(!isAll);
//							if (start > 0) {
//								adapter.addData(datas);
//								pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//							} else {
//								adapter.updateData(datas);
//								pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//							}
//						}
//					}
//				} catch (Exception e) {
//					if (start > 0) {
//						start -= 15;
//						pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
//					} else
//						pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
//					e.printStackTrace();
//				}
//				// AbDialogUtil.removeDialog(getActivity());
//			}
//
//		}, new Response.ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				LogUtil.e(TAG, error.toString());
//				if (start > 0) {
//					start -= 15;
//					pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
//				} else
//					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
//			}
//		});
//		ClientTask.getInstance(getActivity()).addToRequestQueue(request);
//	}
//
//	private int start;
//	private boolean isAll;
//
//	public void loadMoreTask() {
//		AbTask mAbTask = new AbTask();
//		final AbTaskItem item = new AbTaskItem();
//		item.setListener(new AbTaskListener() {
//
//			@Override
//			public void update() {
//				// AbDialogUtil.removeDialog(getActivity());
//				mAbPullToRefreshView.onFooterLoadFinish();
//			}
//
//			@Override
//			public void get() {
//
//			};
//		});
//
//		mAbTask.execute(item);
//	}
//
//	private class MyListener implements OnRefreshListener {
//
//		@Override
//		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
//			// 下拉刷新操作
//			start = 0;
//			getData(url, pullToRefreshLayout); // 千万别忘了告诉控件刷新完毕了哦！
//		}
//
//		@Override
//		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
//			// 加载操作
//			if (isAll){
//				Toast.makeText(getActivity(), getString(R.string.load_all_data), Toast.LENGTH_SHORT).show();
//				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);				
//				return;
//			}
//			start += 15;
//			getData(url, pullToRefreshLayout);
//		}
//
//	}
//
//}
