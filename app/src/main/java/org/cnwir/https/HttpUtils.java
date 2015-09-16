package org.cnwir.https;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtils {

	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			// Toast.makeText(context, R.string.net_error,
			// Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 网络连接判断
	// 判断是否有网络
	// public static boolean isNetworkAvailable(Context context) {
	// return NetWorkHelper.isNetworkAvailable(context);
	// }

	// 判断mobile网络是否可用
	public static boolean isMobileDataEnable(Context context) {
		String TAG = "httpUtils.isMobileDataEnable()";
		try {
			return NetWorkHelper.isMobileDataEnable(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// 判断wifi网络是否可用
	public static boolean isWifiDataEnable(Context context) {
		String TAG = "httpUtils.isWifiDataEnable()";
		try {
			return NetWorkHelper.isWifiDataEnable(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// // 设置Mobile网络开关
	// public static void setMobileDataEnabled(Context context, boolean enabled)
	// {
	// String TAG = "httpUtils.setMobileDataEnabled";
	// try {
	// NetWorkHelper.setMobileDataEnabled(context, enabled);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// Log.e(TAG, e.getMessage());
	// e.printStackTrace();
	// }
	// }

	// 判断是否为漫游
	public static boolean isNetworkRoaming(Context context) {
		return NetWorkHelper.isNetworkRoaming(context);
	}
}
