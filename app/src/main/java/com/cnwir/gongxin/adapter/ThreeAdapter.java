package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.asyncImg.ImageDownloader;
import org.cnwir.asyncImg.OnImageDownload;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.ScreenUtils;

/**
 * 专题
 * 
 * @author wangwm 2015年4月23日 下午5:56:00
 */
public class ThreeAdapter extends BaseAdapter {

	private List<QAppInfo> datas;
	private ParentFragment fragment;
	private ImageDownloader loader;
	
	public ThreeAdapter(ParentFragment fragment){
		this.fragment = fragment;
		datas = new ArrayList<QAppInfo>();
	}

	public void addData(List<QAppInfo> data) {
		if (data == null || data.size() == 0)
			return;
		datas.addAll(data);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.three_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.three_img);
			holder.title = (TextView) convertView.findViewById(R.id.three_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ViewGroup.LayoutParams params = holder.img.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenWidth(fragment.getActivity())/(720/226f));
		holder.img.setLayoutParams(params);
		
		QAppInfo info = datas.get(position);
		String url = info.getImage();
		holder.img.setTag(url);
		if(loader==null)
			loader = new ImageDownloader();
		holder.img.setImageResource(0);
		loader.imageDownload(url, holder.img, Constant.CACHE_IMG, fragment.getActivity(), new OnImageDownload() {
			
			@Override
			public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
				ImageView img = (ImageView) fragment.parentView.findViewWithTag(c_url);
				if(img!=null){
					img.setImageBitmap(bitmap);
					img.setTag("");
				}
			}
		});
		holder.title.setText(info.getTitle());
		
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView title;
	}

}
