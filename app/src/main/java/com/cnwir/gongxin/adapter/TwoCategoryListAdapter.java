package com.cnwir.gongxin.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.CategoryTwoInfo;
import com.cnwir.gongxin.service.RequestURL;
import com.lidroid.xutils.BitmapUtils;

public class TwoCategoryListAdapter extends BaseAdapter {

	private List<CategoryTwoInfo> datas;

	private Context context;

	private LayoutInflater inflater;
	
	private BitmapUtils bitmapUtils;

	public TwoCategoryListAdapter(Context context, List<CategoryTwoInfo> datas) {

		this.context = context;
		this.datas = datas;
		inflater = LayoutInflater.from(context);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.two_category_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.avator = (ImageView) convertView.findViewById(R.id.img);
			
			holder.arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
			holder.title.setText(datas.get(position).getTitle());
			String imgUrl = datas.get(position).getIcon();
			
			if(!TextUtils.isEmpty(imgUrl)){
				if(imgUrl.contains("http://")){
					bitmapUtils.display(holder.avator, datas.get(position).getIcon());
				}else{
					bitmapUtils.display(holder.avator, RequestURL.httpIP + datas.get(position).getIcon());
				}
				
			}
			
			
		return convertView;
	}

	private class ViewHolder {
		TextView title;
		ImageView avator;
		ImageView arrow;
		

	}

}
