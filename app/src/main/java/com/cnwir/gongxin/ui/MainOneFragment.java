package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import com.ab.view.sliding.AbSlidingTabView;
import com.ab.view.sliding.AbSlidingTabView_fix;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.parentclass.ParentFragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 推荐
 * 
 * @author wangwm 2015年4月1日 下午2:42:56
 */
public class MainOneFragment extends ParentFragment {

	private AbSlidingTabView_fix mAbSlidingTabView;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected int getLayoutId() {
		//
		return R.layout.one;
	}

	@Override
	protected void setupViews(View parentView) {
		mAbSlidingTabView = (AbSlidingTabView_fix) parentView.findViewById(R.id.mAbSlidingTabView);
		// 缓存数量
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(3);
		
	}

	@Override
	protected void initialized() {

		Fragment fragment1 = new One_MingZhanFragment_1();
		Fragment fragment2 = new One_JingXuanFragment();
		Fragment fragment3 = new One_RenQiFragment();
		Fragment fragment4 = new CategoryFragment();
		Bundle bundle = new Bundle();
		Object[] obj = new Object[] { "tools" };
		bundle.putSerializable(MainActivity.KEY, obj);
		fragment4.setArguments(bundle);

		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(fragment1);
		mFragments.add(fragment2);
		mFragments.add(fragment3);
		mFragments.add(fragment4);

		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add(getResources().getString(R.string.one_1));
		tabTexts.add(getResources().getString(R.string.one_2));
		tabTexts.add(getResources().getString(R.string.one_3));
		tabTexts.add(getResources().getString(R.string.one_4));

		// 设置样式
		mAbSlidingTabView.setTabTextColor(getResources().getColor(R.color.white_color));
		mAbSlidingTabView.setTabSelectColor(getResources().getColor(R.color.white_color));
		mAbSlidingTabView.setTabSelectTextSize(35);
		mAbSlidingTabView.setTabBackgroundResource(R.color.transparent);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.color.main_color);
		mAbSlidingTabView.setTabBackgroundResourceSelect(R.drawable.tab_select_bg);
		// 演示增加一组
		mAbSlidingTabView.addItemViews(tabTexts, mFragments);

		mAbSlidingTabView.setTabPadding(10, 0, 10, 20);

	}

	@Override
	protected void threadTask() {

	}

}
