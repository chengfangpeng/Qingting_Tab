package com.cnwir.gongxin.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyViewPagerAdapter extends PagerAdapter {

	public List<View> views;
	int mCount;

	public MyViewPagerAdapter(List<View> views) {
		this.views = views;
		mCount = views.size();
		if (views.size() == 0)
			return;
	}

	@Override
	public void destroyItem(View collection, int position, Object arg2) {
		if (views.size() == 0)
			return;
		if (position >= views.size()) {
			int newPosition = position % views.size();
			position = newPosition;
		}
		if (position < 0) {
			position = -position;
		}
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		if (mCount == 0)
			return 0;
		if(mCount ==1)
			return 1;
//		return mCount + 1;// 此处+1才能向右连续滚动
//		return Integer.MAX_VALUE;
		return mCount;
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		if (position >= views.size() && views.size() != 0) {
			int newPosition = position % views.size();
			position = newPosition;
//			mCount++;
		}
		if (position < 0) {
			position = -position;
//			mCount--;
		}
		try {
			((ViewPager) collection).addView(views.get(position), 0);
		} catch (Exception e) {
		}
		if (views.size() == 0)
			return null;
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
