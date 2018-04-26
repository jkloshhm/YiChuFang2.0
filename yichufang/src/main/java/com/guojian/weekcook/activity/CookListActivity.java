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

import com.guojian.weekcook.R;
import com.guojian.weekcook.adapter.CookListAdapter;
import com.guojian.weekcook.bean.CookBean;
import com.guojian.weekcook.bean.MaterialBean;
import com.guojian.weekcook.bean.ProcessBean;
import com.guojian.weekcook.dao.DBServices;
import com.guojian.weekcook.dao.MyDBServiceUtils;
import com.guojian.weekcook.statusbar.StatusBarCompat;
import com.guojian.weekcook.utils.GetJsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CookListActivity extends Activity {
    private static List<CookBean> cookBeanList = new ArrayList<>();

    private CookBean cookBean01, cookBeanSql;
    private String TAG = "jkl_CookListActivity";
    private TextView mNameTextView;
    private ListView mLisview;
    private CookListAdapter mCookListAdapter;
    private LinearLayout mLoadingLinearLayout, mNoMassageLinearLayout;
    private ArrayList<CookBean> cookBeenArrayList;
    private ArrayList<String> cookIdList = new ArrayList<>();
    private MyHandler mMyHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CookListActivity ____________onCreate()");
        setContentView(R.layout.activity_cook_list);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(),R.color.red_theme,null), false);
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

        mCookListAdapter = new CookListAdapter(this, cookBeanList);
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
        initDB();
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
                if (CookType.equals("GetDataByClassId")) {
                    GetJsonUtils.GetDataByClassId(mMyHandler, classId);
                    Log.i(TAG, "classId=======" + classId);
                } else if (CookType.equals("GetDataBySearchName")) {
                    GetJsonUtils.GetDataBySearchName(mMyHandler, name);
                }
            }
        }).start();
    }

    private void initDB() {
        try {
            DBServices db = MyDBServiceUtils.getInstance(this);
            cookBeenArrayList = MyDBServiceUtils.getAllObject(db);
            for (int i = 0; i < cookBeenArrayList.size(); i++) {
                String cook_id = cookBeenArrayList.get(i).getId_cook();
                this.cookIdList.add(cook_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataAndUpdateUI(String data) {
        try {
            cookBeanList.clear();
            JSONObject dataJsonObject = new JSONObject(data);
            String result = dataJsonObject.getString("result");
            String status = dataJsonObject.getString("status");
            String statusMsg = dataJsonObject.getString("msg");
            Log.i(TAG, "--------result----->->->->--" + result);
            if (result.equals("") && status.equals("205")
                    && statusMsg.equals("没有信息")) {
                mLoadingLinearLayout.setVisibility(View.GONE);
                mLisview.setVisibility(View.GONE);
                mNoMassageLinearLayout.setVisibility(View.VISIBLE);
            } else {
                JSONObject resultJsonObject = new JSONObject(result);
                JSONArray listJsonArray = resultJsonObject.getJSONArray("list");
                for (int i = 0; i < listJsonArray.length(); i++) {
                    JSONObject cookJsonObject = listJsonArray.getJSONObject(i);
                    String id_cook = cookJsonObject.getString("id");
                    String classid_cook = cookJsonObject.getString("classid");
                    String name_cook = cookJsonObject.getString("name");
                    String peoplenum = cookJsonObject.getString("peoplenum");
                    String preparetime = cookJsonObject.getString("preparetime");
                    String cookingtime = cookJsonObject.getString("cookingtime");
                    String content = cookJsonObject.getString("content");
                    String pic = cookJsonObject.getString("pic");
                    String tag = cookJsonObject.getString("tag");
                    String maryString = "mary";
                    JSONArray materialArray = cookJsonObject.getJSONArray("material");
                    JSONArray processJsonArray = cookJsonObject.getJSONArray("process");
                    List<MaterialBean> materialBeanList = new ArrayList<>();
                    for (int j = 0; j < materialArray.length(); j++) {
                        JSONObject materialJsonObject = materialArray.getJSONObject(j);
                        materialBeanList.add(new MaterialBean(
                                //String amount, String mname, String type
                                materialJsonObject.getString("amount"),
                                materialJsonObject.getString("mname"),
                                materialJsonObject.getString("type")));
                    }
                    List<ProcessBean> processBeanList = new ArrayList<>();
                    for (int k = 0; k < processJsonArray.length(); k++) {
                        JSONObject processJsonObject = processJsonArray.getJSONObject(k);
                        processBeanList.add(new ProcessBean(
                                processJsonObject.getString("pcontent"),
                                processJsonObject.getString("pic")));
                    }

                    if (cookIdList.contains(id_cook)) {
                        for (int j = 0; j < cookBeenArrayList.size(); j++) {
                            if (cookBeenArrayList.get(j).getId_cook().equals(id_cook)) {
                                cookBeanSql = cookBeenArrayList.get(j);
                            }
                        }
                        cookBeanList.add(cookBeanSql);
                        Log.i(TAG, "if------------if-----------");
                    } else {
                        cookBeanList.add(new CookBean(id_cook, classid_cook, name_cook,
                                peoplenum, preparetime,
                                cookingtime, content, pic,
                                tag, materialBeanList, processBeanList, maryString));
                        Log.i(TAG, "else------------else-----------");
                    }
                }
                mLisview.setAdapter(mCookListAdapter);
                mLoadingLinearLayout.setVisibility(View.GONE);
                mLisview.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "CookListActivity ____________onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "CookListActivity ____________onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "CookListActivity ____________onStop()");
        super.onStop();
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
            String jsonData = jsonBundle.getString("stringBody");
            //Log.i(TAG, "--------->>jsonData====" + jsonData);
            Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            if (jsonData != null) {
                if (classType != null && classType.equals("GetDataBySearchName")) {//按名称搜索菜谱
                    getDataAndUpdateUI(jsonData);
                } /*else if (classType != null && classType.equals("GetDataClass")) {//分类名称
                } */ else if (classType != null && classType.equals("GetDataByClassId")) {//分类名称ID
                    getDataAndUpdateUI(jsonData);
                }
            }
        }
    }
}