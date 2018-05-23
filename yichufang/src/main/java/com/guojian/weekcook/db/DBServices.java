package com.guojian.weekcook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author guojian on 11/18/16.
 */
public class DBServices extends SQLiteOpenHelper {

    public final static int version = 2;
    public final static String dbName = "Test001";


    //表里面的三个内容
    private static final String ID = "_id";
    private static final String NAME = "_cook";
    private static final String RealID = "_real_id";
    private static final String TABLE_NAME = "Test01";

    public DBServices(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        //创建表
        String create_mail_sql = "CREATE TABLE if not exists [Test001]" +
                "(_id integer primary key autoincrement, person text)";

        /*String create_mail_sql =
        "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER primary key autoincrement, "
         + NAME +" text, " + RealID + " INTEGER);"; */
        db.execSQL(create_mail_sql);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //插入
    public void insert(String table, String nullColumnHack, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.insert(table, nullColumnHack, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //删除
    public void delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.delete(table, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //更新
    public void update(String table, ContentValues values,
                       String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.update(table, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //更新
    public void updateData(String table, ContentValues values,
                           String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.update(table, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //读取
    public Cursor read(String sql, String[] args) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery(sql, args);
        db.setTransactionSuccessful();
        db.endTransaction();
        return cursor;
    }
}
