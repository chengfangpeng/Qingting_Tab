package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.ui.MainActivity;
import com.cnwir.gongxin.ui.comment.CommentActivity;
import com.cnwir.gongxin.ui.login.LoginActivity;
import com.cnwir.gongxin.util.CollectUtils;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.ShareUtils;
import com.cnwir.gongxin.util.TextToBitmapUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 名站
 * 
 * @author wangwm 2015年4月8日 上午10:06:13
 */
public class CloudAppResultListViewAdapter extends BaseAdapter {

	private List<QAppInfo> datas;
	private Context context;
	private BitmapUtils bitmapUtils;

	public CloudAppResultListViewAdapter(Context context, List<QAppInfo> data) {
		this.datas = data;
		if (this.datas == null)
			this.datas = new ArrayList<QAppInfo>();
		this.context = context;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.defualt_app_icon);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.defualt_app_icon);
	}

	public void addData(List<QAppInfo> data) {
		if (data == null)
			return;
		this.datas.addAll(data);
		notifyDataSetChanged();
	}

	public void updateData(List<QAppInfo> data) {
		if (data == null) {
			this.datas.clear();
			notifyDataSetChanged();
			return;
		}
		this.datas = data;
		notifyDataSetChanged();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.cloudresult_list_item, null);
			holder.all = convertView.findViewById(R.id.header_all);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.bar = (RatingBar) convertView.findViewById(R.id.ratingbar);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);

			holder.add = convertView.findViewById(R.id.add);
			holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.comment = convertView.findViewById(R.id.comment);
			holder.share = convertView.findViewById(R.id.share);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.add.setVisibility(View.GONE);
		holder.iv_add.setVisibility(View.GONE);
		holder.tv_add.setVisibility(View.GONE);
		holder.comment.setVisibility(View.GONE);
		holder.share.setVisibility(View.GONE);

		datas.get(position).setCollected(
				CollectUtils.CheckIsCollected(datas.get(position)));
		QAppInfo info = datas.get(position);

		if (datas.get(position).isCollected()) {

			holder.btn_add.setText("打开");
			holder.tv_add.setText("打开");
			
		} else {
			holder.btn_add.setText("添加");
			holder.tv_add.setText("添加");
		}

		String imgUrl = info.getImage();

		if (TextUtils.isEmpty(imgUrl)) {
			holder.img.setImageBitmap(TextToBitmapUtils.textToBitmap(info
					.getTitle()));
			holder.desc.setText(info.getUrl());
		} else {
			if (imgUrl.contains("http://")) {
				bitmapUtils.display(holder.img, info.getImage());
			} else {
				bitmapUtils.display(holder.img,
						RequestURL.httpIP + info.getImage());
			}
			// bitmapUtils.display(holder.img, info.getImage());
			holder.desc.setText(info.getSummary());
		}

		holder.bar.setRating(info.getStar());
		holder.title.setText(info.getTitle());

		holder.btn_add.setTag(holder.tv_add);
		holder.btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				QAppInfo info = datas.get(position);
				if (info.isCollected()) {
					Intent intent = new Intent(context, DetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.QAPPINFO, info);
					intent.putExtras(bundle);

					context.startActivity(intent);

					((Activity) context).overridePendingTransition(
							R.anim.slide_left_in, R.anim.slide_left_out);
				} else {
					datas.get(position).setCollected(true);
					info.setCollected(true);
					((Button) v).setText("打开");
					((TextView) v.getTag()).setText("打开");
					Toast.makeText(context, "已经成功添加到云桌面", 0).show();
					CollectUtils.collect(context, info);
				}

			}
		});

		return convertView;
	}

	static class ViewHolder {
		ImageView img;
		RatingBar bar;
		TextView title, desc;
		Button btn_add;

		// ClickChangeImgView iv_collect;
		ImageView iv_add;
		TextView tv_add, tv_collect;
		View add, collect, comment, share, all;
	}

}
