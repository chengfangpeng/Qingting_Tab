package com.cnwir.gongxin.ui;

import java.lang.reflect.Field;

import org.cnwir.https.HttpUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.bean.RequestVo;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.util.ThreadPoolManager;
import com.cnwir.gongxin.view.dialog.CustomProgressDialog;

/**
 * Activity公共类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

	private static final String TAG = "BaseActivity";
	protected static final String k = "1";

	public static int width, height;

	protected ProgressDialog progressDialog;
	protected AlertDialog mAlertDialog;

	private ThreadPoolManager threadPoolManager;

	protected String DRIVEID;
	public int USERID;
	protected String USERNAME,PASSWORD,NICKNAME;
	protected boolean isRemeber;

	private CustomProgressDialog loading;




	public BaseActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	/******************************** ��Activity LifeCycle For Debug�� *******************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		width = display.getWidth();
		height = display.getHeight();
		if ("".equals(width)) {
			LogUtil.d(TAG, "手机屏幕width--height--" + width + "--" + height);
			width = displayMetrics.widthPixels;
		}
		if ("".equals(height)) {
			LogUtil.d(TAG, "手机屏幕width--height--" + width + "--" + height);
			height = displayMetrics.heightPixels;
		}
		LogUtil.d(TAG, "手机屏幕width--height--" + width + "--" + height);
		LogUtil.d(TAG, "getDisplayMetrics()手机屏幕width--height--" + displayMetrics.widthPixels + "--"
				+ displayMetrics.heightPixels);
		DRIVEID = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

		LogUtil.v("DRIVEID", "设备id:" + DRIVEID);
		loadViewLayout();
		findViewById();
		processLogic();
	}



	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject, boolean paramBoolean);
	}

	/**
	 * 从服务器上获取数据，并回调处�?
	 * 
	 * @param reqVo
	 * @param callBack
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack) {

	}

	protected abstract void loadViewLayout();

	/**
	 * 向后台请求数�?
	 */
	protected abstract void processLogic();

	/**
	 * 
	 */
	protected abstract void findViewById();

//	public XListView mXListView;

	@Override
	protected void onStart() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStart() invoked!!");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onRestart() invoked!!");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onResume() invoked!!");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onPause() invoked!!");
		super.onPause();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStop() invoked!!");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onDestroy() invoked!!");
		super.onDestroy();

		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}

	}

	public void recommandToYourFriend(String url, String shareTitle) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);

		Intent itn = Intent.createChooser(intent, "分享");
		startActivity(itn);
	}

	/******************************** ��Activity LifeCycle For Debug�� *******************************************/

	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}

	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过反射来设置对话框是否要关闭，在表单校验时很管用， 因为在用户填写出错时点确定时默认Dialog会消失， �?��达不到校验的效果
	 * 而mShowing字段就是用来控制是否要消失的，�?它在Dialog中是私有变量�?�?��只有通过反射去解决此问题
	 * 
	 * @param pDialog
	 * @param pIsClose
	 */
	public void setAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field field = pDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(pDialog, pIsClose);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(TitleID).setMessage(Message).show();
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pOkClickListener, null, null);
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			DialogInterface.OnClickListener pOkClickListener, DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage, String pPositiveButtonLabel,
			String pNegativeButtonLabel, DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener, DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

//	/**
//	 * 显示提示�?
//	 */
//	protected void showProgressDialog() {
//		if ((!isFinishing()) && (this.progressDialog == null)) {
//			this.progressDialog = new ProgressDialog(this);
//		}
//		this.progressDialog.setTitle(getString(R.string.loadTitle));
//		this.progressDialog.setMessage(getString(R.string.LoadContent));
//		this.progressDialog.show();
//	}

//	/**
//	 * 关闭提示�?
//	 */
//	protected void closeProgressDialog() {
//		if (this.progressDialog != null)
//			this.progressDialog.dismiss();
//	}

	protected ProgressDialog showProgressDialog(int pTitelResID, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = getResources().getString(pTitelResID);
		return showProgressDialog(title, pMessage, pCancelClickListener);
	}

	protected ProgressDialog showProgressDialog(String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(this, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
	}

	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void handleOutmemoryError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "内存空间不足", Toast.LENGTH_SHORT).show();
				// finish();
			}
		});
	}

	private int network_err_count = 0;

	protected void handleNetworkError() {
		network_err_count++;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (network_err_count < 3) {
					Toast.makeText(BaseActivity.this, getString(R.string.network_no_good), Toast.LENGTH_SHORT).show();
				} else if (network_err_count < 5) {
					Toast.makeText(BaseActivity.this, getString(R.string.network_b), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(BaseActivity.this, getString(R.string.network_bad), Toast.LENGTH_SHORT).show();
				}
				// finish();
			}
		});
	}

	protected void handleMalformError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, getString(R.string.data_format_error), Toast.LENGTH_SHORT).show();
				// finish();
			}
		});
	}

	protected void handleFatalError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, getString(R.string.accident_exit), Toast.LENGTH_SHORT).show();
				mfinish();
			}
		});
	}

	public void mfinish() {
		
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	public void defaultFinish() {
		super.finish();
	}

	/**
	 * 弹出loading
	 */
	public void startProgressDialog() {
		if (loading == null) {
			loading = CustomProgressDialog.createDialog(this);
			// loading.setMessage("正在加载...");
		}

		loading.show();
	}

	/**
	 * 隐藏loading
	 */
	public void stopProgressDialog() {
		if (loading != null) {
			loading.dismiss();
			loading = null;
		}
	}

}
