package com.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.guojian.weekcook.api.HttpUtils;
import com.guojian.weekcook.bean.CookClassBean;
import com.guojian.weekcook.utils.GridViewWithHeaderAndFooter;
import com.guojian.weekcook.utils.ToastUtils;


import java.util.List;


/**
 * @author Created by jkloshhm on 2017/6/24. 分类页面
 */
public class ClassFragment extends Fragment {


    private List<CookClassBean.ResultBean> parentClassBeenList;
    private List<CookClassBean.ResultBean.ListBean> childrenClassBeenList;
    private Context mContext;
    private LinearLayout mLoadingLinearLayout, mParentLinearLayout, mChildrenLinearLayout;
    private ListView mListViewParent;
    private GridViewWithHeaderAndFooter mGridViewChildren;
    private ParentClassAdapter parentClassAdapter;
    private ChildrenClassAdapter childrenClassAdapter;
    private View headerView, footerView;

    public ClassFragment() {
        // Required empty public constructor
    }

    private void initChildrenView(int position) {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        // Inflate the layout for this fragment
        View mClassView = inflater.inflate(R.layout.fragment_class, container, false);
        headerView = inflater.inflate(R.layout.layout_class_children_header, container, false);
        footerView = inflater.inflate(R.layout.layout_class_children_footer, container, false);
        mLoadingLinearLayout = mClassView.findViewById(R.id.ll_loading);
        mParentLinearLayout = mClassView.findViewById(R.id.ll_parent_class);
        mChildrenLinearLayout = mClassView.findViewById(R.id.ll_children_class);
        mListViewParent = mClassView.findViewById(R.id.lv_parent_class);
        mGridViewChildren = mClassView.findViewById(R.id.lv_children_class);

        HttpUtils.request(HttpUtils.createApiCook().getDataClass(), new HttpUtils.IResponseListener<CookClassBean>() {
            @Override
            public void onSuccess(CookClassBean data) {
                mLoadingLinearLayout.setVisibility(View.GONE);
                try {
                    parentClassBeenList = data.getResult();
                    for (int i = 0; i < data.getResult().size(); i++) {
                        childrenClassBeenList = data.getResult().get(i).getList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

            @Override
            public void onFail() {
                mLoadingLinearLayout.setVisibility(View.VISIBLE);
                ToastUtils.showShortToast("加载出错了~");
            }
        });

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
                mGridViewChildren = null;
                headerView = null;
                footerView = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
