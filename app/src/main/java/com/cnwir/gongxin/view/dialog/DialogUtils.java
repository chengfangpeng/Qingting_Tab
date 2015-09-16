package com.cnwir.gongxin.view.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.DatabaseUtils;
import android.support.v4.app.FragmentActivity;

import com.ab.fragment.AbProgressDialogFragment;
import com.cnwir.gongxin.R;

public class DialogUtils {
	
	/** dialog tag*/
	private static String mDialogTag = "dialog";
	
	public static LoadingFragment showLoadDialog(Context context) {
		Activity activity = (Activity)context; 
		LoadingFragment newFragment = LoadingFragment.newInstance(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		newFragment.setIndeterminateDrawable(R.drawable.light_app_loading);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
	    newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：移除Fragment.
	 * @param context the context
	 */
	public static void removeDialog(Context context){
		try {
			Activity activity = (Activity)context; 
			FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
	        // 指定一个系统转场动画   
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);  
	        
			Fragment prev = activity.getFragmentManager().findFragmentByTag(mDialogTag);
			if (prev != null) {
			    ft.remove(prev);
			}
			ft.addToBackStack(null);
		    ft.commit();
		} catch (Exception e) {
			//可能有Activity已经被销毁的异常
			e.printStackTrace();
		}
	}
	
	/**
	 * 描述：显示进度框.
	 * @param context the context
	 * @param indeterminateDrawable 用默认请写0
	 * @param message the message
	 */
	public static ProgressDialogFragment showProgressDialog(Context context,int indeterminateDrawable,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		ProgressDialogFragment newFragment = ProgressDialogFragment.newInstance(indeterminateDrawable,message);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	
	

}
