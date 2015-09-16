package com.cnwir.gongxin.db;

import com.cnwir.gongxin.util.LogUtil;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ModuleDb extends SQLiteOpenHelper {
	
	public static final String moduleTable = "modules";
	
	// 主闹铃4要素
	private String sql_create_base = "create table IF NOT EXISTS " + moduleTable
			+ " ( id integer primary key autoincrement ," + " moduleText varchar(50), moduleKey varchar(50),"
			+ " sort integer, img blob,desc varchar(100),isShow integer default 0 )";

	private String[] sqlArray = { sql_create_base};

	public ModuleDb(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/** 创建数据库时调用 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 执行sql语句
		try {
			for (int i = 0; i < sqlArray.length; i++) {
				LogUtil.v("DatabaseHelper", "onCreate--" + i);
				db.execSQL(sqlArray[i]);
			}
		} catch (SQLException e) {
			LogUtil.v("DatabaseHelper", "onCreate" + e.toString());
		}
	}

	/** 版本更新时调用 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/** 创建或打开一个只读数据库 */
	@Override
	public SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}

	/** 创建或打开一个读写数据库 */
	@Override
	public SQLiteDatabase getWritableDatabase() {
		return super.getWritableDatabase();
	}

}
