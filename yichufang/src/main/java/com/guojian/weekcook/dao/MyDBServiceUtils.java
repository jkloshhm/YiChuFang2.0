package com.guojian.weekcook.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.guojian.weekcook.bean.CookListBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by guojian on 11/17/16.
 */
public class MyDBServiceUtils extends DBServices {
    private static MyDBServiceUtils dbhelper = null;
    Context context;

    public MyDBServiceUtils(Context context) {
        super(context);
        this.context = context;
    }

    public static MyDBServiceUtils getInstance(Context context) {
        if (dbhelper == null) {
            dbhelper = new MyDBServiceUtils(context);
        }
        return dbhelper;
    }

    //把cookBean保存到数据库
    public static void saveData(CookListBean.ResultBean.ListBean cookBean, DBServices db) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(cookBean);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = db.getWritableDatabase();
            database.execSQL("insert into Test001 (person) values(?)", new Object[]{data});
            //database.insert()
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CookListBean.ResultBean.ListBean> getAllObject(DBServices db) {
        ArrayList<CookListBean.ResultBean.ListBean> cookBeanList = new ArrayList<CookListBean.ResultBean.ListBean>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from Test001", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.i("data-id", "data-id=====" + cursor.getString(0));
                String cursorString = cursor.getString(0);
                byte data[] = cursor.getBlob(cursor.getColumnIndex("person"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    CookListBean.ResultBean.ListBean cookBean = (CookListBean.ResultBean.ListBean) inputStream.readObject();
                    //cookBean.setReal_ip(cursorString);
                    cookBeanList.add(cookBean);
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            Log.i("data-id","data-id-----------cursor==null");
        }
        Log.d("guojian_Persons-Count", Integer.toString(cookBeanList.size()));
        return cookBeanList;
    }

    public static void delectData(CookListBean.ResultBean.ListBean cookBean, DBServices db) {
        String cursor_id = cookBean.getId();
        if (cursor_id != null) {
            db.delete("Test001", "id like ?", new String[]{cursor_id});
        }
    }


/*
//保存
    public  void saveObject(CookBean cookBean) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(cookBean);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            MyDbHelper dbhelper = MyDbHelper.getInstens(context);
            SQLiteDatabase database = dbhelper.getWritableDatabase();
            database.execSQL("insert into classtable (classtabledata) values(?)",
            new Object[] { data });
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//读取
    public CookBean getObject() {
        CookBean cookBean = null;
        MyDbHelper dbhelper = MyDbHelper.getInstens(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from classtable", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex("classtabledata"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    cookBean = (CookBean) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                    break;//这里为了测试就取一个数据
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return cookBean;

    }*/

}
