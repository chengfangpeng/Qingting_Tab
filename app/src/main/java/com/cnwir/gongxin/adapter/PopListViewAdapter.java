package com.cnwir.gongxin.adapter;

import java.util.List;

import com.cnwir.gongxin.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PopListViewAdapter extends BaseAdapter {

	private List<String> datas;
	private LayoutInflater inflater;
	private int[] imgIds = new int[] { R.drawable.item_add, R.drawable.light_app_open_by_mtt_normal,
			R.drawable.light_app_exit_normal };

	public PopListViewAdapter(Context context, List<String> datas) {
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
			convertView = inflater.inflate(R.layout.pop_listview_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.pop_list_item_iv);
			holder.tv = (TextView) convertView.findViewById(R.id.pop_list_item_tv);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}
		
		
		holder.iv.setImageResource(imgIds[position]);
		holder.tv.setText(datas.get(position));
		return convertView;
	}

	private class ViewHolder {

		ImageView iv;
		TextView tv;

	}

}
