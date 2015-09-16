package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.util.AddShortCutUtils;
import com.cnwir.gongxin.util.Constant;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 功能描述：发现栏目下的GridViewAdapter
 * 
 * @author Cheng.F.P
 * 
 * */

public class FindGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<QAppInfo> infos;
	private BitmapUtils bitmapUtils;
	private LayoutInflater inflater;

	public FindGridViewAdapter(Context context) {
		this.context = context;
		infos = new ArrayList<QAppInfo>();
		bitmapUtils = new BitmapUtils(context);
		inflater = LayoutInflater.from(context);
	}
	
	public void addData(List<QAppInfo> data) {
		if (data == null || data.size() == 0)
			return;
		infos.addAll(data);
		notifyDataSetChanged();
	}

	public void updateData(List<QAppInfo> data) {
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
			convertView = inflater.inflate(R.layout.find_gridview_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.add = (TextView) convertView.findViewById(R.id.add);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		QAppInfo info = infos.get(position);
		bitmapUtils.display(holder.iv, info.getImage());
		holder.title.setText(info.getTitle());

		if (AddShortCutUtils.hasShortcut((Activity) context, info.getTitle())) {

			holder.add.setText("打开");
		} else {
			holder.add.setText("添加");

		}
		holder.add.setTag(info);
		holder.add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QAppInfo info = (QAppInfo) v.getTag();
				if (AddShortCutUtils.hasShortcut((Activity) context, info.getTitle())) {
					Intent intent = new Intent(context, DetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.QAPPINFO, info);
					intent.putExtras(bundle);

					context.startActivity(intent);

					((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				} else {

					AddShortCutUtils.addShortCut(info, context);
					((TextView) v).setText("打开");
				}
				
				
			}
		});

		return convertView;
	}

	private class ViewHolder {

		ImageView iv;
		TextView title;
		TextView add;

	}

}
