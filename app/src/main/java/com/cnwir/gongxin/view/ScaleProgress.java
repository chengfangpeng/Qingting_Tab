package com.cnwir.gongxin.view;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.cnwir.gongxin.util.DisplayUtil;

/**
 * Created by heaven on 2015/7/15.
 */
public class ScaleProgress extends View {

	private static final double PI = Math.PI;

	private int defalult_scale_color = Color.argb(125, 255, 255, 255);

	private int default_background_color = Color.argb(40, 255, 255, 255);

	private int finish_scale_color = Color.argb(255, 255, 255, 255);

	private String percent_text;

	private float percent = 0.5f;

	private Paint paint;

	private Paint textPaint;

	private static final String TIP = "你的手机可以重返二十岁";

	private int max;

	private int progress;

	/**
	 * 表盘的半径
	 */
	private float radius;

	private Context context;

	private String totalMemory;

	private String usedMomory;

	public ScaleProgress(Context context) {
		super(context);
		this.context = context;
	}

	public ScaleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ScaleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	/**
	 * 初始化画笔
	 */
	private void init() {

		textPaint = new TextPaint();
		textPaint.setAntiAlias(true);

	}

	/*
	 * 设置步数
	 */

	public void setProgress(int progress) {

		this.progress = progress;

		invalidate();

	}

	/*
	 * 这是进度最大值
	 */
	public void setMax(int max) {

		this.max = max;
	}

	public void setTotalMemory(String totalMemory) {

		this.totalMemory = totalMemory;
	}

	public void setUsedMemory(String usedMemory) {

		this.usedMomory = usedMemory;
	}

	protected void onDraw(Canvas canvas) {

		paint = new Paint();
		paint.setColor(default_background_color);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f,
				paint);

		canvas.save();

		paint.setColor(finish_scale_color);
		paint.setStyle(Paint.Style.STROKE);// 设置为空心

		paint.setStrokeWidth(12);
		float angle = (float) (PI / 2);
		float outX, outY, inX, inY;
		radius = getWidth() / 2f;

		float outRadius = radius - 30;
		float inRadius = radius - 60;
		if (max != 0) {

			percent = (float) progress / (float) max;
			percent_text = progress + "%";
		} else {

			percent = 0l;
		}

		for (int i = 0; i < 48; i++) {

			outX = (float) (radius + (outRadius * Math.cos(angle)));
			outY = (float) (radius - (outRadius * Math.sin(angle)));
			inX = (float) (radius + (inRadius * Math.cos(angle)));
			inY = (float) (radius - (inRadius * Math.sin(angle)));
			if (i == (int) (percent * 48)) {

				paint.setColor(defalult_scale_color);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(14);
			}
			canvas.drawLine(outX, outY, inX, inY, paint);
			angle -= (PI / 24);
		}

		if (!TextUtils.isEmpty(percent_text)) {
			textPaint.setColor(finish_scale_color);
			textPaint.setTextSize(DisplayUtil.sp2px(context, 28));

			float textHeight = textPaint.descent() + textPaint.ascent();
			canvas.drawText(percent_text,
					(getWidth() - textPaint.measureText(percent_text)) / 2.0f,
					(getWidth() + (3 * textHeight)) / 2.0f, textPaint);
			textPaint.setColor(defalult_scale_color);
			String step_num_text = "告别臃肿";
			textPaint.setColor(defalult_scale_color);
			textPaint.setTextSize(DisplayUtil.sp2px(context, 14));
			canvas.drawText(step_num_text,
					(getWidth() - textPaint.measureText(step_num_text)) / 2.0f,
					(getWidth()) / 2.0f, textPaint);
			textPaint.setColor(defalult_scale_color);
			canvas.drawText(TIP,
					(getWidth() - textPaint.measureText(TIP)) / 2.0f,
					(getWidth() - 2* textHeight) / 2.0f, textPaint);

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
}
