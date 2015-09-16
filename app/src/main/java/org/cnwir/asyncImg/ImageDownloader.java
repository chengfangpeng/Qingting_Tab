package org.cnwir.asyncImg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {

	private static final String TAG = "ImageDownloader";
	private HashMap<String, MyAsyncTask> map = new HashMap<String, MyAsyncTask>();
	private Map<String, SoftReference<Bitmap>> imageCaches = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * 
	 * @param url
	 *            该mImageView对应的url
	 * @param mImageView
	 * @param path
	 *            文件存储路径
	 * @param mActivity
	 * @param download
	 *            OnImageDownload回调接口，在onPostExecute()中被调用
	 */
	public void imageDownload(String url, ImageView mImageView, String path, Activity mActivity,
			OnImageDownload download) {
		SoftReference<Bitmap> currBitmap = imageCaches.get(url);
		Bitmap softRefBitmap = null;
		if (currBitmap != null) {
			softRefBitmap = currBitmap.get();
		}
		String imageName = "";
		if (url != null) {
			imageName = Util.getInstance().getImageName(url);
		}
		Bitmap bitmap = getBitmapFromFile(mActivity, imageName, path);
		// 先从软引用中拿数据
		if (currBitmap != null && mImageView != null && softRefBitmap != null && url.equals(mImageView.getTag())) {
			mImageView.setImageBitmap(softRefBitmap);
		}
		// 软引用中没有，从文件中拿数据
		else if (bitmap != null && mImageView != null && url.equals(mImageView.getTag())) {
			mImageView.setImageBitmap(bitmap);
		}
		// 文件中也没有，此时根据mImageView的tag，即url去判断该url对应的task是否已经在执行，如果在执行，本次操作不创建新的线程，否则创建新的线程。
		else if (url != null && needCreateNewTask(mImageView)) {
			MyAsyncTask task = new MyAsyncTask(url, mImageView, path, mActivity, download);
			if (mImageView != null) {
				Log.i(TAG, "执行MyAsyncTask --> " + Util.flag);
				Util.flag++;
				task.execute();
				// 将对应的url对应的任务存起来
				map.put(url, task);
			}
		}
	}

	/**
	 * 判断是否需要重新创建线程下载图片，如果需要，返回值为true。
	 * 
	 * @param url
	 * @param mImageView
	 * @return
	 */
	private boolean needCreateNewTask(ImageView mImageView) {
		boolean b = true;
		if (mImageView != null) {
			String curr_task_url = (String) mImageView.getTag();
			if (isTasksContains(curr_task_url)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * 检查该url（最终反映的是当前的ImageView的tag，tag会根据position的不同而不同）对应的task是否存在
	 * 
	 * @param url
	 * @return
	 */
	private boolean isTasksContains(String url) {
		boolean b = false;
		if (map != null && map.get(url) != null) {
			b = true;
		}
		return b;
	}

	/**
	 * 删除map中该url的信息，这一步很重要，不然MyAsyncTask的引用会“一直”存在于map中
	 * 
	 * @param url
	 */
	private void removeTaskFormMap(String url) {
		if (url != null && map != null && map.get(url) != null) {
			map.remove(url);
			System.out.println("当前map的大小==" + map.size());
		}
	}

	/**
	 * 从文件中拿图片
	 * 
	 * @param mActivity
	 * @param imageName
	 *            图片名字
	 * @param path
	 *            图片路径
	 * @return
	 */
	private Bitmap getBitmapFromFile(Activity mActivity, String imageName, String path) {
		Bitmap bitmap = null;
		if (imageName != null) {
			File file = null;
			String real_path = "";
			try {
				// if (Util.getInstance().hasSDCard()) {
				// real_path = Util.getInstance().getExtPath()
				// + (path != null && path.startsWith("/") ? path : "/" + path);
				// } else {
				// real_path = Util.getInstance().getPackagePath(mActivity)
				// + (path != null && path.startsWith("/") ? path : "/" + path);
				// }
				File cacheFile = mActivity.getExternalCacheDir();
				String cache = "";
				if (cacheFile != null) {
					cache = cacheFile.getAbsolutePath();
				} else {
					cacheFile = mActivity.getExternalCacheDir();
					cache = cacheFile.getAbsolutePath();
				}
				real_path = cache + (path != null && path.startsWith("/") ? path : "/" + path);
				file = new File(real_path, imageName);
				if (file.exists())
					bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
				bitmap = null;
			}
		}
		return bitmap;
	}

	/**
	 * 将下载好的图片存放到文件中
	 * 
	 * @param path
	 *            图片路径
	 * @param mActivity
	 * @param imageName
	 *            图片名字
	 * @param bitmap
	 *            图片
	 * @return
	 */
	private boolean setBitmapToFile(String path, Activity mActivity, String imageName, Bitmap bitmap) {
		File file = null;
		String real_path = "";
		// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据

		File cacheFile = mActivity.getExternalCacheDir();
		String cache = "";
		if (cacheFile != null) {
			cache = cacheFile.getAbsolutePath();
		} else {
			cacheFile = mActivity.getExternalCacheDir();
			cache = cacheFile.getAbsolutePath();
		}
		// sd卡目录
		// String cache = Util.getInstance().getExtPath();
		try {
			// if (Util.getInstance().hasSDCard()) {
			// real_path = Util.getInstance().getExtPath()
			// + (path != null && path.startsWith("/") ? path : "/" + path);
			// } else {
			// real_path = Util.getInstance().getPackagePath(mActivity)
			// + (path != null && path.startsWith("/") ? path : "/" + path);
			// }
			real_path = cache + (path != null && path.startsWith("/") ? path : "/" + path);

			removeCache(real_path);
			file = new File(real_path, imageName);
			if (!file.exists()) {
				File file2 = new File(real_path + "/");
				file2.mkdirs();
			}
			file.createNewFile();
			FileOutputStream fos = null;
			if (Util.getInstance().hasSDCard()) {
				fos = new FileOutputStream(file);
			} else {
				fos = mActivity.openFileOutput(imageName, Context.MODE_PRIVATE);
			}

			if (imageName != null && (imageName.contains(".png") || imageName.contains(".PNG"))) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			}
			fos.flush();
			if (fos != null) {
				fos.close();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// private static final String CACHDIR = "ImgCach";
	private static final String WHOLESALE_CONV = ".cach";

	private static final int MB = 1024 * 1024;
	private static final int CACHE_SIZE = 10;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 */
	private boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}

		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}

		if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++) {
				if (files[i].getName().contains(WHOLESALE_CONV)) {
					files[i].delete();
				}
			}
		}

		if (freeSpaceOnSd() <= CACHE_SIZE) {
			return false;
		}

		return true;
	}

	/** 计算sdcard上的剩余空间 **/
	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 根据文件的最后修改时间进行排序
	 */
	private class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 辅助方法，一般不调用
	 * 
	 * @param path
	 * @param mActivity
	 * @param imageName
	 */
	private void removeBitmapFromFile(String path, Activity mActivity, String imageName) {
		File file = null;
		String real_path = "";
		try {
			// if (Util.getInstance().hasSDCard()) {
			// real_path = Util.getInstance().getExtPath()
			// + (path != null && path.startsWith("/") ? path : "/" + path);
			// } else {
			// real_path = Util.getInstance().getPackagePath(mActivity)
			// + (path != null && path.startsWith("/") ? path : "/" + path);
			// }
			File cacheFile = mActivity.getExternalCacheDir();
			String cache = "";
			if (cacheFile != null) {
				cache = cacheFile.getAbsolutePath();
			} else {
				cacheFile = mActivity.getExternalCacheDir();
				cache = cacheFile.getAbsolutePath();
			}
			real_path = cache + (path != null && path.startsWith("/") ? path : "/" + path);
			file = new File(real_path, imageName);
			if (file != null)
				file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 异步下载图片的方法
	 * 
	 * @author yanbin
	 * 
	 */
	private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView mImageView;
		private String url;
		private OnImageDownload download;
		private String path;
		private Activity mActivity;

		public MyAsyncTask(String url, ImageView mImageView, String path, Activity mActivity, OnImageDownload download) {
			this.mImageView = mImageView;
			this.url = url;
			this.path = path;
			this.mActivity = mActivity;
			this.download = download;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap data = null;
			if (url != null) {
				try {
					URL c_url = new URL(url);
					InputStream bitmap_data = c_url.openStream();
					data = BitmapFactory.decodeStream(bitmap_data);
					String imageName = Util.getInstance().getImageName(url);
					if (!setBitmapToFile(path, mActivity, imageName, data)) {
						removeBitmapFromFile(path, mActivity, imageName);
					}
					imageCaches.put(url, new SoftReference<Bitmap>(Bitmap.createScaledBitmap(data, 100, 100, true)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// 回调设置图片
			if (download != null) {
				download.onDownloadSucc(result, url, mImageView);
				// 该url对应的task已经下载完成，从map中将其删除
				removeTaskFormMap(url);
			}
			super.onPostExecute(result);
		}

	}
}
