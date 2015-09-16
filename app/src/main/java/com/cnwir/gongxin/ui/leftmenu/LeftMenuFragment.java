package com.cnwir.gongxin.ui.leftmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cnwir.asyncImg.ImageDownloader;
import org.cnwir.asyncImg.OnImageDownload;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.bean.UserInfo;
import com.cnwir.gongxin.parentclass.ParentFragment;
import com.cnwir.gongxin.ui.DetailActivity;
import com.cnwir.gongxin.ui.collect.CollectActivity;
import com.cnwir.gongxin.ui.create.CreateUpdateActivity;
import com.cnwir.gongxin.ui.create.CreateDetailActivity;
import com.cnwir.gongxin.ui.login.LoginActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.TextToBitmapUtils;
import com.cnwir.gongxin.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

public class LeftMenuFragment extends ParentFragment {

	private Callbacks callbacks = defaultCallbacks;
	private UpdateUserInfoReceiver receiver;
	private CircleImageView menu_header;
	private TextView menu_name;
	private TextView menu_desc;
	private Button login;
	private ImageView iv_img_create;
	private LinearLayout createLayout;

	private List<CollectApp> appCollectList;

	private BitmapUtils bitmapUtils;

	private TextView createCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container, savedInstanceState);

		receiver = new UpdateUserInfoReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACITON_UPDATE_USER_INFO);
		getActivity().registerReceiver(receiver, filter);
		bitmapUtils = new BitmapUtils(getActivity());
		return contextView;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUserInfo();
	}

	private OnClickListener leftmenu_click_Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			callbacks.onLeftMenuClick(v);
			switch (v.getId()) {
//			case R.id.iv_img_collect1:
//				isclickCollect(collect_img_1, appCollectList, 1);
//
//				break;
//			case R.id.iv_img_collect2:
//
//				isclickCollect(collect_img_2, appCollectList, 2);
//
//				break;
//			case R.id.iv_img_collect3:
//
//				isclickCollect(collect_img_3, appCollectList, 3);
//
//				break;
//			case R.id.ll_collect:
//				intentToDetailCollect();
//				break;
			case R.id.ll_create:
				intentToDetailCreate();
				break;
			case R.id.iv_img_create:
				if (MyApplication.getInstance().isLogined()) {

					Intent intent = new Intent(getActivity(), CreateUpdateActivity.class);
					startActivity(intent);
					(getActivity()).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				} else {

					getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
					getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);

				}

				break;

			default:
				break;
			}

		}

	};

	private void intentToDetailCreate() {

		if (MyApplication.getInstance().isLogined()) {
			Intent intent = new Intent(getActivity(), CreateDetailActivity.class);

			getActivity().startActivity(intent);

			(getActivity()).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

		} else {
			getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
			return;
		}

	}

	/**
	 * 描述：跳转到收藏页面
	 * 
	 * */

	public void intentToDetailCollect() {

		if (MyApplication.getInstance().isLogined()) {

			Intent intent = new Intent(getActivity(), CollectActivity.class);

			getActivity().startActivity(intent);

			(getActivity()).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

		} else {
			getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
			return;
		}
	}

	/**
	 * 
	 * 描述：判断收藏按钮可以点吗，在可点的情况下进入详情页面
	 * 
	 * @param iv
	 *            对应的收藏
	 * @param infos
	 *            收藏列表
	 * @param 第几个
	 * 
	 * */

	public void isclickCollect(ImageView iv, List<CollectApp> infos, int index) {
		if (MyApplication.getInstance().isLogined()) {
			if (infos == null) {

				return;
			} else if (index > infos.size()) {
				return;

			} else {
				Intent intent = new Intent(getActivity(), DetailActivity.class);

				intent.putExtra(Constant.QAPPINFO, infos.get(index - 1));

				getActivity().startActivity(intent);

				(getActivity()).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

			}

		} else {

			return;

		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		callbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callbacks = defaultCallbacks;
	}

	private static Callbacks defaultCallbacks = new Callbacks() {

		@Override
		public void onLeftMenuClick(View v) {

		}

	};

	public interface Callbacks {

		public void onLeftMenuClick(View v);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.menu;
	}

	@Override
	protected void setupViews(View parentView) {
		menu_header = (CircleImageView) parentView.findViewById(R.id.iv_menu_header);
		menu_name = (TextView) parentView.findViewById(R.id.tv_menu_user_name);
		menu_desc = (TextView) parentView.findViewById(R.id.tv_menu_user_desc);
		createCount = (TextView) parentView.findViewById(R.id.tv_my_create_count);

		iv_img_create = (ImageView) parentView.findViewById(R.id.iv_img_create);

		createLayout = (LinearLayout) parentView.findViewById(R.id.menu_create_app_layout);

		iv_img_create.setOnClickListener(leftmenu_click_Listener);

		parentView.findViewById(R.id.ll_create).setOnClickListener(leftmenu_click_Listener);
		parentView.findViewById(R.id.ll_feedback).setOnClickListener(leftmenu_click_Listener);
		parentView.findViewById(R.id.ll_setting).setOnClickListener(leftmenu_click_Listener);
		parentView.findViewById(R.id.about_us).setOnClickListener(leftmenu_click_Listener);
		login = (Button) parentView.findViewById(R.id.login);
		login.setOnClickListener(leftmenu_click_Listener);

	}

	protected void updateUserInfo() {

		// 更新用户信息

		if (MyApplication.getInstance().isLogined()) {
			UserInfo userInfo = MyApplication.getInstance().getUserInfo();
			login.setText(R.string.exit_login);
			createCount.setText(MyApplication.getInstance().getCreateAppList().size() + "");
			if (!TextUtils.isEmpty(userInfo.getNickName())) {
				menu_name.setText(userInfo.getNickName());
			}
			if (!TextUtils.isEmpty(userInfo.getHeadImgUrl())) {

				ImageDownloader imageDownloader = new ImageDownloader();
				imageDownloader.imageDownload(userInfo.getHeadImgUrl(), menu_header, Constant.CACHE_IMG, getActivity(),
						new OnImageDownload() {

							@Override
							public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
								imageView.setImageBitmap(bitmap);
							}
						});

			}

			Map<Integer, CreateApp> createApps = MyApplication.getInstance().getCreateAppList();
			if (createApps != null) {
				iv_img_create.setVisibility(View.GONE);
				int size = 0;
				if (createApps.size() > 2) {

					size = 2;
				} else {

					size = createApps.size();
				}
				List<CreateApp> apps = new ArrayList<CreateApp>();

				for (Integer key : createApps.keySet()) {
					apps.add(createApps.get(key));
				}
				createLayout.removeAllViews();
				for (int i = 0; i < size; i++) {
					ImageView iv = new ImageView(getActivity());
					iv.setImageBitmap(TextToBitmapUtils.textToBitmap(apps.get(i).getTitle()));
					iv.setTag(apps.get(i));
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							CreateApp app = (CreateApp) v.getTag();
							toAppDetailInfo(app);

						}
					});
					
					createLayout.addView(iv, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT));

				}

			}

		} else {
			login.setText(R.string.login);
			menu_name.setText("");
			menu_header.setImageResource(R.drawable.menu_icon);
			createCount.setText("");
			createLayout.removeAllViews();
			iv_img_create.setVisibility(View.VISIBLE);
			iv_img_create.setImageResource(R.drawable.add_icon);

		}

	}

	protected void toAppDetailInfo(QAppInfo info) {

		Intent intent = new Intent(getActivity(), DetailActivity.class);

		intent.putExtra(Constant.QAPPINFO, info);

		getActivity().startActivity(intent);

		(getActivity()).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

	}

	@Override
	protected void threadTask() {

	}

	class UpdateUserInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.ACITON_UPDATE_USER_INFO.equals(action)) {// 更新用户信息

				updateUserInfo();
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (receiver != null)
			getActivity().unregisterReceiver(receiver);
	}

	@Override
	protected void initialized() {

	}

}
