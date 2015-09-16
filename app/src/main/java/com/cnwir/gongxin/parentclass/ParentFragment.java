package com.cnwir.gongxin.parentclass;



import com.cnwir.gongxin.ui.MainActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Fragment父类
 * 
 */
public abstract class ParentFragment extends Fragment{

	protected static String SaveFragmentKey = "ParentFragment",
			SaveDataKey = "SaveDataKey";
	protected static LayoutInflater inflater;
	protected Object[] params, saveData;
	protected ImageView loading_refresh_ImageView;
	private static FragmentManager manager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		manager = getActivity().getFragmentManager();
		if (savedInstanceState != null) {
			saveData = (Object[]) savedInstanceState
					.getSerializable(SaveDataKey);
		}
		Bundle bundle = getArguments();
		if (bundle != null) {
			params = (Object[]) bundle
					.getSerializable(MainActivity.KEY);
			if (params == null) {
				Log.e(this.getClass().getSimpleName(),
						"传参Bundle对象包含的Object数组为空");
			}
		} else {
			Log.e(this.getClass().getSimpleName(), "传参Bundle对象为空");
		}
	
	}
	
	public View parentView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ParentFragment.inflater = inflater;
		int layoutId = getLayoutId();
		if (layoutId == 0) {
		} else {
			parentView = inflater.inflate(layoutId, container, false);
		}
		setupViews(parentView);
		initialized();
		return parentView;
	}


	protected void onSaveInstanceState(Bundle outState,
			ParentFragment fragment, Object... saveData) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SaveDataKey, saveData);
		getActivity().getFragmentManager().putFragment(outState,
				SaveFragmentKey, fragment);
	};
	
	
	
	
	/**
	 * Fragment 没有添加到栈不能返还
	 * 
	 * @param replaceLayout
	 * @param fragment
	 * @param fragmentName
	 * @param params
	 */
	protected void switchFragmentNoBackStack(int replaceLayout, ParentFragment fragment,
			String fragmentName, Object... params) {
		if (fragment != null) {
			if (params != null) {
				Bundle args = new Bundle();
				args.putSerializable(MainActivity.KEY, params);
				fragment.setArguments(args);
			} else {
				Log.i(this.getClass().getSimpleName(), "目标ParentFragment未传递参数");
			}
			manager.beginTransaction().replace(replaceLayout, fragment)
					.commit();
		} else {
			Log.e(this.getClass().getSimpleName(), "目标ParentFragment为空");
		}
	}
	
	/**
	 * Fragment 添加到栈可以返回
	 * 
	 * @param replaceLayout
	 * @param fragment
	 * @param fragmentName
	 * @param params
	 */
	protected void switchFragment(int replaceLayout, ParentFragment fragment,
			String fragmentName, Object... params) {
		if (fragment != null) {
			if (params != null) {
				Bundle args = new Bundle();
				args.putSerializable(MainActivity.KEY, params);
				fragment.setArguments(args);
			} else {
				Log.i(this.getClass().getSimpleName(), "目标ParentFragment未传递参数");
			}
			manager.beginTransaction().replace(replaceLayout, fragment).addToBackStack(fragmentName).commit();
					
			Log.e(this.getClass().getSimpleName(), "目标ParentFragment为空");
		}
	}


	/**
	 * 布局文件ID
	 * 
	 * @return
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化组件
	 */
	protected abstract void setupViews(View parentView);

	/**
	 * 初始化数据
	 */
	protected abstract void initialized();

	/**
	 *异步任务
	 * 
	 */
	protected abstract void threadTask();

}
