package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.asyncImg.ImageDownloader;

import android.annotation.SuppressLint;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.util.CollectUtils;
import com.cnwir.gongxin.util.Constant;
import com.lidroid.xutils.BitmapUtils;

/**
 * 分类
 * 
 * @author wangwm 2015年4月17日 下午17:30:29
 */
public class OneCate_Module_Adapter extends BaseAdapter {

	private List<QAppInfo> datas;
	private ImageDownloader loader;
	private GridView gridview;
	private Context context;
	private BitmapUtils bitmapUtils;

	public OneCate_Module_Adapter(Context context, GridView gridview, List<QAppInfo> data) {
		datas = new ArrayList<QAppInfo>();
		this.context = context;
		this.gridview = gridview;
		this.datas = data;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.defualt_app_icon);
		bitmapUtils.configDefaultLoadingImage(R.drawable.defualt_app_icon);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_cate_model_item, null);
			holder.all = convertView.findViewById(R.id.all);
			holder.title = (TextView) convertView.findViewById(R.id.title);

			holder.iv = (ImageView) convertView.findViewById(R.id.img);
//			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.add = (TextView) convertView.findViewById(R.id.add);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		datas.get(position).setCollected(CollectUtils.CheckIsCollected(datas.get(position)));
		QAppInfo info = datas.get(position);

		if (datas.get(position).isCollected()) {

			holder.add.setText("打开");
			
		} else {
			holder.add.setText("添加");

		}

		bitmapUtils.display(holder.iv, info.getImage());
		holder.title.setText(info.getTitle());
//		holder.count.setText(info.getShare() + "次");

		holder.add.setTag(info);
		holder.add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				QAppInfo info = datas.get(position);
				if (info.isCollected()) {
					Intent intent = new Intent(context, DetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.QAPPINFO, info);
					intent.putExtras(bundle);

					context.startActivity(intent);

					((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				} else {
					datas.get(position).setCollected(true);
					info.setCollected(true);
					((TextView) v).setText("打开");
					Toast.makeText(context, "已经成功添加到云桌面", Toast.LENGTH_SHORT).show();
					CollectUtils.collect(context, info);
				}

			}
		});
		holder.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.QAPPINFO, datas.get(position));
				intent.putExtras(bundle);

				context.startActivity(intent);

				((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView title;
		ImageView iv;
		TextView add;
		View all;
	}


}
