package com.cnwir.gongxin.ui.comment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.NormalPostRequest;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.ui.MainActivity;
import com.cnwir.gongxin.util.Constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

/**
 * 
 * 功能描述：评论activity
 * 
 * @author Cheng.F.P
 * */

public class CommentActivity extends Activity implements OnClickListener {

	private RatingBar commentRatingBar;

	private Button finish;

	private ImageView reBack;

	private QAppInfo info;

	private int star;

	private Button commentSubmitBtn;

	private String commentContent;

	private EditText et;

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		initView();
		initData();
	}

	private void initView() {

		reBack = (ImageView) findViewById(R.id.iv_comment_return);
		commentRatingBar = (RatingBar) findViewById(R.id.comment_ratingbar);
		finish = (Button) findViewById(R.id.btn_comment_finish);
		commentSubmitBtn = (Button) findViewById(R.id.comment_submit);
		et = (EditText) findViewById(R.id.comment_content);
		reBack.setOnClickListener(this);
		commentSubmitBtn.setOnClickListener(this);

	}

	private void initData() {
		commentRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						star = (int) rating;

					}
				});
		info = (QAppInfo) getIntent().getSerializableExtra(Constant.QAPPINFO);

		commentSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (info != null) {

					if (checkFormat()) {
						commentRequest(info.getId(), star, commentContent);
					} else {
						Toast.makeText(CommentActivity.this, "请说点什么吧!", 0)
								.show();

					}

				}

			}

		});

	}

	private void commentRequest(int appId, int starNum, String commentText) {

		pd = new ProgressDialog(CommentActivity.this);
		pd.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
		pd.setMessage("正在提交...");
		pd.show();

		Map<String, String> map = new HashMap<String, String>();

		map.put("appId", appId + "");
		map.put("s", starNum + "");
		map.put("c", commentText);
		map.put("token", MyApplication.getInstance().getUserInfo().getToken());

		NormalPostRequest request = new NormalPostRequest(
				RequestURL.commentStar(), new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// 08-13 10:45:02.920: I/System.out(16951):
						// {"data":0,"error":0}

						pd.dismiss();
						try {
							if (response.getInt("error") == 0) {

								Toast.makeText(CommentActivity.this, "提交评论成功",
										0).show();
								CommentActivity.this.finish();
							} else {

								Toast.makeText(CommentActivity.this, "提交评论失败",
										0).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(CommentActivity.this, "提交评论失败", 0)
									.show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						pd.dismiss();
						Toast.makeText(CommentActivity.this, "提交评论失败", 0)
								.show();
					}
				}, map);

		ClientTask.getInstance(CommentActivity.this).addToRequestQueue(request);

	}

	private boolean checkFormat() {
		commentContent = et.getText().toString().trim();

		if (TextUtils.isEmpty(commentContent)) {

			return false;
		}
		return true;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pd.cancel();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_comment_return:
			mfinish();
			break;

		default:
			break;
		}

	}

	public void mfinish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

}
