package com.guojian.weekcook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.guojian.weekcook.R;
import com.guojian.weekcook.adapter.CookListAdapter;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.dao.DBServices;
import com.guojian.weekcook.dao.MyDBServiceUtils;
import com.guojian.weekcook.statusbar.StatusBarCompat;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    private static DBServices db;
    private static ArrayList<CookListBean.ResultBean.ListBean> cookBeanlist;
    private static CookListBean.ResultBean.ListBean cookBean;
    //private ArrayList<String> array = new ArrayList<String>();
    private CookListAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("jkloshhm", "CollectionActivity____________onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(),R.color.red_theme,null), false);
        lv = (ListView) findViewById(R.id.lv_my_collection_list);
        final LinearLayout mBackLinearLayout = (LinearLayout) findViewById(R.id.ll_back_to_my_home);
        if (mBackLinearLayout != null) {
            mBackLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cookBean = cookBeanlist.get(position);
                try {
                    Intent intent = new Intent(CollectionActivity.this, RecipeDetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("cookBean01", cookBean);
                    intent.putExtras(b);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                cookBean = cookBeanlist.get(position);
                setCancelCollection();
                return true;
            }
        });
    }

    //是否取消收藏
    public void setCancelCollection() {
        //提示对话框
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setView(getLayoutInflater().inflate(R.layout.alert_dialog_view, null));
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "移除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyDBServiceUtils.delectData(cookBean, db);
                        //Toast.makeText(CollectionActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        initDB();
                        Toast.makeText(CollectionActivity.this, "已取消收藏~", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        Button pButton = builder.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(getResources().getColor(R.color.red_theme));
        Button nButton = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(getResources().getColor(R.color.gray));
    }


    private void initDB() {
        db = MyDBServiceUtils.getInstance(this);
        cookBeanlist = MyDBServiceUtils.getAllObject(db);
        Log.i("jkloshhm", "CollectionActivity____________cookBeanlist.size()" + cookBeanlist.size());
        /*for (int i = 0; i < cookBeanlist.size(); i++) {
            String object = cookBeanlist.get(i).getName_cook();
            this.array.add(object);
        }*/
        adapter = new CookListAdapter(this, cookBeanlist);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
