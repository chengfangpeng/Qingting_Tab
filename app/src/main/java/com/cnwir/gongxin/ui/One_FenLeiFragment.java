//package com.cnwir.gongxin.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.cnwir.https.JsonGetFromHttp;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.cnwir.gongxin.R;
//import com.cnwir.gongxin.adapter.OneCate_Module_Adapter;
//import com.cnwir.gongxin.bean.QAppInfo;
//import com.cnwir.gongxin.util.Constant;
//import com.google.gson.Gson;
//
//import android.annotation.SuppressLint;
//import android.app.Fragment;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Adapter;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.GridView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * 分类
// *
// * @author wangwm 2015年4月3日 上午9:59:59
// */
//public class One_FenLeiFragment extends Fragment {
//
//	public View view;
//	private LinearLayout container;
//	private LayoutInflater inflater;
//	private View module;
//	private List<QAppInfo> data;
//
//	@SuppressLint("InflateParams")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		this.inflater = inflater;
//		view = inflater.inflate(R.layout.one_cate, null);
//		this.container = (LinearLayout) view.findViewById(R.id.one_cate_container);
//		getModule();
//		return view;
//	}
//
//	private void getModule() {
//		String url = getString(R.string.app_host).concat(Constant.CATEGORYINDEX);
//		new Async().execute(url);
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
//			try {
//				JSONObject o = new JSONObject(result);
//				if (o.has("error")) {
//					if (o.getInt("error") != 0) {
//						return;
//					}
//				}
//				JSONArray array = o.getJSONObject("data").getJSONArray("tp");
//				int s = array.length();
//				TextView title;
//				View all;
//				GridView gv;
//				JSONObject obj = null;
//
//				Gson gson = new Gson();
//				for (int i = 0; i < s; i++) {
//					data = new ArrayList<QAppInfo>();
//					obj = array.getJSONObject(i);
//					module = inflater.inflate(R.layout.one_cate_model, null);
//					title = (TextView) module.findViewById(R.id.cate_module_title);
//					title.setText(obj.getString("title"));
//					all = module.findViewById(R.id.cate_module_more);
//					all.setTag(obj.getString("url") + "," + obj.getString("title"));
//					all.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO 查看全部
//							String tag = (String) v.getTag();
//							startActivity(new Intent(getActivity(), CategoryListActivity.class).putExtra("url",
//									tag.split(",")[0]).putExtra("title", tag.split(",")[1]));
//							getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//						}
//					});
//					gv = (GridView) module.findViewById(R.id.cate_module_gv);
//					gv.setOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//							Intent intent = new Intent(getActivity(), DetailActivity.class);
//							Bundle bundle = new Bundle();
//							bundle.putSerializable(Constant.QAPPINFO, data.get(position));
//							intent.putExtras(bundle);
//
//							getActivity().startActivity(intent);
//
//							getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//
//						}
//					});
//					JSONArray d = obj.getJSONArray("list");
//					int ss = d.length();
//					for (int j = 0; j < ss; j++) {
//						data.add(gson.fromJson(d.get(j).toString(), QAppInfo.class));
//					}
//					gv.setAdapter(new OneCate_Module_Adapter(getActivity(), gv, data));
//					setGridViewHeight(gv, 4);
//					container.addView(module);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	private void setGridViewHeight(GridView gv, int n) {
//		Adapter adapter = gv.getAdapter();
//		if (adapter == null) {
//			return;
//		}
//		int totalHeight = 0;
//		int s = adapter.getCount();
//		for (int i = 0; i < s; i++) {
//			if (i % n == 0) {
//				View listItem = adapter.getView(i, null, gv);
//				listItem.measure(0, 0);
//				totalHeight += listItem.getMeasuredHeight();
//			}
//		}
//		ViewGroup.LayoutParams params = gv.getLayoutParams();
//		params.height = totalHeight + gv.getPaddingTop() + gv.getPaddingBottom() + 2;
//		gv.setLayoutParams(params);
//	}
//
//}
