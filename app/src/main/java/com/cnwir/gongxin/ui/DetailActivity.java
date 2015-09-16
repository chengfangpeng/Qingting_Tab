package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.PopListViewAdapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.ui.loading.LoadingActivity;
import com.cnwir.gongxin.ui.login.LoginActivity;
import com.cnwir.gongxin.util.AddShortCutUtils;
import com.cnwir.gongxin.util.CollectUtils;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.DisplayParams;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.gongxin.util.ShareUtils;
import com.cnwir.gongxin.view.dialog.DialogUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 轻应用详情
 * 
 * @author wangwm 2015年4月23日 下午6:36:08
 */
public class DetailActivity extends Activity implements OnClickListener {

	private static final String TAG = "DetailActivity";

	private WebView webview;
	private QAppInfo info;

	private String url;
	private boolean isFromDesktop;
	private ListView popListView;

	private PopupWindow popWindow;
	private ImageView b_more;

	private LinearLayout footer_detail;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		this.webview = ((WebView) findViewById(R.id.webview));

		findViewById(R.id.b_return).setOnClickListener(this);
		findViewById(R.id.b_refresh).setOnClickListener(this);
		findViewById(R.id.b_share).setOnClickListener(this);
		b_more = (ImageView) findViewById(R.id.b_more);
		b_more.setOnClickListener(this);
		footer_detail = (LinearLayout) findViewById(R.id.footer_detail);

		WebSettings settings = this.webview.getSettings();
		// /**设置webview推荐使用的窗口，设置为true*/
		// settings.setUseWideViewPort(true);
		// /**设置webview加载的页面的模式，也设置为true*/
		// settings.setLoadWithOverviewMode(true);

		// 使得其中的图片 适应手机屏幕的宽度
		// settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		DialogUtils.showLoadDialog(this);

		// info = (QAppInfo)
		// getIntent().getSerializableExtra(Constant.QAPPINFO);
		Bundle bundle = getIntent().getExtras();
		isFromDesktop = bundle.getBoolean("isFromDesktop", false);
		if (isFromDesktop) {
			info = new QAppInfo();
			info.setCollect(bundle.getInt("collect", 0));
			info.setCollected(bundle.getBoolean("isCollected", false));
			info.setDesc(bundle.getString("desc"));
			info.setId(bundle.getInt("id", 0));
			info.setImage(bundle.getString("image"));
			info.setShare(bundle.getInt("share"));
			info.setStar(bundle.getInt("star"));
			info.setSummary(bundle.getString("summary"));
			info.setTitle(bundle.getString("title"));
			info.setType(bundle.getInt("type"));
			info.setUrl(bundle.getString("url"));

		} else {

			info = (QAppInfo) bundle.getSerializable(Constant.QAPPINFO);
		}
		if (info != null) {
			url = info.getUrl();
			if (!TextUtils.isEmpty(url)) {
				url = checkUrl(url);
				this.webview.loadUrl(url);
				this.webview.setWebViewClient(new CnWirWebViewClient());

			}

		}

		webview.setWebViewClient(new WebViewClient() {
			
			

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				DialogUtils.removeDialog(DetailActivity.this);
				super.onPageFinished(view, url);
			}

		});
		webview.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				
				if (newProgress >= 30) {
					DialogUtils.removeDialog(DetailActivity.this);
				}
				
				super.onProgressChanged(view, newProgress);
			}
			
			
		});

	}

	private String checkUrl(String url) {
		if (url.length() < 4)
			return "";
		String temp = url.substring(0, 4);
		if (!temp.equals("http")) {
			url = "http://".concat(url);
		}
		return url;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_return:
			if (this.webview.canGoBack()) {
				this.webview.goBack();
			}else{
				if (isFromDesktop) {

					Intent intent = new Intent(DetailActivity.this, LoadingActivity.class);

					startActivity(intent);

					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				}
				finish();
			}
			
			break;
		case R.id.b_refresh:
			this.webview.loadUrl(url);
			break;
		case R.id.b_share:
			ShareUtils.showShare(DetailActivity.this, info.getId(), info.getTitle(), info.getDesc(), info.getUrl(),
					info.getImage());
			break;
		case R.id.b_more:
			if (popWindow == null) {
				LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
				View view = inflater.inflate(R.layout.pop_list, null);
				popListView = (ListView) view.findViewById(R.id.pop_list);
				popListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						switch (position) {
						case 0:

							break;
						case 1:

							break;
						case 2:
							if (isFromDesktop) {

								Intent intent = new Intent(DetailActivity.this, LoadingActivity.class);

								startActivity(intent);

							}
							finish();
							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
							break;

						default:
							break;
						}

					}
				});
				List<String> items = new ArrayList<String>();
				
				items.add("添加");
				items.add("用浏览器打开");
				items.add("退出");
				
				PopListViewAdapter adapter = new PopListViewAdapter(DetailActivity.this, items);
				popListView.setAdapter(adapter);
				popListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						switch (position) {
						case 0:
							if(CollectUtils.CheckIsCollected(info)){
								
								Toast.makeText(DetailActivity.this, "您已添加过该应用", 0).show();
							}else{
								CollectUtils.collect(DetailActivity.this, info);
								Toast.makeText(DetailActivity.this, "成功添加到云桌面", 0).show();
							}
							
							
							
							
							break;
						case 1:
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url = Uri.parse(url);
							intent.setData(content_url);
							startActivity(intent);
							break;
						case 2:
							if (isFromDesktop) {

								Intent intentfinish = new Intent(DetailActivity.this, LoadingActivity.class);

								startActivity(intentfinish);

								overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

							}
							finish();

							break;

						default:
							break;
						}

					}
				});
				popWindow = new PopupWindow(view, DisplayParams.dip2px(getApplicationContext(), 200),
						DisplayParams.dip2px(getApplicationContext(), 142));

			}
			popWindow.setFocusable(true);
			popWindow.setBackgroundDrawable(new BitmapDrawable());
			popWindow.setOutsideTouchable(true);

			int[] location = new int[2];
			footer_detail.getLocationOnScreen(location);

			// popWindow.showAtLocation(footer_detail, Gravity.NO_GRAVITY,
			// footer_detail.getWidth() - popWindow.getWidth(), location[1]
			// - popWindow.getHeight());
			popWindow.showAsDropDown(footer_detail, footer_detail.getWidth() - popWindow.getWidth(),
					0 - footer_detail.getHeight() - popWindow.getHeight());

			break;
		}
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) {
			if (this.webview.canGoBack()){
				
				this.webview.goBack();
			}else{
				if (isFromDesktop) {

					Intent intent = new Intent(DetailActivity.this, LoadingActivity.class);

					startActivity(intent);

					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				}
				finish();
			}
			
		}
		return false;
	}

	private class CnWirWebViewClient extends WebViewClient {
		private CnWirWebViewClient() {
		}

		public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
			paramWebView.loadUrl(paramString);
			return true;
		}
	}

}
