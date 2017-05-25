package com.eighteengray.procamera.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * openOrCreateDatabase被隐藏在了SQLiteOpenHelper构造函数中
 */
public class DBOpenHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "procamera.db"; // 数据库名称
	private static final int DATABASE_VERSION = 1;// 数据库版本


	public DBOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DataBaseExpression.CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS procamera");
		onCreate(db);
	}
}