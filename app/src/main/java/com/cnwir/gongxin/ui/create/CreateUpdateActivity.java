package com.cnwir.gongxin.ui.create;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CreateApp;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.NormalPostRequest;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 功能描述：创建activity
 * 
 * @author Cheng.F.P
 * */

public class CreateUpdateActivity extends Activity implements OnClickListener {

	private Button finish;

	private ImageView reBack;

	private EditText createEditUrl;

	private EditText createEditTitle;

	private String createUrl;

	private String createTitle;
	private QAppInfo info;
	private TextView headTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_app);
		info = (QAppInfo) getIntent().getSerializableExtra(Constant.QAPPINFO);

		initView();
		initData();
	}

	private void initView() {

		reBack = (ImageView) findViewById(R.id.iv_create_return);
		finish = (Button) findViewById(R.id.btn_create_finish);
		headTitle = (TextView) findViewById(R.id.create_head_text);
		if (info != null) {

			headTitle.setText("修改应用");
		}
		createEditUrl = (EditText) findViewById(R.id.create_edit_url);
		createEditTitle = (EditText) findViewById(R.id.create_edit_title);
		reBack.setOnClickListener(this);

	}

	private void initData() {

		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("finish");

				if (checkFormat()) {
					System.out.println("add");
					if (info == null) {

						addApp(createUrl, createTitle);
					} else {

						updateApp(createUrl, createTitle, info.getId());
					}

				}

			}
		});

	}

	/**
	 * 功能描述：修改app
	 * 
	 * 
	 * */

	protected void updateApp(String createUrl, String createTitle, int id) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getInstance().getUserInfo().getToken());
		map.put("title", createTitle);
		map.put("url", createUrl);
		map.put("id", id + "");

		NormalPostRequest request = new NormalPostRequest(RequestURL.updateApp(), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				if (response != null) {

					try {
						JSONObject object = new JSONObject(response.toString());

						int data = object.optInt("error");
						if(data == 0){
							Toast.makeText(CreateUpdateActivity.this, "修改成功", 0).show();
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(CreateUpdateActivity.this, R.string.update_error, 0).show();

				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

				Toast.makeText(CreateUpdateActivity.this, R.string.create_error, 0).show();

			}
		}, map);
		ClientTask.getInstance(CreateUpdateActivity.this).addToRequestQueue(request);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_create_return:
			mfinish();
			break;

		default:
			break;
		}

	}

	private boolean checkFormat() {
		createUrl = createEditUrl.getText().toString();
		createTitle = createEditTitle.getText().toString();
		if (TextUtils.isEmpty(createUrl)) {
			Toast.makeText(CreateUpdateActivity.this, R.string.empty_error, 0).show();
			return false;
		}
		if (TextUtils.isEmpty(createTitle)) {

			Toast.makeText(CreateUpdateActivity.this, R.string.empty_error, 0).show();
			return false;

		}
		if (!createUrl.startsWith("http://")) {

			Toast.makeText(CreateUpdateActivity.this, R.string.format_error, 0).show();
			return false;

		}
		return true;

	}

	/**
	 * 功能描述：创建自定义应用
	 * 
	 * @param url
	 *            应用地址
	 * @param title
	 *            标题
	 * */
	private void addApp(String url, String title) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getInstance().getUserInfo().getToken());
		map.put("title", createTitle);
		map.put("url", createUrl);

		NormalPostRequest request = new NormalPostRequest(RequestURL.addApp(), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				if (response != null) {

					try {
						JSONObject object = new JSONObject(response.toString());

						int data = object.optInt("error");
						if(data == 0){
							Toast.makeText(CreateUpdateActivity.this, "创建成功", 0).show();
							Map<Integer, CreateApp> createApps = MyApplication.getInstance().getCreateAppList();
							CreateApp createApp = new CreateApp();
							createApp.setId(new Random().nextInt());
							createApp.setTitle(createTitle);
							createApp.setUrl(createUrl);
							createApps.put(createApp.getId(), createApp);
							MyApplication.getInstance().setCreateAppList(createApps);
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(CreateUpdateActivity.this, R.string.create_error, 0).show();

				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

				Toast.makeText(CreateUpdateActivity.this, R.string.create_error, 0).show();

			}
		}, map);
		ClientTask.getInstance(CreateUpdateActivity.this).addToRequestQueue(request);
	}

	public void mfinish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

}
