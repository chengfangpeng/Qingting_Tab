package com.cnwir.gongxin.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.view.sliding.AbSlidingTabView_fix;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.parentclass.ParentFragment;

/**
 * 段子
 * @author wangwm
 * 2015年4月1日 下午2:42:56
 */
public class MainFourFragment extends ParentFragment{

	private AbSlidingTabView_fix mAbSlidingTabView;
	
	private Activity mAcivity;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAcivity = activity;
				
	}

	@Override
	protected int getLayoutId() {
		//
		return R.layout.one;
	}

	@Override
	protected void setupViews(View parentView) {
//		mAbSlidingTabView = (AbSlidingTabView_fix) parentView.findViewById(R.id.mAbSlidingTabView);
//		// 缓存数量
//		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(4);
//		
		Fragment fragment = new CategoryFragment();
//		Bundle bundle = new Bundle();
//		Object[] obj = new Object[] { "jokedz"};
//		bundle.putSerializable(MainActivity.KEY, obj);
//		fragment.setArguments(bundle);
//		
//		mAcivity.getFragmentManager().beginTransaction().replace(R.id.fl, fragment).commit();

	}

	@Override
	protected void initialized() {

//		Fragment fragment = new CategoryFragment();
//		Bundle bundle = new Bundle();
//		Object[] obj = new Object[] { "shangjia"};
//		bundle.putSerializable(MainActivity.KEY, obj);
//		fragment.setArguments(bundle);
//		
//
//
//		// 设置样式
//		mAbSlidingTabView.setTabTextColor(getResources().getColor(R.color.tab_text_selected_color));
//		mAbSlidingTabView.setTabSelectColor(getResources().getColor(R.color.main_color));
//		mAbSlidingTabView.setTabSelectTextSize(35);
//		mAbSlidingTabView.setTabBackgroundResource(R.color.transparent);
//		mAbSlidingTabView.setTabLayoutBackgroundResource(R.color.one_bg_color);
//		// 演示增加一组
//		mAbSlidingTabView.addItemView("全部", fragment);
//
//		mAbSlidingTabView.setTabPadding(10, 0, 10, 0);

	}

	@Override
	protected void threadTask() {

	}
	
}
