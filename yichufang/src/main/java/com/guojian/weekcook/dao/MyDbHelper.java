package com.guojian.weekcook.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guojian on 11/17/16.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    private static MyDbHelper dbhelper = null;

    private MyDbHelper(Context context) {
        super(context, "datebase.db", null, 1);

    }

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    public static MyDbHelper getInstens(Context context) {
        if (dbhelper == null) {
            dbhelper = new MyDbHelper(context);
        }
        return dbhelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_class_table = "create table if not exists classtable(" +
                "_id integer primary key autoincrement, classtabledata text)";
        db.execSQL(sql_class_table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
