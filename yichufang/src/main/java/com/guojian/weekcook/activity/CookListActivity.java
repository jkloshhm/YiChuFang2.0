package com.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.adapter.CookListAdapter;
import com.guojian.weekcook.adapter.CookRecyclerViewListAdapter;
import com.guojian.weekcook.api.HttpUtils;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.statusbar.StatusBarCompat;
import com.guojian.weekcook.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jack.guo 2017-02-12  菜谱列表页面Activity
 */
public class CookListActivity extends Activity {
    private static List<CookListBean.ResultBean.ListBean> cookBeanList = new ArrayList<>();

    private CookListBean.ResultBean.ListBean cookBean01;
    private String TAG = "jkl_CookListActivity";
    private TextView mNameTextView;
    //private ListView mLisview;
    private RecyclerView mRecyclerView;
    private CookListAdapter mCookListAdapter;
    private CookRecyclerViewListAdapter mRecyclerViewListAdapter;
    private LinearLayout mLoadingLinearLayout, mNoMassageLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CookListActivity ____________onCreate()");
        setContentView(R.layout.activity_cook_list);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(), R.color.red_theme, null), false);
        mNameTextView = findViewById(R.id.tv_cook_name);
        //mLisview = findViewById(R.id.lv_cook_list);
        mRecyclerView = findViewById(R.id.lv_cook_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mLoadingLinearLayout = findViewById(R.id.ll_loading_list);
        mNoMassageLinearLayout = findViewById(R.id.ll_no_data_massage);
        LinearLayout mBackLinearLayout = findViewById(R.id.ll_back_class_home);
        mBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initJsonData();
    }

    private void initJsonData() {
        Intent intent = this.getIntent();
        final String CookType = intent.getStringExtra("CookType");
        final String classId = intent.getStringExtra("classId");
        final String name = intent.getStringExtra("name");
        mNameTextView.setText(name);
        if (CookType.equals("getDataByClassId")) {
            Log.i(TAG, "classId=======" + classId);
            HttpUtils.request(
                    HttpUtils.createApiCook().getDataByClassId(Integer.parseInt(classId), 20, 0),
                    new HttpUtils.IResponseListener<CookListBean>() {
                        @Override
                        public void onSuccess(CookListBean data) {
                            try {
                                if (data == null) {
                                    mLoadingLinearLayout.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.GONE);
                                    mNoMassageLinearLayout.setVisibility(View.VISIBLE);
                                } else {
                                    //传过来的就是cookBeanList
                                    cookBeanList = data.getResult().getList();
                                    mRecyclerViewListAdapter = new CookRecyclerViewListAdapter(CookListActivity.this, cookBeanList);
                                    mRecyclerView.setAdapter(mRecyclerViewListAdapter);
                                    mLoadingLinearLayout.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    ToastUtils.showShortToast("classId列表数据加载成功~ 列表数据size：" + cookBeanList.size());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail() {
                            ToastUtils.showShortToast("classId列表数据加载加载失败~");
                        }
                    });
        } else if (CookType.equals("getDataBySearchName")) {
            HttpUtils.request(
                    HttpUtils.createApiCook().getDataByKeyword(name, 20, 0),
                    new HttpUtils.IResponseListener<CookListBean>() {
                        @Override
                        public void onSuccess(CookListBean data) {
                            try {
                                if (data == null) {
                                    mLoadingLinearLayout.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.GONE);
                                    mNoMassageLinearLayout.setVisibility(View.VISIBLE);
                                } else {
                                    //传过来的就是cookBeanList
                                    cookBeanList = data.getResult().getList();

                                    mRecyclerViewListAdapter = new CookRecyclerViewListAdapter(CookListActivity.this, cookBeanList);
                                    mRecyclerView.setAdapter(mRecyclerViewListAdapter);
                                    mLoadingLinearLayout.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    ToastUtils.showShortToast("name列表数据加载成功~ 列表数据size：" + cookBeanList.size());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail() {
                            ToastUtils.showShortToast("name列表数据加载加载失败~");
                        }
                    });
        }
    }

}