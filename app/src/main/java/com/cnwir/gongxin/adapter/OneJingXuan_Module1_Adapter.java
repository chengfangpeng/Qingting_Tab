package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.asyncImg.ImageDownloader;
import org.cnwir.asyncImg.OnImageDownload;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.Jingxuan_model_item;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.ui.One_JingXuanFragment;
import com.cnwir.gongxin.util.Constant;

/**
 * 精选 -- type 1
 * 
 * @author wangwm 2015年4月14日 下午8:30:29
 */
public class OneJingXuan_Module1_Adapter extends BaseAdapter {

	private List<Jingxuan_model_item> datas;
	private One_JingXuanFragment fragment;
	private ImageDownloader loader;
	private List<Jingxuan_model_item> totall_datas;

	public OneJingXuan_Module1_Adapter(One_JingXuanFragment fragment, List<Jingxuan_model_item> data) {
		totall_datas = new ArrayList<Jingxuan_model_item>();
		this.fragment = fragment;
		this.datas = data;

	}

	@Override
	public int getCount() {
		if (datas.size() >= 5) {

			return 5;
		} else {
			return datas.size();
		}
	}

	public void updateData(List<Jingxuan_model_item> tem_datas, int index) {
		if (tem_datas != null) {
			if (index + 5 < tem_datas.size()) {
				this.datas = tem_datas.subList(index, index + 5);

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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_jingxuan_model_1_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.iv = (ImageView) convertView.findViewById(R.id.img);
			holder.author = (TextView) convertView.findViewById(R.id.resourse);
			holder.share = (TextView) convertView.findViewById(R.id.share_count);
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
				ImageView img = (ImageView) fragment.view.findViewWithTag(c_url);
				if (img != null) {
					img.setImageBitmap(bitmap);
					img.setTag("");
				}
			}
		});

		holder.title.setText(info.title);
		holder.author.setText(info.author);
		holder.share.setText(info.share + "次分享");

		return convertView;
	}

	class ViewHolder {
		TextView title, author, share;
		ImageView iv;
	}

}
