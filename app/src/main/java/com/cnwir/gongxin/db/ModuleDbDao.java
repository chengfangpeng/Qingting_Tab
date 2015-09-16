package com.cnwir.gongxin.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.cnwir.gongxin.bean.CardInfo;
import com.cnwir.gongxin.util.LogUtil;

/**
 * 数据库操作
 * 
 * @author Administrator
 * 
 */
public class ModuleDbDao {

	private final static String DATABASE_NAME = "Gongxin.db";
	private final static String tag = "ModuleDbDao";

	ModuleDb helper;
	SQLiteDatabase db;

	public ModuleDbDao(Context con) {
		helper = new ModuleDb(con, DATABASE_NAME, null, 2);
	}

	/**
	 * 插或更新 卡片信息 如果不存在该条数据，就插入；如果存在该条数据，就更新
	 * 
	 * @param module
	 */
	public void insertOrUpdateCard(CardInfo module) {
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(ModuleDb.moduleTable, null, "id=?", new String[] { module.getId() + "" }, null, null,
					null);
			ContentValues values = new ContentValues();
			values.put("moduleText", module.getModuleText());
			values.put("moduleKey", module.getModuleKey());
			values.put("sort", module.getSort());
			values.put("img", getPicture(module.getImg()));
			values.put("desc", module.getDesc());

			if (module.isShow())
				values.put("isShow", 0);
			else
				values.put("isShow", 1);
			if (!cursor.moveToNext()) {
				// 如果不存在该条数据，就插入
				db.insert(ModuleDb.moduleTable, null, values);
			} else {
				// 如果存在该条数据，就更新
				db.update(ModuleDb.moduleTable, values, null, null);
			}
		} catch (Exception e) {
			LogUtil.v(tag, "insertOrUpdateAlerm--" + e.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 批量插入
	 * 
	 * @param cardList
	 */
	public void insertCard(List<CardInfo> cardList) {
		try {
			db = helper.getWritableDatabase();
			for (CardInfo module : cardList) {
				ContentValues values = new ContentValues();
				values.put("moduleText", module.getModuleText());
				values.put("moduleKey", module.getModuleKey());
				values.put("sort", module.getSort());
				values.put("img", getPicture(module.getImg()));
				values.put("desc", module.getDesc());

				if (module.isShow())
					values.put("isShow", 0);
				else
					values.put("isShow", 1);
				// 插入
				db.insert(ModuleDb.moduleTable, null, values);
			}
		} catch (Exception e) {
			LogUtil.v(tag, "insertAlerm list--" + e.toString());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 更新卡片信息
	 * 
	 * @param module
	 */
	public void updateCard(CardInfo module) {
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(ModuleDb.moduleTable, null, "id=?", new String[] { module.getId() + "" }, null, null,
					null);
			ContentValues values = new ContentValues();
			values.put("moduleText", module.getModuleText());
			values.put("moduleKey", module.getModuleKey());
			values.put("sort", module.getSort());
			values.put("img", getPicture(module.getImg()));
			values.put("desc", module.getDesc());

			if (module.isShow())
				values.put("isShow", 0);
			else
				values.put("isShow", 1);
			// 更新
			db.update(ModuleDb.moduleTable, values, "id=?", new String[] { module.getId() + "" });
		} catch (Exception e) {
			LogUtil.v(tag, "insertOrUpdateAlerm--" + e.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 获取卡片信息
	 * 
	 * @return List<CardInfo>
	 */
	public List<CardInfo> getAllCardInfo() {
		List<CardInfo> cardList = new ArrayList<CardInfo>();
		CardInfo info = null;
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(ModuleDb.moduleTable, null, null, null, null, null, "sort asc");
			while (cursor.moveToNext()) {
				info = new CardInfo();
				info.setId(cursor.getInt(cursor.getColumnIndex("id")));

				int complete = cursor.getInt(cursor.getColumnIndex("isShow"));
				if (complete == 0)
					info.setShow(true);
				else
					info.setShow(false);

				info.setModuleText(cursor.getString(cursor.getColumnIndex("moduleText")));
				info.setModuleKey(cursor.getString(cursor.getColumnIndex("moduleKey")));
				info.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
				info.setImg(byteToDrawable(cursor.getBlob(cursor.getColumnIndex("img"))));
				info.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
				cardList.add(info);
			}
		} catch (Exception e) {
			LogUtil.v(tag, "getAllAlermInfo--" + e.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return cardList;
	}

	/**
	 * 根据key 查找显示位置index
	 * 
	 * @author wangwm 2015年4月17日 上午10:39:58
	 * @param key
	 *            moduleKey
	 * @return key -1失败
	 */
	public int getIndexFromkey(String key) {
		int sort = -1;
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(ModuleDb.moduleTable, null, "moduleKey=?", new String[] { key }, null, null, null);
			sort = cursor.getInt(cursor.getColumnIndex("sort"));

		} catch (Exception e) {
			LogUtil.v(tag, "getAllAlermInfo--" + e.toString());
		}
		return sort;
	}

	/**
	 * 修改排序
	 * @author wangwm
	 * 2015年4月20日 下午6:49:39
	 * @param from  原本的排序
	 * @param to    切换后的排序
	 */
	public void changeSort(int from, int to) {
		Cursor cursor = null;
		int id =0;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(ModuleDb.moduleTable, null, "sort=?", new String[] { to + "" }, null, null, null);
			if(cursor.moveToNext())
			 id = cursor.getInt(cursor.getColumnIndex("id"));
			cursor = db.query(ModuleDb.moduleTable, null, "sort=?", new String[] { from + "" }, null, null, null);
			if (cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				values.put("sort", to);
				db.update(ModuleDb.moduleTable, values, "id=?", new String[] { cursor.getInt(cursor.getColumnIndex("id")) + "" });
			}
			if (from > to) {
					cursor = db.query(ModuleDb.moduleTable, null, "sort>? and sort<?", new String[] { to + "" ,from+""}, null, null, "sort asc");
					while (cursor.moveToNext()) {
						ContentValues values = new ContentValues();
						int i = cursor.getInt(cursor.getColumnIndex("id"));
						values.put("sort", cursor.getInt(cursor.getColumnIndex("sort")) + 1);
						db.update(ModuleDb.moduleTable, values, "id=?", new String[] { i + "" });
					}
			}
			
			if (from < to) {
				cursor = db.query(ModuleDb.moduleTable, null, "sort>? and sort<?", new String[] { from + "" ,to+""}, null, null, "sort asc");
				while (cursor.moveToNext()) {
					ContentValues values = new ContentValues();
					int i = cursor.getInt(cursor.getColumnIndex("id"));
					values.put("sort", cursor.getInt(cursor.getColumnIndex("sort")) - 1);
					db.update(ModuleDb.moduleTable, values, "id=?", new String[] { i + "" });
				}
			}
			cursor = db.query(ModuleDb.moduleTable, null, "sort=?", new String[] { to + "" }, null, null, null);
			if (cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				if (from > to)
					values.put("sort", to + 1);
				else
					values.put("sort", to - 1);
				db.update(ModuleDb.moduleTable, values, "id=?", new String[] { id + "" });
			}
			

		} catch (Exception e) {
			LogUtil.v(tag, "getAllAlermInfo--" + e.toString());
		}
	}

	private Drawable byteToDrawable(byte[] b) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		Drawable drawable = bitmapDrawable;
		return drawable;
	}

	// 将drawable转换成可以用来存储的byte[]类型
	private byte[] getPicture(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}

}
