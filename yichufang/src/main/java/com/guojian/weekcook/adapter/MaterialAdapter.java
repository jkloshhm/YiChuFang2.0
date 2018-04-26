package com.guojian.weekcook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.bean.MaterialBean;

import java.util.List;

/**
 * Created by guojian on 11/17/16.
 */
public class MaterialAdapter extends BaseAdapter {

    private Context context;
    private List<MaterialBean> materialBeanList;

    public MaterialAdapter(Context context, List<MaterialBean> materialBeanList) {
        this.context = context;
        this.materialBeanList = materialBeanList;
    }

    @Override
    public int getCount() {
        return materialBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return materialBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MaterialViewHolder holder;
        if (convertView == null) {
            holder = new MaterialViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.material_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_item_mname);
            //holder.type = (TextView) convertView.findViewById(R.id.tv_item_type);
            holder.amount = (TextView) convertView.findViewById(R.id.tv_item_amount);
            convertView.setTag(holder);
        }else {
            holder = (MaterialViewHolder) convertView.getTag();
        }
        MaterialBean materialBean  = materialBeanList.get(position);
        holder.name.setText(materialBean.getMname());
        //holder.type.setText(materialBean.getType());
        holder.amount.setText(materialBean.getAmount());
        return convertView;
    }

    class MaterialViewHolder {
        private TextView name;
        private TextView type;
        private TextView amount;
    }
}
