package com.cnwir.gongxin.view.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.util.AbViewUtil;
import com.cnwir.gongxin.R;

public class LoadingFragment extends AbLoadingFragment{
	
	private int mTheme;
	private int mStyle;
	private int mIndeterminateDrawable;
	private int mTextSize = 15;
	private int mTextColor = Color.WHITE;
	private View mContentView;
	private TextView mTextView = null;
	private ImageView mImageView = null;
	private ImageView mImageViewBg = null;
	private int mBackgroundColor = Color.parseColor("#FFFFFF");

	/**
	 * Create a new instance of AbDialogFragment, providing "style" as an
	 * argument.
	 */
	public static LoadingFragment newInstance(int style, int theme) {
		LoadingFragment f = new LoadingFragment();
		// Supply style input as an argument.
		Bundle args = new Bundle();
		args.putInt("style", style);
		args.putInt("theme", theme);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStyle = getArguments().getInt("style");
		mTheme = getArguments().getInt("theme");
		setStyle(mStyle, mTheme);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout parent = new RelativeLayout(this.getActivity());
		parent.setBackgroundColor(mBackgroundColor);
		parent.setGravity(Gravity.CENTER);
		parent.setPadding(20, 20, 20, 20);
//		parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT));
//		parent.setMinimumWidth(AbViewUtil.scaleValue(this.getActivity(), 400));

		mImageView = new ImageView(this.getActivity());
		mImageViewBg = new ImageView(this.getActivity());
		
		mImageView.setImageResource(mIndeterminateDrawable);
//		mImageView.setImageResource(R.drawable.light_app_loading_bg);
		mImageViewBg.setImageResource(R.drawable.light_app_loading_bg);
		mImageView.setScaleType(ScaleType.MATRIX);

		RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout.LayoutParams parambg = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		parambg.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		parent.addView(mImageView, param);
		parent.addView(mImageViewBg, parambg);
//		parent.addView(mImageView, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		parent.addView(mTextView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 执行刷新
				load(v);
			}

		});

		// 执行加载
		load(mImageView);
		mContentView = parent;

		return mContentView;
	}
	
	public View getContentView() {
		return mContentView;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public void setTextSize(int textSize) {
		this.mTextSize = textSize;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
	}

	@Override
	public void setMessage(String message) {
		this.mMessage = message;
		if (mTextView != null) {
			mTextView.setText(mMessage);
		}
	}

	public int getIndeterminateDrawable() {
		return mIndeterminateDrawable;
	}

	public void setIndeterminateDrawable(int indeterminateDrawable) {
		this.mIndeterminateDrawable = indeterminateDrawable;
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
	}


}
