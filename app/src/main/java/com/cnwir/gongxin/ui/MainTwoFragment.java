package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.TwoCategoryListAdapter;
import com.cnwir.gongxin.bean.CategoryTwoInfo;
import com.cnwir.gongxin.bean.ImgInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.view.dialog.DialogUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 分类
 * 
 * @author wangwm 2015年4月1日 下午2:42:56
 */
public class MainTwoFragment extends ParentFragment {

//	private ListView listView;

	private GridView mGridView;

	private List<CategoryTwoInfo> datas;

	private TwoCategoryListAdapter adapter;

	private ViewPager mBanner;

	private BannerAdapter mBannerAdapter;

	private ImageView[] mIndicators;

	private Timer mTimer = new Timer();

	private int mBannerPosition = 0;
	private final int FAKE_BANNER_SIZE = 100;
	private boolean mIsUserTouched = false;

	private BitmapUtils mBitmapUtils;

	private List<ImgInfo> imgInfos;

	private Activity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		return view;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.two_category;
	}

	@Override
	protected void setupViews(View parentView) {

		initBanner(parentView);

//		listView = (ListView) parentView.findViewById(R.id.category_list);
		mGridView = (GridView) parentView.findViewById(R.id.category_gv);

	}

	@Override
	protected void initialized() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onStart() {
		super.onStart();
		mBitmapUtils = new BitmapUtils(mActivity);
		threadTask();
	}

	@Override
	protected void threadTask() {
		String result = ConfigCacheUtil.getUrlCache(RequestURL.getCategory());
		if (TextUtils.isEmpty(result)) {
			getModule();
		} else {
			updateView(result);
		}
	}

	private void initBanner(View parentView) {
		mIndicators = new ImageView[] { (ImageView) parentView.findViewById(R.id.indicator1),
				(ImageView) parentView.findViewById(R.id.indicator2),
				(ImageView) parentView.findViewById(R.id.indicator3),
				(ImageView) parentView.findViewById(R.id.indicator4),
				(ImageView) parentView.findViewById(R.id.indicator5) };
		mBanner = (ViewPager) parentView.findViewById(R.id.banner);
		
		//因为在scrollview中放了listview并且重新的设置了listview的高度，但是显示后视图一直显示在视图的中间位置。于是手动的设置上边的轮播图获得焦点
		mBanner.setFocusable(true);
		mBanner.setFocusableInTouchMode(true);
		mBanner.requestFocus();
		
		String result = ConfigCacheUtil.getUrlCache(RequestURL.getJingXuanBanner("fenlei"));
		if (TextUtils.isEmpty(result)) {

			getImages();
		} else {
			updateBanner(result.toString());
		}

	}

	private void getImages() {

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getJingXuanBanner("fenlei"), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						

						updateBanner(response.toString());

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		ClientTask.getInstance(mActivity).addToRequestQueue(request);

	}

	private void updateBanner(String result) {

		JSONObject o;
		try {
			o = new JSONObject(result.toString());
			if (o.has("error"))
				if (o.getInt("error") != 0)
					return;
			ConfigCacheUtil.setUrlCache(result.toString(), RequestURL.getJingXuanBanner("fenlei"));
			JSONArray array = o.getJSONObject("data").getJSONArray("d");
			Gson gson = new Gson();
			imgInfos = new ArrayList<ImgInfo>();
			for (int i = 0; i < array.length(); i++) {
				ImgInfo imgInfo = new ImgInfo();
				imgInfo = gson.fromJson(array.getString(i).toString(), ImgInfo.class);
				imgInfos.add(imgInfo);

			}
			System.out.println("getAcitity() = " + mActivity);
			mBannerAdapter = new BannerAdapter(mActivity);
			mBanner.setAdapter(mBannerAdapter);
			mBanner.setOnPageChangeListener(mBannerAdapter);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	* 动态设置ListView的高度
	* @param listView
	*/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    if(listView == null) return;
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}

	/**
	 * 动态设置GridView的高度
	 * @param listView
	 */
	public static void setGridViewHeightBasedOnChildren(GridView listView) {
		// 获取listview的adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int col = 2;// listView.getNumColumns();
		int totalHeight = 0;
		// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
		// listAdapter.getCount()小于等于8时计算两次高度相加
		for (int i = 0; i < listAdapter.getCount(); i += col) {
			// 获取listview的每一个item
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
		}

		// 获取listview的布局参数
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// 设置高度
		params.height = totalHeight;
		// 设置margin
		((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		// 设置参数
		listView.setLayoutParams(params);
	}

	private void getModule() {

		DialogUtils.showLoadDialog(mActivity);

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getCategory(), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				if (response != null) {
					DialogUtils.removeDialog(mActivity);
					String result = response.toString();
					// 添加缓存

					ConfigCacheUtil.setUrlCache(result, RequestURL.getCategory());
					updateView(result);
				} else {
					Toast.makeText(mActivity, R.string.request_error, Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}

		, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				DialogUtils.removeDialog(mActivity);
				Toast.makeText(mActivity, R.string.request_error, Toast.LENGTH_SHORT).show();
			}
		});
		ClientTask.getInstance(mActivity).addToRequestQueue(request);

	}

	private void updateView(String result) {
		datas = new ArrayList<CategoryTwoInfo>();

		try {
			JSONObject jsonObject = new JSONObject(result.toString());
			JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("tp");
			Gson gson = new Gson();
			for (int i = 0; i < jsonArray.length(); i++) {
				CategoryTwoInfo info = new CategoryTwoInfo();

				info = gson.fromJson(jsonArray.getString(i).toString(), CategoryTwoInfo.class);
				datas.add(info);

			}
			adapter = new TwoCategoryListAdapter(mActivity, datas);
			mGridView.setAdapter(adapter);
			setGridViewHeightBasedOnChildren(mGridView);
			mGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					startActivity(new Intent(mActivity, CategoryListActivity.class).putExtra("url",
							datas.get(position).getUrl()).putExtra("title", datas.get(position).getTitle()));
					mActivity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				}
			});

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void setIndicator(int position) {
		position %= imgInfos.size();
		for (ImageView indicator : mIndicators) {
			indicator.setImageResource(R.drawable.indicator_unchecked);
		}
		mIndicators[position].setImageResource(R.drawable.indicator_checked);
	}

	private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

		private LayoutInflater mInflater;

		public BannerAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return FAKE_BANNER_SIZE;
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position %= imgInfos.size();
			View view = mInflater.inflate(R.layout.viewpager_item, container, false);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			// imageView.setImageResource(mImagesSrc[position]);
			mBitmapUtils.display(imageView, imgInfos.get(position).getImage());

			final int pos = position;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			int position = mBanner.getCurrentItem();
			if (position == 0) {
				position = imgInfos.size();
				mBanner.setCurrentItem(position, false);
			} else if (position == FAKE_BANNER_SIZE - 1) {
				position = imgInfos.size() - 1;
				mBanner.setCurrentItem(position, false);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mBannerPosition = position;
			setIndicator(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

}
