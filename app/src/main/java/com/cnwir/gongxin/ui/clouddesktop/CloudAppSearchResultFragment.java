package com.cnwir.gongxin.ui.clouddesktop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.CloudAppResultListViewAdapter;
import com.cnwir.gongxin.bean.QAppInfo;
import com.google.gson.Gson;

public class CloudAppSearchResultFragment extends Fragment {

	private Activity mActivity;

	private String appJson;

	private CloudAppResultListViewAdapter adapter;

	private ListView mListView;

	private List<QAppInfo> datas;

	private TextView appScanCount;

	private TextView appResultCount;
	
	private TextView appProgressNum;

	private TextView saveMemorizeSize, morePhotos, moreSongs;



	/**
	 * 扫描了app的数量
	 */
	private int scanAppCount;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(
				R.layout.cloud_search_result_fragment_layout, null);
		Bundle bundle = getArguments();
		appJson = bundle.getString("appJson", null);

		scanAppCount = bundle.getInt("scanAppCount", 0);
		initView(contentView);
		initialized();
		return contentView;
	}

	private void initView(View contentView) {
		mListView = (ListView) contentView.findViewById(R.id.app_listView);
		appScanCount = (TextView) contentView.findViewById(R.id.total_scan_tv);
		appResultCount = (TextView) contentView.findViewById(R.id.result_app_count);
		appProgressNum = (TextView) contentView.findViewById(R.id.app_progress_num);
		saveMemorizeSize = (TextView) contentView.findViewById(R.id.save_memorize_size);
		morePhotos = (TextView) contentView.findViewById(R.id.more_photos);
		moreSongs = (TextView) contentView.findViewById(R.id.more_songs);
		try {
			if (!TextUtils.isEmpty(appJson)) {
				Gson gson = new Gson();
				JSONArray array = new JSONArray(appJson.toString());
				datas = new ArrayList<QAppInfo>();
				for (int i = 0; i < array.length(); i++) {
					QAppInfo info = new QAppInfo();
					info = gson.fromJson(array.get(i).toString(),
							QAppInfo.class);

					datas.add(info);

				}
				// 匹配到了多少应用
				appResultCount.setText(datas.size() + "");
				appProgressNum.setText(datas.size() + "款");
				saveMemorizeSize.setText(Math.floor(datas.size() * 32.4) + "M");
				moreSongs.setText(Math.floor(datas.size() * 32.4 / 3) + "首歌曲");
				morePhotos.setText((int)(datas.size() * 32.4) + "张照片");
				adapter = new CloudAppResultListViewAdapter(mActivity, datas);
				mListView.setAdapter(adapter);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void initialized() {
		appScanCount.setText("共扫描了" + scanAppCount + "款应用");
	}

}
