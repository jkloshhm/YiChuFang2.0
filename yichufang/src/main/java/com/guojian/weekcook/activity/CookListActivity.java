package com.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guojian.weekcook.api.GetJsonUtils;
import com.guojian.weekcook.R;
import com.guojian.weekcook.adapter.CookListAdapter;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.db.DBServices;
import com.guojian.weekcook.db.MyDBServiceUtils;
import com.guojian.weekcook.statusbar.StatusBarCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CookListActivity extends Activity {
    private static List<CookListBean.ResultBean.ListBean> cookBeanList = new ArrayList<>();

    private CookListBean.ResultBean.ListBean cookBean01, cookBeanSql;
    private String TAG = "jkl_CookListActivity";
    private TextView mNameTextView;
    private ListView mLisview;
    private CookListAdapter mCookListAdapter;
    private LinearLayout mLoadingLinearLayout, mNoMassageLinearLayout;
    private ArrayList<CookListBean.ResultBean.ListBean> cookBeenArrayList;
    private ArrayList<String> cookIdList = new ArrayList<>();
    private MyHandler mMyHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CookListActivity ____________onCreate()");
        setContentView(R.layout.activity_cook_list);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(), R.color.red_theme, null), false);
        mNameTextView = (TextView) findViewById(R.id.tv_cook_name);
        mLisview = (ListView) findViewById(R.id.lv_cook_list);
        mLoadingLinearLayout = (LinearLayout) findViewById(R.id.ll_loading_list);
        mNoMassageLinearLayout = (LinearLayout) findViewById(R.id.ll_no_data_massage);
        LinearLayout mBackLinearLayout = (LinearLayout) findViewById(R.id.ll_back_class_home);
        mBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initJsonData();


        mLisview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cookBean01 = cookBeanList.get(position);
                try {
                    Intent intent = new Intent(CookListActivity.this, RecipeDetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("cookBean01", cookBean01);
                    intent.putExtras(b);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "CookListActivity ____________onResume()");
        super.onResume();
        //initDB();
    }

    private void initJsonData() {
        Intent intent = this.getIntent();
        final String CookType = intent.getStringExtra("CookType");
        final String classId = intent.getStringExtra("classId");
        final String name = intent.getStringExtra("name");
        mNameTextView.setText(name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CookType.equals("getDataByClassId")) {
                    GetJsonUtils.getDataByClassId(mMyHandler, classId);
                    Log.i(TAG, "classId=======" + classId);
                } else if (CookType.equals("getDataBySearchName")) {
                    GetJsonUtils.getDataBySearchName(mMyHandler, name);
                }
            }
        }).start();
    }

    private void initDB() {
        try {
            DBServices db = MyDBServiceUtils.getInstance(this);
            cookBeenArrayList = MyDBServiceUtils.getAllObject(db);
            for (int i = 0; i < cookBeenArrayList.size(); i++) {
                String id = cookBeenArrayList.get(i).getId();
                this.cookIdList.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataAndUpdateUIBySearchName(CookListBean.ResultBean dataResultBean) {
        try {
            //cookBeanList.clear();
            if (dataResultBean == null) {
                mLoadingLinearLayout.setVisibility(View.GONE);
                mLisview.setVisibility(View.GONE);
                mNoMassageLinearLayout.setVisibility(View.VISIBLE);
            } else {
                //传过来的就是cookBeanList
                cookBeanList = dataResultBean.getList();
                mCookListAdapter = new CookListAdapter(this, cookBeanList);
                mLisview.setAdapter(mCookListAdapter);
                mLoadingLinearLayout.setVisibility(View.GONE);
                mLisview.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyHandler extends Handler {
        WeakReference<CookListActivity> cookListActivityWeakReference;

        MyHandler(CookListActivity cookListActivity) {
            cookListActivityWeakReference = new WeakReference<>(cookListActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle jsonBundle = msg.getData();
            String classType = jsonBundle.getString("classType");
            String jsonErrorMessage = jsonBundle.getString("errorMessage");

            //Log.i(TAG, "--------->>jsonData====" + jsonData);
            Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            CookListBean.ResultBean stringBody = (CookListBean.ResultBean) jsonBundle.getSerializable("stringBody");
            getDataAndUpdateUIBySearchName(stringBody);
        }
    }
}