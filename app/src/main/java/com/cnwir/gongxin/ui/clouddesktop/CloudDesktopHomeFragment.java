package com.cnwir.gongxin.ui.clouddesktop;

import java.io.File;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.AppsItemInfo;
import com.cnwir.gongxin.util.Util;
import com.cnwir.gongxin.view.ScaleProgress;
import com.tencent.weibo.sdk.android.component.ConversationActivity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 
 * */

public class CloudDesktopHomeFragment extends Fragment {

	private Activity mActivity;

	private TextView bv_transfer;

	private ScaleProgress mScaleProgress;

	private int progress;

	private TextView memorizeSpaceTv;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mScaleProgress.setProgress(msg.what);


			if (progress >= (int) (((float) Util.getRomUsedSize())
					/ (float) Util.getRomTotalSize() * 100)) {
				return;
			}

			mHandler.sendEmptyMessageDelayed(++progress, 100);

			super.handleMessage(msg);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View contentView = inflater.inflate(
				R.layout.cloud_desktop_home_fragment, null);
		initView(contentView);
		initialized();
		return contentView;
	}

	private void initialized() {
		mScaleProgress.setMax(100);
		mScaleProgress.setTotalMemory(Util.getRomTotalSizeStr(mActivity));
		mScaleProgress.setUsedMemory(Util.getUsedSizeStr(mActivity));
		memorizeSpaceTv.setText(Util.getUsedSizeStr(mActivity) + "/" + Util.getRomTotalSizeStr(mActivity));
		mHandler.sendEmptyMessageDelayed(0, 100);

	}

	private void initView(View contentView) {

		mScaleProgress = (ScaleProgress) contentView
				.findViewById(R.id.mScaleProgress);

		bv_transfer = (TextView) contentView
				.findViewById(R.id.cloud_desktop_transfer_btn);
		bv_transfer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				replaceFragment(R.id.content_frame,
						new SearchCloudDataBaseFragment());

			}
		});
		memorizeSpaceTv = (TextView) contentView.findViewById(R.id.memorize_space);


	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;

	}

	protected void replaceFragment(int viewId, Fragment fragment) {
		FragmentManager fragmentManager = ((FragmentActivity) mActivity)
				.getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(viewId, fragment).commit();
	}

}
