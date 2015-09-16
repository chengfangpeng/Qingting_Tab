package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.asyncImg.ImageDownloader;
import org.cnwir.asyncImg.OnImageDownload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.ui.CategoryFragment;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.ShareUtils;
import com.cnwir.gongxin.view.ExpandableLayoutItem;
import com.cnwir.gongxin.view.ExpandableLayoutListView;

/**
 * 名站
 * 
 * @author wangwm 2015年4月8日 上午10:06:13
 */
public class CategoryListAdapter extends BaseAdapter {

	private List<QAppInfo> datas;
	private ExpandableLayoutListView eListView;
	private Context context;
	private ImageDownloader loader;
	private ParentFragment fragment;

	public CategoryListAdapter(CategoryFragment fragment, ExpandableLayoutListView eListView, Context context) {
		if (this.datas == null)
			this.datas = new ArrayList<QAppInfo>();
		this.context = context;
		this.eListView = eListView;
		this.fragment = fragment;
		loader = new ImageDownloader();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.bar = (RatingBar) convertView.findViewById(R.id.ratingbar);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.count = (TextView) convertView.findViewById(R.id.tv_share_count);
			holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
			holder.moreView = convertView.findViewById(R.id.more);

			holder.more = convertView.findViewById(R.id.ll_more);
			holder.add = convertView.findViewById(R.id.add);
			holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.collect = convertView.findViewById(R.id.collect);
			holder.iv_collect = (ImageView) convertView.findViewById(R.id.iv_collect);
			holder.tv_collect = (TextView) convertView.findViewById(R.id.tv_collect);
			holder.detail = convertView.findViewById(R.id.detail);
			holder.comment = convertView.findViewById(R.id.comment);
			holder.share = convertView.findViewById(R.id.share);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		QAppInfo info = datas.get(position);
		String url = info.getImage();
		holder.img.setTag(url);
		if (loader == null)
			loader = new ImageDownloader();
		holder.img.setImageResource(0);
		loader.imageDownload(url, holder.img, Constant.CACHE_IMG, fragment.getActivity(), new OnImageDownload() {

			@Override
			public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
				ImageView img = (ImageView) fragment.parentView.findViewWithTag(c_url);
				if (img != null) {
					img.setImageBitmap(bitmap);
					img.setTag("");
				}
			}
		});
		holder.bar.setRating(info.getStar());
		holder.title.setText(info.getTitle());
		holder.desc.setText(info.getDesc());
		holder.count.setText(info.getShare() + "次分享");

		holder.moreView.setTag(position);

		holder.moreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				eListView.pulldownClick((Integer) v.getTag());
				ExpandableLayoutItem currentExpandableLayout = (ExpandableLayoutItem) eListView.getChildAt(
						position - eListView.getFirstVisiblePosition()).findViewWithTag(
						ExpandableLayoutItem.class.getName());
				// 当expandItem出现时设置listview的高度
				if (currentExpandableLayout.isOpened()) {

					setHeight(eListView, currentExpandableLayout.getHeight());
				} else {

					setHeight(eListView, 0);
				}

			}
		});
		holder.add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "添加", Toast.LENGTH_SHORT).show();
			}
		});

		holder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				QAppInfo info = datas.get(position);
				ShareUtils.showShare(context, info.getId(),info.getTitle(), info.getDesc(), info.getUrl(),
						info.getImage());

			}
		});

		holder.detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QAppInfo qAppInfo = datas.get(position);
				Intent intent = new Intent(context, DetailActivity.class);

				intent.putExtra(Constant.QAPPINFO, qAppInfo);

				context.startActivity(intent);

				((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

			}
		});

		return convertView;
	}

	static class ViewHolder {
		ImageView img;
		RatingBar bar;
		TextView title, desc, count;
		Button btn_add;
		View moreView;

		ImageView iv_add, iv_collect;
		TextView tv_add, tv_collect;
		View add, collect, detail, comment, share, more;
	}

	private void setHeight(ListView listView, int itemHeight) {
		Adapter adapter = listView.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		int size = adapter.getCount();
		for (int i = 0; i < size; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = itemHeight + totalHeight + (listView.getDividerHeight() * (size - 1));
		listView.setLayoutParams(params);
	}

}
