package com.cnwir.gongxin.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingTabView;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.CategoryTwoInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.google.gson.Gson;

/**
 * 分类列表（二级分类）
 * 
 * @author wangwm 2015年4月22日 下午1:53:25
 */
public class CategoryListActivity extends FragmentActivity {

	private final String TAG = "CategoryListActivity";
	private AbSlidingTabView mAbSlidingTabView;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);
		String url = getIntent().getStringExtra("url");

		mAbSlidingTabView = (AbSlidingTabView) findViewById(R.id.mAbSlidingTabView);
		// 缓存数量
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(0);

		// 设置样式
		mAbSlidingTabView.setTabTextColor(getResources().getColor(R.color.tab_normal_color));
		mAbSlidingTabView.setTabSelectColor(getResources().getColor(R.color.white_color));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.category_normal_tab_bg);
		mAbSlidingTabView.setTabBackgroundResourceSelect(R.drawable.category_select_tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.color.one_bg_color);
		mAbSlidingTabView.setPadding(0, 0, 3, 0);
		
		TextView title = (TextView) findViewById(R.id.tv_title_text);
		title.setText(getIntent().getStringExtra("title"));

		findViewById(R.id.return_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
			}
		});
		getData(url);
	}

	private void getData(String key) {
		String url = getString(R.string.app_host).concat(Constant.CATEGORY).concat(key).concat(Constant.CATEGORYTWO);
		JsonObjectRequest request = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d(TAG, "分类数据--\n" + response.toString());
				try {
					Gson gson = new Gson();
					if (response != null) {
						if (response.has("data")) {
							JSONArray array = response.getJSONArray("data");
							int s = array.length();
							// 缓存数量
							mAbSlidingTabView.getViewPager().setOffscreenPageLimit(s);
							for (int i = 0; i < s; i++) {
								CategoryTwoInfo info = gson.fromJson(array.getJSONObject(i).toString(),
										CategoryTwoInfo.class);
								Fragment fragment = new CategoryFragment();
								Bundle bundle = new Bundle();
								Object[] obj = new Object[] { info.getUrl() };
								bundle.putSerializable(MainActivity.KEY, obj);
								fragment.setArguments(bundle);
								mAbSlidingTabView.addItemView(info.getTitle(), fragment);
							}
							mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e(TAG, error.toString());
			}
		});
		ClientTask.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

}
