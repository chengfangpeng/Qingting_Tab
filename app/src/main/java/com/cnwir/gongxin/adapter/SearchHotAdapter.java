package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.Keyword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 热门搜索
 * @author wangwm
 * 2015年4月2日 下午2:31:40
 */
public class SearchHotAdapter extends BaseAdapter{

	private List<Keyword> datas;
	private Context context;
	
	public SearchHotAdapter(Context context, List<Keyword> datas){
		this.datas = datas;
		this.context = context;
	}
	
	public void updateData(List<Keyword> data){
		if(data==null)
			return;
		this.datas.clear();
		this.datas.addAll(data);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView ==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_hot_item, null);
			holder.key = (TextView) convertView.findViewById(R.id.tv_search_hot_item_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Keyword info = datas.get(position);
		holder.key.setText(info.getKeywords());
		return convertView;
	}

	class ViewHolder{
		TextView key;
	}
	
}
