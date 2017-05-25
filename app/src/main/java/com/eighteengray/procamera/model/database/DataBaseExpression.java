package com.eighteengray.procamera.model.database;


/**
 * SQLite数据类型：NULL，INTEGER，REAL，TEXT/VARCHAR,Date/Time
 * c.move(int offset); //以当前位置为参考,移动到指定行
 c.moveToFirst();    //移动到第一行
 c.moveToLast();     //移动到最后一行
 c.moveToPosition(int position); //移动到指定行
 c.moveToPrevious(); //移动到前一行
 c.moveToNext();     //移动到下一行
 c.isFirst();        //是否指向第一条
 c.isLast();     //是否指向最后一条
 c.isBeforeFirst();  //是否指向第一条之前
 c.isAfterLast();    //是否指向最后一条之后
 c.isNull(int columnIndex);  //指定列是否为空(列基数为0)
 c.isClosed();       //游标是否已关闭
 c.getCount();       //总数据项数
 c.getPosition();    //返回当前游标所指向的行数
 c.getColumnIndex(String columnName);//返回某列名对应的列索引值
 c.getString(int columnIndex);   //返回当前行指定列的值
 */
public class DataBaseExpression
{
    //数据库字段名
    public static final String IMAGEPATH = "imagepath";



    //创建表
    public static final String CREATE_DATABASE = "CREATE TABLE procamera " +
            "(_id integer primary key autoincrement, name varchar(20), age varchar(10))";

    String stu_sql="insert into stu_table(sname,snumber) values('xiaoming','01005')";
    String sql = "delete from stu_table where _id = 6";
    String sql2 = "update stu_table set snumber = 654321 where id = 1";











}
