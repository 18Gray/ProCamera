package com.eighteengray.procamera.model.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;



public class PersonContentProvider extends ContentProvider
{
	private DBOpenHelper dbOpenHelper;

	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int PERSONS = 1;
	private static final int PERSON = 2;
	static
	{
		// 如果match()方法匹配content://cn.com.karl.personProvider/person路径，返回匹配码1
		MATCHER.addURI("cn.com.personProvider", "person", PERSONS);
		// 如果match()方法匹配content://cn.com.karl.personProvider/person/230路径，返回匹配码2，#号为通配符?
		MATCHER.addURI("cn.com.personProvider", "person/#", PERSON);
	}

	@Override
	public boolean onCreate()
	{
		this.dbOpenHelper = new DBOpenHelper(this.getContext());
		return false;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (MATCHER.match(uri))
		{
			case PERSONS:
				return "vnd.android.cursor.dir/person";
			case PERSON:
				return "vnd.android.cursor.item/person";
			default:
				throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		switch (MATCHER.match(uri))
		{
		// 查询所有person信息，参数分别为：
		// 参数table:表名称，参数columns:列名称数组，参数selection:条件字句，相当于where，参数selectionArgs:条件字句，参数数组
		//参数groupBy:分组列，参数having:分组条件，参数orderBy:排序列，参数limit:分页查询限制，参数Cursor:返回值，相当于结果集ResultSet
		case PERSONS:
			return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
			// 查询对应id的一个person信息
		case PERSON:
			long id = ContentUris.parseId(uri);// 得到uri中的id
			String where = "_id=" + id;
			if (selection != null && !"".equals(selection))
			{
				where = selection + " and " + where;
			}
			return db.query("person", projection, where, selectionArgs, null, null, sortOrder);
		default:
			throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		switch (MATCHER.match(uri))
		{
		case PERSONS:
			// 第一个参数person是数据库中的表，第二个参数是指当name字段为空时，将自动插入一个NULL?
			long rowid = db.insert("person", "name", values);
			Uri insertUri = ContentUris.withAppendedId(uri, rowid);// 得到代表新增记录的Uri
			this.getContext().getContentResolver().notifyChange(uri, null);// 刷新
			return insertUri;
		default:
			throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int count = 0;
		switch (MATCHER.match(uri))
		{
		case PERSONS:
			count = db.delete("person", selection, selectionArgs);
			return count;
		case PERSON:
			long id = ContentUris.parseId(uri);// 得到uri中的id
			String where = "_id=" + id;
			if (selection != null && !"".equals(selection))
			{
				where = selection + " and " + where;
			}
			count = db.delete("person", where, selectionArgs);
			return count;
		default:
			throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int count = 0;
		switch (MATCHER.match(uri))
		{
		case PERSONS:
			count = db.update("person", values, selection, selectionArgs);
			return count;

		case PERSON:
			db.beginTransaction();
			long id = ContentUris.parseId(uri);
			String where = "_id=" + id;
			if (selection != null && !"".equals(selection))
			{
				where = selection + " and " + where;
			}
			count = db.update("person", values, where, selectionArgs);
			db.setTransactionSuccessful();
			db.endTransaction();
			return count;

		default:
			throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
	}


}