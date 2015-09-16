package com.cnwir.gongxin.util;

import com.cnwir.gongxin.R;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class TextToBitmapUtils {

	/**
	 * 功能描述：将文本转化成bitmap
	 * 
	 * */

	public static Bitmap textToBitmap(String text) {
		
		
		String str = text.substring(0, 1);
		Bitmap bitmap = Bitmap.createBitmap(150, 150, Config.ARGB_8888);// 创建一个宽度和高度都是400、32位ARGB图
		Canvas canvas = new Canvas(bitmap);// 初始化画布绘制的图像到icon上
		canvas.drawColor(Color.rgb(2, 174, 240));// 图层的背景色
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 创建画笔
		paint.setTextSize(100.0f);// 设置文字的大小
		paint.setTypeface(Typeface.DEFAULT_BOLD);// 文字的样式(加粗)
		paint.setColor(Color.WHITE);// 文字的颜色
		canvas.drawText(str, bitmap.getWidth() / 2 - 30, bitmap.getHeight() / 2 + 30, paint);// 将文字写入。这里面的（120，130）代表着文字在图层上的初始位置
		canvas.save(canvas.ALL_SAVE_FLAG);// 保存所有图层
		canvas.restore();
		return bitmap;

	}

}
