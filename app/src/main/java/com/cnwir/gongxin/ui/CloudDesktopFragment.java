package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.CloudDesktopGVAdapter;
import com.cnwir.gongxin.adapter.TwoCategoryListAdapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CategoryTwoInfo;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.NormalPostRequest;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.view.dialog.CloudCancelDialog;
import com.cnwir.gongxin.view.dialog.CloudCancelDialog.CloudCancelListener;
import com.google.gson.Gson;

import me.drakeet.materialdialog.MaterialDialog;

public class CloudDesktopFragment extends ParentFragment{

	private static final String TAG = CloudDesktopFragment.class.getSimpleName();

	private GridView mGridView;

	private List<QAppInfo> datas;

	private CloudDesktopGVAdapter adapter;

	private PackageManager pManager;

	private StringBuilder allAppNames;

	private List<QAppInfo> installedApps;

	private Activity mActivity;

	private ImageView image_btn;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "进入fragment");
		View view = super.onCreateView(inflater, container, savedInstanceState);

		return view;
	}
	
	/**
	 * 
	 * 因为该Fragment放在viewpager里保存的，所以当数据发生变化时再次进入并不调用onCreateView方法，但是调用onResume（）方法
	 * 所以把数据显示放到该方法中
	 * 
	 * 
	 */
	
	@Override
	public void onResume() {
		super.onResume();
		mGridView = (GridView) parentView.findViewById(R.id.cloud_gridview);
		updateView();
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = activity;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.cloud_desktop_layout;
	}

	@Override
	protected void setupViews(View parentView) {

		

	}

	@Override
	protected void initialized() {
		
	}

	@Override
	protected void threadTask() {

	}

	public void updateView() {
		datas = new ArrayList<QAppInfo>();
		Map<Integer, CollectApp> collects = MyApplication.getInstance()
				.getCollectAppList();
		List<CollectApp> collectList = new ArrayList<CollectApp>();
		for (Integer key : collects.keySet()) {
			collectList.add(collects.get(key));

		}

		datas.addAll(collectList);
		adapter = new CloudDesktopGVAdapter(mActivity, datas);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(mActivity, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.QAPPINFO, datas.get(position));
				intent.putExtras(bundle);

				mActivity.startActivity(intent);

				mActivity.overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);

			}
		});
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				final MaterialDialog mMaterialDialog = new MaterialDialog(mActivity);
				mMaterialDialog.setTitle("删除应用");
				mMaterialDialog.setMessage("你确定删除所选的应用吗？");
				mMaterialDialog.setPositiveButton("  确定  ", new OnClickListener() {
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
						datas.remove(position);

						Map<Integer, CollectApp> collects = new HashMap<Integer, CollectApp>();

						for (int i = 0; i < datas.size(); i++) {
							collects.put(datas.get(i).getId(),
									(CollectApp) datas.get(i));

						}


						MyApplication.getInstance().setCollectAppList(collects);
						adapter.notifyDataSetChanged();
					}
				});
				mMaterialDialog.setNegativeButton("  取消  ", new OnClickListener() {
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				});

				mMaterialDialog.show();
				return true;
			}
		});

	}

	

}
