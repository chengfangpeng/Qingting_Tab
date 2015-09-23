package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.view.PullToRefreshLayout;
import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
import com.cnwir.gongxin.view.expandablelistview.PullableListView;
import com.cnwir.gongxin.view.expandablelistview.SlideExpandableListAdapter;
import com.google.gson.Gson;

public class CategroyActivity extends BaseActivity implements OnClickListener {

	private PullableListView listview;
	private List<QAppInfo> datas;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	// private AbLoadDialogFragment mDialogFragment = null;
	private ExpandbleListViewAdapter adapter;
	private String url;
	private PullToRefreshLayout pull;
	private int start;
	private boolean isAll;
	private boolean isFirstIn = true;
	private ImageView return_back;
	private String title;
	private TextView titleTv;
	private int column;

	@Override
	protected void loadViewLayout() {

		setContentView(R.layout.category_activity_list);

	}

	@Override
	protected void processLogic() {
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
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		column = getIntent().getIntExtra(Constant.COLUMN, 0);
		titleTv.setText(title);
		datas = new ArrayList<QAppInfo>();

		adapter = new ExpandbleListViewAdapter(CategroyActivity.this, datas, null);

		listview.setAdapter(adapter);

	}

	@Override
	protected void findViewById() {

		pull = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		pull.setOnRefreshListener(new MyListener());
		listview = (PullableListView) findViewById(R.id.eListView);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		return_back = (ImageView) findViewById(R.id.return_back);
		return_back.setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.tv_title_text);
	}

	private void getData(String key, final PullToRefreshLayout pullToRefreshLayout) {
		datas = new ArrayList<QAppInfo>();
		String url = "";
		switch (column) {
		case Constant.COLUMN_POPULARITY:

			url = RequestURL.http.concat("/popularity/").concat(key);
			break;

		default:
			url = RequestURL.http.concat(Constant.CATEGORY).concat(key).concat(Constant.CATEGORYLIST);
			break;
		}

		if (start > 0)
			url = url.concat("?start=" + start);
		JsonObjectRequest request = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
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
				if (start > 0) {
					start -= 15;
					pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
				} else
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			}
		});
		ClientTask.getInstance(CategroyActivity.this).addToRequestQueue(request);
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
				Toast.makeText(CategroyActivity.this, getString(R.string.load_all_data), Toast.LENGTH_SHORT).show();
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
		case R.id.return_back:
			mfinish();

			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		mfinish();
	}

}
