package com.cnwir.gongxin.ui.feedback;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.NormalPostRequest;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.create.CreateUpdateActivity;

public class FeedBackActivity extends Activity implements OnClickListener {

	private EditText feedbackContent;
	private EditText feedbackEmail;
	private Button submit;
	private String content;
	private String email;
	private ImageView reBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	private void initView() {
		feedbackContent = (EditText) findViewById(R.id.feedback_content);
		feedbackEmail = (EditText) findViewById(R.id.feedback_email);
		submit = (Button) findViewById(R.id.btn_feedback_submit);
		reBack = (ImageView) findViewById(R.id.iv_feedback_return);
		submit.setOnClickListener(this);
		reBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_feedback_submit:
			if (checkFormat()) {

				submitFeedback();
			}

			break;
		case R.id.iv_feedback_return:

			finish();
			break;

		default:
			break;
		}

	}
	/**
	 * 功能描述：提交反馈已经
	 * 
	 * */

	private void submitFeedback() {

		Map<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getInstance().getUserInfo().getToken());
		map.put("email", email);
		map.put("content", content);

		NormalPostRequest request = new NormalPostRequest(RequestURL.submitFeedback(), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response != null) {

						int code = response.getInt("error");
						if (code == 0) {

							Toast.makeText(FeedBackActivity.this, "意见已经提交到服务器", 0).show();
							finish();
						}

					} else {
						Toast.makeText(FeedBackActivity.this, "提交意见失败", 0).show();

					}
				} catch (JSONException e) {
					Toast.makeText(FeedBackActivity.this, "提交意见失败", 0).show();
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(FeedBackActivity.this, "提交意见失败", 0).show();

			}
		}, map);
		ClientTask.getInstance(FeedBackActivity.this).addToRequestQueue(request);

	}

	private boolean checkFormat() {
		content = feedbackContent.getText().toString();
		email = feedbackEmail.getText().toString();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(FeedBackActivity.this, R.string.empty_error, 0).show();
			return false;
		}
		if (TextUtils.isEmpty(email)) {

			Toast.makeText(FeedBackActivity.this, R.string.empty_error, 0).show();
			return false;

		}
		if (!email.contains("@")) {

			Toast.makeText(FeedBackActivity.this, R.string.format_error, 0).show();
			return false;

		}
		return true;

	}

}
