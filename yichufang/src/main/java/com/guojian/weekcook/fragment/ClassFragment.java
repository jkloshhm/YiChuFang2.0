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
import com.guojian.weekcook.bean.ChildrenClassBean;
import com.guojian.weekcook.bean.ParentClassBean;
import com.guojian.weekcook.utils.GetJsonUtils;
import com.guojian.weekcook.utils.GridViewWithHeaderAndFooter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends Fragment {


    private static List<ParentClassBean> parentClassBeenList;
    private static List<ChildrenClassBean> childrenClassBeenList;
    private static Context mContext;
    private static LinearLayout mLoadingLinearLayout, mParentLinearLayout, mChildrenLinearLayout;
    //private static String TAG = "guojian_CookDemo_ClassFragment";
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
        ParentClassBean parentClassBean1 = parentClassBeenList.get(position);
        childrenClassBeenList = parentClassBean1.getChildrenClassBeen();
        childrenClassAdapter = new ChildrenClassAdapter(childrenClassBeenList, mContext);
        mGridViewChildren.setAdapter(childrenClassAdapter);
        parentClassAdapter.setSelectItem(position);
        parentClassAdapter.notifyDataSetInvalidated();
        mLoadingLinearLayout.setVisibility(View.GONE);
        mParentLinearLayout.setVisibility(View.VISIBLE);
        mChildrenLinearLayout.setVisibility(View.VISIBLE);
    }

    private static void getDataAndUpdateUI(String data) {
        try {
            JSONObject dataJsonObject = new JSONObject(data);
            String result = dataJsonObject.getString("result");
            JSONArray resultJsonArray = new JSONArray(result);
            //StringBuffer s = new StringBuffer();
            for (int i = 0; i < resultJsonArray.length(); i++) {
                JSONObject resultJsonObject = resultJsonArray.getJSONObject(i);
                String classId_parent = resultJsonObject.getString("classid");
                String className_parent = resultJsonObject.getString("name");
                String parentId_parent = resultJsonObject.getString("parentid");
                JSONArray list_parent = resultJsonObject.getJSONArray("list");
                childrenClassBeenList = new ArrayList<>();
                for (int j = 0; j < list_parent.length(); j++) {
                    JSONObject list_children = list_parent.getJSONObject(j);
                    ChildrenClassBean childrenClassBean = new ChildrenClassBean(
                            list_children.getString("classid"),
                            list_children.getString("name"),
                            list_children.getString("parentid"));
                    childrenClassBeenList.add(childrenClassBean);
                    //Log.i(TAG, "name=========" + list_children.getString("name"));
                }
                ParentClassBean parentClassBean = new ParentClassBean(
                        childrenClassBeenList,
                        classId_parent,
                        className_parent,
                        parentId_parent);
                parentClassBeenList.add(parentClassBean);
                /*s.append(classId_parent + "-" +
                        className_parent + "-" +
                        parentId_parent + "\n");*/
            }
            //Log.i(TAG, "S=" + s);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        parentClassBeenList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetJsonUtils.GetDataClass(myHandler);
            }
        }).start();
        parentClassAdapter = new ParentClassAdapter(mContext, parentClassBeenList);
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
                ChildrenClassBean childrenClassBean1 = childrenClassBeenList.get(position);
                Intent intent = new Intent(mContext, CookListActivity.class);
                intent.putExtra("CookType", "GetDataByClassId");
                intent.putExtra("classId", childrenClassBean1.getChildrenClassId());
                intent.putExtra("name", childrenClassBean1.getChildrenClassName());
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
        if (childrenClassAdapter != null) childrenClassAdapter = null;
        if (parentClassAdapter != null) parentClassAdapter = null;
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
            String classType = jsonBundle.getString("classType");
            //String jsonErrorMessage = jsonBundle.getString("errorMessage");
            String jsonData = jsonBundle.getString("stringBody");
            //Log.i(TAG, "--------->>jsonData====" + jsonData);
            //Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            if (jsonData != null) {
                if (classType != null && classType.equals("GetDataClass")) {//分类名称
                    getDataAndUpdateUI(jsonData);
                }
            }
            //parentClassAdapter.notifyDataSetChanged();
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
}
