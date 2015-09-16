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
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.RequestURL;
import com.lidroid.xutils.BitmapUtils;

public class CloudDesktopGVAdapter extends BaseAdapter {

	private Context context;

	private List<QAppInfo> datas;

	private LayoutInflater inflater;

	private BitmapUtils bitmapUtils;

	public CloudDesktopGVAdapter(Context context, List<QAppInfo> datas) {

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

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (holder == null) {
			convertView = inflater.inflate(R.layout.cloud_desktop_gridview_itme, null);
			holder = new ViewHolder();
			holder.icon_iv = (ImageView) convertView.findViewById(R.id.cloud_desktop_gv_img);
			holder.title_tv = (TextView) convertView.findViewById(R.id.cloud_desktop_gv_title);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		
		holder.title_tv.setText(datas.get(position).getTitle());
		
		String imgUrl = datas.get(position).getImage();
		
		if(!TextUtils.isEmpty(imgUrl)){
			if(imgUrl.contains("http://")){
				bitmapUtils.display(holder.icon_iv, datas.get(position).getImage());
			}else{
				bitmapUtils.display(holder.icon_iv, RequestURL.httpIP + datas.get(position).getImage());
			}
			
		}

		return convertView;
	}

	private class ViewHolder {

		private ImageView icon_iv;

		private TextView title_tv;

	}

}
