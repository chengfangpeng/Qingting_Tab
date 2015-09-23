package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class ExpandbleListViewAdapter extends BaseAdapter {

	private List<QAppInfo> datas;
	private Context context;
	private BitmapUtils bitmapUtils;
	// 区别那个分类
	private String category;

	public ExpandbleListViewAdapter(Context context, List<QAppInfo> data,
			String category) {
		this.datas = data;
		if (this.datas == null)
			this.datas = new ArrayList<QAppInfo>();
		this.context = context;
		this.category = category;
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
					R.layout.expandable_list_item, null);
			holder.all = convertView.findViewById(R.id.header_all);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.bar = (RatingBar) convertView.findViewById(R.id.ratingbar);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
			holder.toggleImageView = (ImageView) convertView.findViewById(R.id.expandable_toggle_button);
			holder.expandable = (LinearLayout) convertView.findViewById(R.id.expandable);

			holder.add = convertView.findViewById(R.id.add);
			holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.detail = convertView.findViewById(R.id.detail);
			holder.comment = convertView.findViewById(R.id.comment);
			holder.share = convertView.findViewById(R.id.share);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

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

		holder.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QAppInfo info = datas.get(position);
				Intent intent = new Intent(context, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.QAPPINFO, info);
				intent.putExtras(bundle);

				context.startActivity(intent);

				((Activity) context).overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_left_out);

			}
		});
		holder.toggleImageView.setTag(holder.expandable);
		holder.toggleImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout expandable = (LinearLayout) v.getTag();
				if(expandable.getVisibility() == View.GONE){
					expandable.setVisibility(View.VISIBLE);
				}else{
					expandable.setVisibility(View.GONE);
				}
			}
		});

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
		holder.add.setTag(holder.tv_add);
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

					((Activity) context).overridePendingTransition(
							R.anim.slide_left_in, R.anim.slide_left_out);
				} else {
					datas.get(position).setCollected(true);
					info.setCollected(true);
					((TextView) v.getTag()).setText("打开");
					Toast.makeText(context, "已经成功添加到云桌面", 0).show();
					CollectUtils.collect(context, info);
				}
			}
		});

		holder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				QAppInfo qAppInfo = datas.get(position);
				ShareUtils.showShare(context, qAppInfo.getId(),
						qAppInfo.getTitle(), qAppInfo.getDesc(),
						qAppInfo.getUrl(), qAppInfo.getImage());

			}
		});

		holder.comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MyApplication.getInstance().isLogined()) {
					QAppInfo info = datas.get(position);
					Intent intent = new Intent(context, CommentActivity.class);
					intent.putExtra(Constant.QAPPINFO, info);

					context.startActivity(intent);
				} else {
					Toast.makeText(context, R.string.login_first, 0).show();
					context.startActivity(new Intent(context,
							LoginActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.slide_bottom_in, R.anim.slide_top_out);

				}

			}
		});

		if (position == datas.size() - 1) {

			System.out.println("Renqiadapter = " + System.currentTimeMillis());
		}

		return convertView;
	}

	static class ViewHolder {
		ImageView img;
		RatingBar bar;
		TextView title, desc;
		Button btn_add;
		ImageView toggleImageView;
		LinearLayout expandable;

		// ClickChangeImgView iv_collect;
		ImageView iv_add;
		TextView tv_add, tv_collect;
		View add, collect, detail, comment, share, all;
	}

}
