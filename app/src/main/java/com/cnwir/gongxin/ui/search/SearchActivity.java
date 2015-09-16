package com.cnwir.gongxin.ui.search;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.SearchHistoryAdapter;
import com.cnwir.gongxin.adapter.SearchHotAdapter;
import com.cnwir.gongxin.bean.Keyword;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.BaseActivity;
import com.google.gson.Gson;

/**
 * 搜索
 * 
 * @author wangwm 2015年4月2日 上午10:52:32
 */
public class SearchActivity extends BaseActivity implements OnClickListener {

	private EditText keywordet;
	private GridView hot;
	private ListView listview;
	private SearchHotAdapter hotAdapter;
	private SearchHistoryAdapter hiAdapter;
	private List<Keyword> datas;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.search);
	}

	@Override
	protected void processLogic() {

		String result = ConfigCacheUtil.getUrlCache(RequestURL.getSearchKeyWords(0));
		if (TextUtils.isEmpty(result)) {
			getData(0);
		} else {
			updateView(result);
		}

	}

	@Override
	protected void findViewById() {
		findViewById(R.id.iv_search_return).setOnClickListener(this);
		findViewById(R.id.iv_search_del).setOnClickListener(this);
		findViewById(R.id.btn_search).setOnClickListener(this);
		findViewById(R.id.search_change).setOnClickListener(this);

		keywordet = (EditText) findViewById(R.id.et_search);
		hot = (GridView) findViewById(R.id.gv_search_hot);
		listview = (ListView) findViewById(R.id.search_history_listview);

		hiAdapter = new SearchHistoryAdapter();
		listview.setAdapter(hiAdapter);
	}

	private void getData(final int start) {

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getSearchKeyWords(start), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (response != null) {
							String result = response.toString();
							if (start == 0) {
								ConfigCacheUtil.setUrlCache(result, RequestURL.getSearchKeyWords(start));
							}

							updateView(result);

						} else {

							Toast.makeText(SearchActivity.this, R.string.request_error, Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(SearchActivity.this, R.string.request_error, Toast.LENGTH_SHORT).show();
					}
				});

		ClientTask.getInstance(SearchActivity.this).addToRequestQueue(request);
	}

	public void updateView(String result) {
		datas = new ArrayList<Keyword>();

		if (TextUtils.isEmpty(result)) {
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(result.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			Gson gson = new Gson();
			for (int i = 0; i < jsonArray.length(); i++) {
				Keyword keyword = new Keyword();
				keyword = gson.fromJson(jsonArray.get(i).toString(), Keyword.class);
				datas.add(keyword);
			}
			hotAdapter = new SearchHotAdapter(SearchActivity.this, datas);
			hot.setAdapter(hotAdapter);
			hot.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String keyword = datas.get(position).getKeywords().toString().trim();
					Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
					intent.putExtra("keywords", keyword);
					intent.putExtra("title", "搜索结果");
					keywordet.setText(keyword);
					SearchActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
					
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search_return:
			mfinish();
			break;
		case R.id.iv_search_del:
			keywordet.setText("");
			break;
		case R.id.btn_search:// 搜索
			toSearch();
			break;

		case R.id.search_change:// 换一批热门搜索

			break;

		default:
			break;
		}
	}

	private void toSearch() {
		String keyword = keywordet.getText().toString().trim();
		if(TextUtils.isEmpty(keyword)){
			Toast.makeText(SearchActivity.this, R.string.keyword_no_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
		intent.putExtra("keywords", keyword);
		intent.putExtra("title", "搜索结果");
		SearchActivity.this.startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		
		
	}

}
