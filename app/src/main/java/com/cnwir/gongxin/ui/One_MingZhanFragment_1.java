package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.OneCate_Module_Adapter;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.view.dialog.DialogUtils;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分类
 * 
 * @author wangwm 2015年4月3日 上午9:59:59
 */
public class One_MingZhanFragment_1 extends ParentFragment {

	public View view;
	private LinearLayout container;
	private LayoutInflater inflater;
	private View module;
	private List<QAppInfo> data;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		if (view == null) {
			view = inflater.inflate(R.layout.one_cate, null);
			this.container = (LinearLayout) view.findViewById(R.id.one_cate_container);
		}

		threadTask();
		

		return view;
	}

	@Override
	protected void threadTask() {
		String result = ConfigCacheUtil.getUrlCache(RequestURL.getMingZhanInfo());
		if (TextUtils.isEmpty(result)) {
			getModule();
		} else {
			initView(result);
		}
	}

	private void getModule() {
		DialogUtils.showLoadDialog(getActivity());
		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getMingZhanInfo(), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						String result = response.toString();
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

							ConfigCacheUtil.setUrlCache(result, RequestURL.getMingZhanInfo());
							initView(result);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});

		ClientTask.getInstance(getActivity()).addToRequestQueue(request);

	}

	private void initView(String result) {
		DialogUtils.removeDialog(getActivity());
		try {
			JSONObject o = new JSONObject(result);
			JSONArray array = o.getJSONObject("data").getJSONArray("tp");
			int s = array.length();
			TextView title;
			View all;
			GridView gv;
			JSONObject obj = null;

			Gson gson = new Gson();
			for (int i = 0; i < s; i++) {
				data = new ArrayList<QAppInfo>();
				obj = array.getJSONObject(i);
				module = inflater.inflate(R.layout.one_cate_model, null);
				title = (TextView) module.findViewById(R.id.cate_module_title);
				title.setText(obj.getString("title"));
				all = module.findViewById(R.id.cate_module_more);
				all.setTag(obj.getString("url") + "," + obj.getString("title"));
				all.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 查看全部
						String tag = (String) v.getTag();
						startActivity(new Intent(getActivity(), CategroyActivity.class)
								.putExtra("url", tag.split(",")[0]).putExtra("title", tag.split(",")[1])
								.putExtra(Constant.COLUMN, Constant.COLUMN_POPSITE));
						getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
					}
				});
				gv = (GridView) module.findViewById(R.id.cate_module_gv);
				JSONArray d = obj.getJSONArray("list");
				int ss = d.length();
				for (int j = 0; j < ss; j++) {
					data.add(gson.fromJson(d.get(j).toString(), QAppInfo.class));
				}
				gv.setAdapter(new OneCate_Module_Adapter(getActivity(), gv, data));
				setGridViewHeight(gv, 4);
				container.addView(module);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	private void setGridViewHeight(GridView gv, int n) {
		Adapter adapter = gv.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		int s = adapter.getCount();
		for (int i = 0; i < s; i++) {
			if (i % n == 0) {
				View listItem = adapter.getView(i, null, gv);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}
		ViewGroup.LayoutParams params = gv.getLayoutParams();
		params.height = totalHeight + gv.getPaddingTop() + gv.getPaddingBottom() + 2;
		gv.setLayoutParams(params);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.one_cate;
	}

	@Override
	protected void setupViews(View parentView) {

	}

	@Override
	protected void initialized() {

	}

}
