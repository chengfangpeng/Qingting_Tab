package com.cnwir.gongxin.adapter;

import java.util.List;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.R.color;
import com.cnwir.gongxin.bean.AppsItemInfo;
import com.cnwir.gongxin.bean.CategoryTwoInfo;
import com.cnwir.gongxin.service.RequestURL;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchCloudAppsListViewAdapter extends BaseAdapter{

	private List<AppsItemInfo> datas;

	private Context context;

	private LayoutInflater inflater;
	

	public SearchCloudAppsListViewAdapter(Context context, List<AppsItemInfo> datas) {

		this.context = context;
		this.datas = datas;
		inflater = LayoutInflater.from(context);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.search_cloud_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.avator = (ImageView) convertView.findViewById(R.id.img);
			
			holder.arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
			holder.title.setText(datas.get(position).getLabel());
			holder.avator.setImageDrawable(datas.get(position).getIcon());
			
			
			holder.arrow.setVisibility(View.GONE);
			
		return convertView;
	}

	private class ViewHolder {
		TextView title;
		ImageView avator;
		ImageView arrow;
		

	}

}
