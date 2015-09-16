package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObservable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.create.CreateUpdateActivity;
import com.cnwir.gongxin.ui.create.CreateDetailActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.TextToBitmapUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 功能描述：创建列表listview adapter
 * 
 * @author Cheng.F.P
 * 
 * */

public class CreateListViewAdapter extends BaseAdapter {

	private Context context;
	private List<CreateApp> infos;
	private BitmapUtils bitmapUtils;
	private LayoutInflater inflater;

	public CreateListViewAdapter(Context context) {

		this.context = context;
		this.infos = new ArrayList<CreateApp>();
		bitmapUtils = new BitmapUtils(context);
		inflater = LayoutInflater.from(context);

	}

	public void addData(List<CreateApp> data) {
		if (data == null || data.size() == 0)
			return;
		infos.addAll(data);
		notifyDataSetChanged();
	}

	public void updateData(List<CreateApp> data) {
		if (data == null) {
			this.infos.clear();
			notifyDataSetChanged();
			return;
		}
		this.infos = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.create_list_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.url = (TextView) convertView.findViewById(R.id.desc);
			holder.editApp = (ImageView) convertView.findViewById(R.id.edit_app);
			holder.cancelApp = (ImageView) convertView.findViewById(R.id.cancel_app);
			holder.more_edit_cancel = (RelativeLayout) convertView.findViewById(R.id.create_more_edit);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setTag(R.id.create_more_edit, holder.more_edit_cancel);
		QAppInfo info = infos.get(position);
		holder.editApp.setTag(info);
		holder.editApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				QAppInfo info = (QAppInfo) v.getTag();
				Intent intent = new Intent(context, CreateUpdateActivity.class);
				intent.putExtra(Constant.QAPPINFO, info);
				context.startActivity(intent);
				((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

			}
		});
		holder.cancelApp.setTag(info);
		holder.cancelApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CreateApp info = (CreateApp) v.getTag();
				infos.remove(info);
				notifyDataSetChanged();
				MyApplication.getInstance().deleteCreateApp(info);
				deleteApp(info);
				

			}

		});

		holder.iv.setImageBitmap(TextToBitmapUtils.textToBitmap(info.getTitle()));
		holder.title.setText(info.getTitle());
		holder.url.setText(info.getUrl());

		return convertView;
	}

	private class ViewHolder {

		ImageView iv, editApp, cancelApp;
		TextView title, url;
		RatingBar bar;
		RelativeLayout more_edit_cancel;

	}

	private void deleteApp(QAppInfo info) {

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.deleteApp(MyApplication.getInstance()
				.getUserInfo().getToken(), info.getId()), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				System.out.println(arg0.toString());

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println(arg0.toString());

			}
		});
		ClientTask.getInstance(context).addToRequestQueue(request);

	}

}
