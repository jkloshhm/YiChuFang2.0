package com.guojian.weekcook.db;

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
 * @author guojian on 11/17/16.
 */
public class MyDBServiceUtils extends DBServices {
    private static MyDBServiceUtils dbhelper = null;
    private Context context;

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
            database.execSQL("insert into Test001 (_id,person) values(" + Integer.valueOf(cookBean.getId()) + ",?)", new Object[]{data});
            //database.insert()
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CookListBean.ResultBean.ListBean> getAllObject(DBServices db) {
        ArrayList<CookListBean.ResultBean.ListBean> cookBeanList = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from Test001", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex("person"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    CookListBean.ResultBean.ListBean cookBean = (CookListBean.ResultBean.ListBean) inputStream.readObject();
                    cookBeanList.add(cookBean);
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.i("jack_guo", "data-id-----------cursor==null");
        }
        return cookBeanList;
    }

    public static void deleteData(CookListBean.ResultBean.ListBean cookBean, DBServices db) {
        String id = cookBean.getId();
        if (id != null) {
            db.delete("Test001", "_id like ?", new String[]{id});
        }
    }


}
