package com.cnwir.gongxin.util;

import android.content.Context;
import android.content.Intent;

/**
 * 桌面添加快捷键
 * 
 * @author wangwm 2015年4月2日 下午4:34:53
 */
public class AddShortcartKey {

	/**
	 * 创建桌面快捷方式
	 * @author wangwm
	 * 2015年4月2日 下午5:01:36
	 * @param context
	 * @param act 快捷键启动的程序
	 * @param title  标题
	 * @param imgUrl  图标
	 */
	public static void createShortcut(Context context, Class<?> act, String title,String imgUrl) {
		// 创建一个添加快捷方式的Intent
		Intent addSC = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		//不允许重复创建
		addSC.putExtra("duplicate", false);
		// 快捷键的图标
//		Parcelable icon = Intent.ShortcutIconResource.fromContext(context,
//				R.drawable.ic_launcher);
		
		// 创建单击快捷键启动本程序的Intent
		Intent launcherIntent = new Intent(context, act);
		// 设置快捷键的标题
		addSC.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 设置快捷键的图标
		addSC.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,imgUrl);
		// 设置单击此快捷键启动的程序
		addSC.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
		// 向系统发送添加快捷键的广播
		context.sendBroadcast(addSC);
	}

}
