package com.cnwir.gongxin.util;

import java.util.List;

import org.cnwir.asyncImg.ImageDownloader;
import org.cnwir.asyncImg.OnImageDownload;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;

public class AddShortCutUtils {

	/**
	 * 功能描述：添加到桌面
	 * 
	 * */

	public static void addShortCut(final QAppInfo info, final Context context) {
		ImageDownloader loader = new ImageDownloader();

		// 快捷图标
		Toast.makeText(context, "添加成功", 0).show();
		loader.imageDownload(info.getImage(), new ImageView(context), Constant.CACHE_IMG, (Activity) context,
				new OnImageDownload() {

					@Override
					public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView) {
						BitmapDrawable bd = new BitmapDrawable(bitmap);
						// 安装的Intent
						Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

						// 快捷名称
						shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, info.getTitle());
						// 快捷图标是允许重复
						shortcut.putExtra("duplicate", false);

						Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
						// shortcutIntent.putExtra(Constant.QAPPINFO, info);
						// 这个地方本来要传实体的，但是一直报一个错，没法解决所以只能用这种麻烦的方法了
						Bundle b = new Bundle();
						b.putBoolean("isFromDesktop", true);
						b.putString("url", info.getUrl());
						b.putString("desc", info.getDesc());
						b.putString("image", info.getImage());
						b.putString("summary", info.getSummary());
						b.putString("title", info.getTitle());
						b.putInt("type", info.getType());
						b.putInt("collect", info.getCollect());
						b.putBoolean("isCollected", info.isCollected());
						b.putInt("id", info.getId());
						b.putInt("share", info.getShare());
						b.putInt("star", info.getStar());

						shortcutIntent.putExtras(b);
						shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
						shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, bd.getBitmap());

						shortcutIntent.setClassName("com.cnwir.gongxin", "com.cnwir.gongxin.ui.DetailActivity");

						// 发送广播
						context.sendBroadcast(shortcut);

					}
				});

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.addToDesktop(info.getId()), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		ClientTask.getInstance(context).addToRequestQueue(request);

	}

	/**
	 * 判断是否存在快捷方式
	 * */
	// public static boolean hasShortcut(Activity activity, String shortcutName)
	// {
	// String url = "";
	// int systemversion = Integer.parseInt(android.os.Build.VERSION.SDK);
	// /* 大于8的时候在com.android.launcher2.settings 里查询（未测试） */
	// if (systemversion < 8) {
	// url = "content://com.android.launcher.settings/favorites?notify=true";
	// } else {
	// url = "content://com.android.launcher2.settings/favorites?notify=true";
	// }
	// ContentResolver resolver = activity.getContentResolver();
	// Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new
	// String[] { shortcutName }, null);
	// if (cursor != null && cursor.moveToFirst()) {
	// cursor.close();
	// return true;
	// }
	// return false;
	// }

	// public static boolean hasShortcut(Activity activity, String shortcutName)
	// {
	// String url = "";
	// url = "content://" + getAuthorityFromPermission(activity,
	// "com.android.launcher.permission.READ_SETTINGS") +
	// "/favorites?notify=true";
	// ContentResolver resolver = activity.getContentResolver();
	// Cursor cursor = resolver.query(Uri.parse(url), new String[] { "title",
	// "iconResource" }, "title=?", new String[] { shortcutName }, null);
	// if (cursor != null && cursor.moveToFirst()) {
	// cursor.close();
	// return true;
	// }
	// return false;
	// }
	// 并不能准确判断 需要通过权限去获取当前手机provider.authority

	private static String getAuthorityFromPermission(Context context, String permission) {
		if (permission == null)
			return null;
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs != null) {
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (permission.equals(provider.readPermission))
							return provider.authority;
						if (permission.equals(provider.writePermission))
							return provider.authority;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 是否已经有快捷方式，某些机型会出现权限问题而报错
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasShortcut(Context context, String shortcutName) {
		boolean hasShortcut = false;
		Uri uri = Uri.parse("content://"
				+ getAuthorityFromPermission1(context, "com.android.launcher.permission.READ_SETTINGS")
				+ "/favorites?notify=true");
		Cursor cursor = context.getContentResolver().query(uri, new String[] { "title", "iconResource" }, "title=?",
				new String[] { shortcutName }, null);
		if (cursor != null && cursor.getCount() > 0) {
			hasShortcut = true;
		}
		return hasShortcut;
	}

	static String getAuthorityFromPermission1(Context context, String permission) {
		if (permission == null)
			return null;
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs != null) {
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (permission.equals(provider.readPermission))
							return provider.authority;
						if (permission.equals(provider.writePermission))
							return provider.authority;
					}
				}
			}
		}
		return null;
	}

}
