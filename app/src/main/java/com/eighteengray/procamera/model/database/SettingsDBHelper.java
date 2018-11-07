package com.eighteengray.procamera.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SettingsDBHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "settings.db"; // 数据库名称
	private static final int DATABASE_VERSION = 1;// 数据库版本


	public SettingsDBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String sql = "CREATE TABLE settings " +
				"(_id integer primary key autoincrement, name varchar(20), age varchar(10))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		update(db, oldVersion, newVersion);
	}

	/**
	 * 本版本实现的优势：
	 1、首先采用了单例模式，如果不懂什么单例以及线程安全请自行google。
	 2、采用递归的方式解决了数据库跨越升级的问题
	 3、采用工厂模式达到了易扩展
	 */
	public static void update(SQLiteDatabase db, int oldVersion, int newVersion){
		Upgrade upgrade = null;
		if (oldVersion < newVersion) {
			oldVersion++;
			upgrade = VersionFactory.getUpgrade(oldVersion);
			if (upgrade == null) {
				return;
			}
			upgrade.update(db);
			update(db, oldVersion, newVersion);
		}
	}

}