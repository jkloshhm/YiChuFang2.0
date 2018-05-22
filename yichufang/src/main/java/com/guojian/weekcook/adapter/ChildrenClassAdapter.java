package com.guojian.weekcook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.bean.CookClassBean;

import java.util.List;

/**
 * @author Created by guojian on 2016-11-14. 分类页面二级分类页面Adapter
 */
public class ChildrenClassAdapter extends BaseAdapter {
    private Context context;
    private List<CookClassBean.ResultBean.ListBean> childrenClassList;

    public ChildrenClassAdapter(List<CookClassBean.ResultBean.ListBean> childrenClassList, Context context) {
        this.childrenClassList = childrenClassList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return childrenClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.children_class_adapter_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_children_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CookClassBean.ResultBean.ListBean childrenClassBean = childrenClassList.get(position);
        holder.textView.setText(childrenClassBean.getName());
        return convertView;
    }

    class ViewHolder {
        private TextView textView;
    }

}
