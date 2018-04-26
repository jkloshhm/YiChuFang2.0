package com.guojian.weekcook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.bean.ChildrenClassBean;

import java.util.List;


/**
 * Created by guojian on 11/4/16.
 */
public class HotMaterialAdapter extends BaseAdapter {
    private Context context;
    private List<ChildrenClassBean> childrenClassList;

    public HotMaterialAdapter(List<ChildrenClassBean> childrenClassList, Context context) {
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
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ChildrenClassBean childrenClassBean = childrenClassList.get(position);
        holder.textView.setText(childrenClassBean.getChildrenClassName());
        return convertView;
    }

    class ViewHolder {
        private TextView textView;
    }

}
