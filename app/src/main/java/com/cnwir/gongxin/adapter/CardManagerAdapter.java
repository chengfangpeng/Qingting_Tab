package com.cnwir.gongxin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.CardInfo;
import com.cnwir.gongxin.bean.CardSortInfo;
import com.cnwir.gongxin.db.CardInfoDao;
import com.lidroid.xutils.BitmapUtils;

/**
 * 卡片管理
 * 
 * @author wangwm 2015年4月15日
 */
public class CardManagerAdapter extends ArrayAdapter<CardSortInfo> {
	private List<CardSortInfo> datas;
	private Context context;

	private List<CardInfo> cardList;

	private CardInfoDao dao;
	private BitmapUtils bitmapUtils;

	public CardManagerAdapter(Context context, List<CardSortInfo> datas) {
		super(context, 0, datas);
		this.datas = new ArrayList<CardSortInfo>();
		this.context = context;
		if (datas == null)
			return;
		this.datas = datas;
		dao = new CardInfoDao(context);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.defualt_app_icon);
		bitmapUtils.configDefaultLoadingImage(R.drawable.defualt_app_icon);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CardSortInfo getItem(int position) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_managerr_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.btn = (RadioButton) convertView.findViewById(R.id.rb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CardSortInfo info = datas.get(position);

		bitmapUtils.display(holder.img, info.getIcon());
		holder.title.setText(info.getTitle());
		holder.desc.setText(info.getSummary());
		holder.btn.setTag(position);
		holder.btn.setChecked(!info.isShow());

		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = (Integer) v.getTag();
				CardSortInfo info = datas.get(index);
				RadioButton rb = (RadioButton) v;
				rb.setChecked(!info.isShow());
				 info.setShow(((RadioButton) v).isChecked());
				 
				// dao.updateCardInfo(info);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView title, desc;
		RadioButton btn;
	}

}
