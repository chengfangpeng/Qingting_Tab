package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.Keyword;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 历史搜索
 * @author wangwm
 * 2015年4月2日 下午2:31:40
 */
public class SearchHistoryAdapter extends BaseAdapter{

	private List<Keyword> datas;
	
	public SearchHistoryAdapter(){
		datas = new ArrayList<Keyword>();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, null);
			holder.key = (TextView) convertView.findViewById(R.id.search_history_key);
			holder.num = (TextView) convertView.findViewById(R.id.search_history_num);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Keyword info = datas.get(position);
		holder.key.setText(info.getKeywords());
		holder.num.setText(position+"");
		return convertView;
	}

	class ViewHolder{
		TextView key,num;
	}
	
}
