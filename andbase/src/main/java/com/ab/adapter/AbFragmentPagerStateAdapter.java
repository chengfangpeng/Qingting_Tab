package com.ab.adapter;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;


public class AbFragmentPagerStateAdapter extends FragmentStatePagerAdapter{
	
	/** The m fragment list. */
	private ArrayList<Fragment> mFragmentList = null;

	/**
	 * Instantiates a new ab fragment pager adapter.
	 * @param mFragmentManager the m fragment manager
	 * @param fragmentList the fragment list
	 */

	public AbFragmentPagerStateAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
		super(fm);
		mFragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (position < mFragmentList.size()){
			fragment = mFragmentList.get(position);
		}else{
			fragment = mFragmentList.get(0);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

}
