package com.cnwir.gongxin.ui.collect;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.BaseActivity;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.view.ExpandableLayoutListView;
import com.cnwir.gongxin.view.PullToRefreshLayout;
import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
import com.cnwir.gongxin.view.expandablelistview.PullableListView;
import com.cnwir.gongxin.view.expandablelistview.SlideExpandableListAdapter;
import com.google.gson.Gson;

/**
 * 收藏列表
 * 
 * @author Cheng.F.p
 */
public class CollectActivity extends BaseActivity implements OnClickListener {

	private final static String TAG = "CollectActivity";

	private PullableListView listview;
	private List<QAppInfo> datas;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ExpandbleListViewAdapter adapter;
	private String url;
	private PullToRefreshLayout pull;

	private ImageView iv_collect_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect);
		setupViews();
		initialized();

	}

	protected void setupViews() {
		iv_collect_return = (ImageView) findViewById(R.id.iv_collect_return);
		iv_collect_return.setOnClickListener(this);
		pull = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		pull.setOnRefreshListener(new MyListener());
		listview = (PullableListView) findViewById(R.id.eListView);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
	}

	protected void initialized() {

		datas = new ArrayList<QAppInfo>();

		adapter = new ExpandbleListViewAdapter(this, datas, null);
		listview.setAdapter(new SlideExpandableListAdapter(adapter, R.id.expandable_toggle_button, R.id.expandable));

		threadTask();
	}

	private boolean isFirstIn = true;

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
		// String url =
		// getString(R.string.app_host).concat(Constant.CATEGORY).concat(key).concat(Constant.CATEGORYLIST);
		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getCollectAppList(MyApplication.getInstance()
				.getUserInfo().getToken(), start), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d(TAG, "分类列表数据--\n" + response.toString());
				try {
					Gson gson = new Gson();
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
				// AbDialogUtil.removeDialog(getActivity());
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
		ClientTask.getInstance(CollectActivity.this).addToRequestQueue(request);
	}

	private int start;
	private boolean isAll;

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				// AbDialogUtil.removeDialog(getActivity());
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void get() {

			};
		});

		mAbTask.execute(item);
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
				Toast.makeText(CollectActivity.this, getString(R.string.load_all_data), Toast.LENGTH_SHORT).show();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				return;
			}
			start += 15;
			getData(url, pullToRefreshLayout);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_collect_return:

			finish();

			break;

		default:
			break;
		}

	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

}
