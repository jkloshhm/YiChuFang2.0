package com.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.activity.CookListActivity;
import com.guojian.weekcook.adapter.ChildrenClassAdapter;
import com.guojian.weekcook.adapter.ParentClassAdapter;
import com.guojian.weekcook.bean.CookClassBean;
import com.guojian.weekcook.api.GetJsonUtils;
import com.guojian.weekcook.utils.GridViewWithHeaderAndFooter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends Fragment {


    private static List<CookClassBean.ResultBean> parentClassBeenList;
    private static List<CookClassBean.ResultBean.ListBean> childrenClassBeenList;
    private static Context mContext;
    private static LinearLayout mLoadingLinearLayout, mParentLinearLayout, mChildrenLinearLayout;
    private static ListView mListViewParent;
    private static GridViewWithHeaderAndFooter mGridViewChildren;
    private static ParentClassAdapter parentClassAdapter;
    private static ChildrenClassAdapter childrenClassAdapter;
    private MyHandler myHandler = new MyHandler(this);
    private View headerView, footerView;

    public ClassFragment() {
        // Required empty public constructor
    }

    private static void initChildrenView(int position) {
        CookClassBean.ResultBean parentClassBean1 = parentClassBeenList.get(position);
        childrenClassBeenList = parentClassBean1.getList();
        childrenClassAdapter = new ChildrenClassAdapter(childrenClassBeenList, mContext);
        mGridViewChildren.setAdapter(childrenClassAdapter);
        parentClassAdapter.setSelectItem(position);
        parentClassAdapter.notifyDataSetInvalidated();
        mLoadingLinearLayout.setVisibility(View.GONE);
        mParentLinearLayout.setVisibility(View.VISIBLE);
        mChildrenLinearLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        // Inflate the layout for this fragment
        View mClassView = inflater.inflate(R.layout.fragment_class, container, false);
        headerView = inflater.inflate(R.layout.layout_class_children_header, container, false);
        footerView = inflater.inflate(R.layout.layout_class_children_footer, container, false);
        mLoadingLinearLayout = (LinearLayout) mClassView.findViewById(R.id.ll_loading);
        mParentLinearLayout = (LinearLayout) mClassView.findViewById(R.id.ll_parent_class);
        mChildrenLinearLayout = (LinearLayout) mClassView.findViewById(R.id.ll_children_class);
        mListViewParent = (ListView) mClassView.findViewById(R.id.lv_parent_class);
        mGridViewChildren = (GridViewWithHeaderAndFooter) mClassView
                .findViewById(R.id.lv_children_class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetJsonUtils.getDataClass(myHandler);
            }
        }).start();

        mListViewParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    initChildrenView(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mGridViewChildren.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridViewChildren.addHeaderView(headerView);
        mGridViewChildren.addFooterView(footerView);
        mGridViewChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CookClassBean.ResultBean.ListBean childrenClassBean1 = childrenClassBeenList.get(position);
                Intent intent = new Intent(mContext, CookListActivity.class);
                intent.putExtra("CookType", "getDataByClassId");
                intent.putExtra("classId", childrenClassBean1.getClassid());
                intent.putExtra("name", childrenClassBean1.getName());
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return mClassView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (childrenClassAdapter != null) {
            childrenClassAdapter = null;
        }
        if (parentClassAdapter != null) {
            parentClassAdapter = null;
        }
        if (mGridViewChildren != null) {
            try {
                //Log.i(TAG, "mGridViewChildren 1111===" + (mGridViewChildren == null));
                mGridViewChildren = null;
                headerView = null;
                footerView = null;
                //Log.i(TAG, "mGridViewChildren 2222===" + (mGridViewChildren == null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Log.i(TAG, "mGridViewChildren 3333===" + (mGridViewChildren == null));
        }
    }

    private class MyHandler extends Handler {
        WeakReference<ClassFragment> classFragmentWeakReference;

        MyHandler(ClassFragment classFragment) {
            classFragmentWeakReference = new WeakReference<>(classFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle jsonBundle = msg.getData();
            CookClassBean cookClassBean = (CookClassBean) jsonBundle.getSerializable("stringBody");
            String classType = jsonBundle.getString("classType");
            String jsonData = jsonBundle.getString("stringBody");
            //Log.i(TAG, "--------->>jsonData====" + jsonData);
            //Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            /*if (jsonData != null) {
                if (classType != null && classType.equals("getDataClass")) {//分类名称
                    getDataAndUpdateUI(jsonData);
                }
            }*/
            getDataAndUpdateUI(cookClassBean);
            //parentClassAdapter.notifyDataSetChanged();
            parentClassAdapter = new ParentClassAdapter(mContext, parentClassBeenList);
            mListViewParent.setAdapter(parentClassAdapter);
            if (parentClassBeenList.size() != 0 && parentClassBeenList.get(0) != null) {
                try {
                    initChildrenView(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void getDataAndUpdateUI(CookClassBean cookClassBean) {
        try {
            parentClassBeenList = cookClassBean.getResult();
            //StringBuffer s = new StringBuffer();
            for (int i = 0; i < cookClassBean.getResult().size(); i++) {
                childrenClassBeenList = cookClassBean.getResult().get(i).getList();
            }
            //Log.i(TAG, "S=" + s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
