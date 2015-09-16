//package com.cnwir.gongxin.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.cnwir.asyncImg.ImageDownloader;
//import org.cnwir.asyncImg.OnImageDownload;
//
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.cnwir.gongxin.R;
//import com.cnwir.gongxin.bean.DiscoverInfo;
//import com.cnwir.gongxin.ui.MainTwoFragment;
//import com.cnwir.gongxin.util.Constant;
//
///**
// * 发现
// * @author wangwm
// * 2015年4月2日 下午3:51:14
// */
//public class TwoAdapter extends BaseAdapter{
//
//	private List<DiscoverInfo> datas;
//	private ImageDownloader loader;
//	private MainTwoFragment fragment;
//	
//	public TwoAdapter(MainTwoFragment fragment){
//		this.fragment = fragment;
//		datas = new ArrayList<DiscoverInfo>();
//		loader = new ImageDownloader();
//	}
//	
//	public void updateData(List<DiscoverInfo> data){
//		if(data ==null)
//			return;
//		datas.clear();
//		datas.addAll(data);
//		notifyDataSetChanged();
//	}
//	
//	@Override
//	public int getCount() {
//		return datas.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		if(convertView == null){
//			holder = new ViewHolder();
//			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_item, null);
//			holder.img = (ImageView) convertView.findViewById(R.id.two_img);
//			holder.title = (TextView) convertView.findViewById(R.id.two_title);
//			holder.add = convertView.findViewById(R.id.btn_add);
//			holder.line = convertView.findViewById(R.id.two_line);
//			convertView.setTag(holder);
//		}else{
//			holder = (ViewHolder) convertView.getTag();
//		}
//		
//		if(position%2!=0){
//			holder.line.setVisibility(View.GONE);
//		}else{
//			holder.line.setVisibility(View.VISIBLE);
//		}
//		
//		DiscoverInfo info = datas.get(position);
//		if(loader ==null)
//			loader = new ImageDownloader();
//		 String url = "http://img.sj33.cn/uploads/allimg/201402/7-140223103130591.png";
//		holder.img.setTag(url);
//		holder.img.setImageResource(0);
//		loader.imageDownload(url, holder.img, Constant.CACHE_IMG, fragment.getActivity(), new OnImageDownload() {
//			
//			@Override
//			public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
//				ImageView img = (ImageView) fragment.gv.findViewWithTag(c_url);
//				if(img!=null){
//					img.setImageBitmap(bitmap);
//					img.setTag("");
//				}
//			}
//		});
//		holder.title.setText("测试故事");
//		
//		holder.add.setTag(position);
//		holder.add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				DiscoverInfo info =datas.get( (Integer)v.getTag());
//				//TODO 创建桌面快捷键
////				AddShortcartKey.createShortcut(context, act, title, imgUrl); 
//			}
//		});
//		
//		return convertView;
//	}
//
//	class ViewHolder{
//		ImageView img;
//		TextView title;
//		View add,line;
//	}
//	
//}
