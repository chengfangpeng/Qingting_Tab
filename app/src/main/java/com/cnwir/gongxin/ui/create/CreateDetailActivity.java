package com.cnwir.gongxin.ui.create;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.CreateListViewAdapter;
import com.cnwir.gongxin.adapter.ExpandbleListViewAdapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.view.ExpandableLayoutListView;
import com.cnwir.gongxin.view.PullToRefreshLayout;
import com.cnwir.gongxin.view.PullToRefreshLayout.OnRefreshListener;
import com.google.gson.Gson;

/**
 * 收藏列表
 * 
 * @author Cheng.F.p
 */
public class CreateDetailActivity extends Activity implements OnClickListener, OnFooterLoadListener,
		OnHeaderRefreshListener {

	private final static String TAG = "CreateDetailActivity";

	private List<CreateApp> datas;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	// private AbLoadDialogFragment mDialogFragment = null;
	private CreateListViewAdapter adapter;

	private ImageView iv_create_return;

	private Button btn_create_add;
	private ListView mListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_create);
		

	}
	@Override
	protected void onResume() {
		super.onResume();
		setupViews();
		initialized();
	}

	protected void setupViews() {
		iv_create_return = (ImageView) findViewById(R.id.iv_createdetail_return);
		iv_create_return.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.mListView);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		btn_create_add = (Button) findViewById(R.id.btn_create_add);
		btn_create_add.setOnClickListener(this);
	}

	protected void initialized() {
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		datas = new ArrayList<CreateApp>();

		adapter = new CreateListViewAdapter(CreateDetailActivity.this);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				QAppInfo info = (QAppInfo) adapter.getItem(arg2);

				Intent intent = new Intent(CreateDetailActivity.this, DetailActivity.class);

				intent.putExtra(Constant.QAPPINFO, info);

				startActivity(intent);

				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				RelativeLayout create_edit_cancel = (RelativeLayout) view.getTag(R.id.create_more_edit);
				if (create_edit_cancel.isShown()) {

					create_edit_cancel.setVisibility(View.GONE);
				} else {

					create_edit_cancel.setVisibility(View.VISIBLE);
				}

				return true;
			}
		});
		refreshTask();

	}

	private void getData() {
		datas = new ArrayList<CreateApp>();
		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getCreateAppList(MyApplication.getInstance()
				.getUserInfo().getToken(), start), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					Gson gson = new Gson();
					if (response != null) {
						if (response.has("data")) {
							JSONArray array = response.getJSONObject("data").getJSONArray("list");
							int s = array.length();
							for (int i = 0; i < s; i++) {
								datas.add(gson.fromJson(array.getJSONObject(i).toString(), CreateApp.class));
							}
							if (datas.size() < 15) {
								isAll = true;
							} else {
								isAll = false;
							}
							if (start > 0) {
								adapter.addData(datas);
							} else {
								adapter.updateData(datas);
							}
						}
					}
				} catch (Exception e) {
					if (start > 0) {
						start -= 15;
					}
				}
				// AbDialogUtil.removeDialog(getActivity());
			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e(TAG, error.toString());
				if (start > 0) {
					start -= 15;
				}
			}
		});
		ClientTask.getInstance(CreateDetailActivity.this).addToRequestQueue(request);
	}

	private int start;
	private boolean isAll;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_createdetail_return:

			finish();

			break;
		case R.id.btn_create_add:
			Intent intent = new Intent(CreateDetailActivity.this, CreateUpdateActivity.class);
			
			
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

		default:
			break;
		}

	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		refreshTask();

	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		loadMoreTask();

	}

	public void refreshTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void get() {
				start = 0;
				getData();
			};
		});

		mAbTask.execute(item);
	}

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void get() {
				if (!isAll) {
					start += 15;
					getData();
				}
			};
		});

		mAbTask.execute(item);
	}

}
