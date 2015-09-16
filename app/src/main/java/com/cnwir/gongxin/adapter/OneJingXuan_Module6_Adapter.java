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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.Jingxuan_model_item;
import com.cnwir.gongxin.ui.One_JingXuanFragment;
import com.cnwir.gongxin.util.Constant;

/**
 * 精选 -- type 6
 * 
 * @author wangwm 2015年4月14日 下午8:30:29
 */
public class OneJingXuan_Module6_Adapter extends BaseAdapter {

	private List<Jingxuan_model_item> datas;
	private One_JingXuanFragment fragment;
	private ImageDownloader loader;
	private GridView gridview;

	public OneJingXuan_Module6_Adapter(One_JingXuanFragment fragment, GridView gridview, List<Jingxuan_model_item> data) {
		datas = new ArrayList<Jingxuan_model_item>();
		this.fragment = fragment;
		this.gridview = gridview;
		this.datas = data;
	}

	@Override
	public int getCount() {
		if (datas.size() >= 4) {

			return 4;
		} else {
			return datas.size();
		}
	}

	public void updateData(List<Jingxuan_model_item> tem_datas, int index) {
		if (tem_datas != null) {
			if (index + 4 < tem_datas.size()) {
				this.datas = tem_datas.subList(index, index + 4);

			} else {

				this.datas = tem_datas.subList(index, tem_datas.size());
			}
			notifyDataSetChanged();
			return;
		}
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_jingxuan_model_6_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.iv = (ImageView) convertView.findViewById(R.id.img);
			holder.summary = (TextView) convertView.findViewById(R.id.summary);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Jingxuan_model_item info = datas.get(position);
		String url = info.image;
		holder.iv.setTag(url);

		if (loader == null)
			loader = new ImageDownloader();
		holder.iv.setImageResource(0);
		loader.imageDownload(url, holder.iv, Constant.CACHE_IMG, fragment.getActivity(), new OnImageDownload() {

			@Override
			public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
				ImageView img = (ImageView) gridview.findViewWithTag(c_url);
				if (img != null) {
					img.setImageBitmap(bitmap);
					img.setTag("");
				}
			}
		});

		holder.title.setText(info.title);
		holder.summary.setText(info.summary);

		return convertView;
	}

	class ViewHolder {
		TextView title, summary;
		ImageView iv;
		
	}

}
