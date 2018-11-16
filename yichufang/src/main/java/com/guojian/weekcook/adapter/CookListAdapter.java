package com.guojian.weekcook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.utils.glide.GlideUtils;

import java.util.List;

/**
 * @author Created by guojian on 11/15/16.  菜谱列表页面
 */
public class CookListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CookListBean.ResultBean.ListBean> cookBeanList;

    public CookListAdapter(Context mContext, List<CookListBean.ResultBean.ListBean> cookBeanList) {
        this.mContext = mContext;
        this.cookBeanList = cookBeanList;
    }

    @Override
    public int getCount() {
        return cookBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return cookBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cook_list_item, null);
            holder.mCookName = (TextView) convertView.findViewById(R.id.tv_item_cook_name);
            holder.mCookMaterial = (TextView) convertView.findViewById(R.id.tv_item_cook_material);
            holder.mCookingTime = (TextView) convertView.findViewById(R.id.tv_item_cook_time);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_cook_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CookListBean.ResultBean.ListBean cookBean = cookBeanList.get(position);
        Log.i("guojian", "CookListAdapter-->> cookBean.toString();====" + cookBean.toString());
        holder.mCookName.setText(cookBean.getName());
        List<CookListBean.ResultBean.ListBean.MaterialBean> materialBeanList = cookBean.getMaterial();
        StringBuilder material = new StringBuilder("");
        for (int i = materialBeanList.size() - 1; i >= 0; i--) {
            CookListBean.ResultBean.ListBean.MaterialBean materialBean = materialBeanList.get(i);
            String materialString = materialBean.getMname() + ", ";
            material.append(materialString);
        }
        holder.mCookMaterial.setText(material);
        String mCookingTimeString = "烹饪时间: " + cookBean.getCookingtime();
        holder.mCookingTime.setText(mCookingTimeString);
        GlideUtils.loadImage(mContext, cookBean.getPic(), holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        private TextView mCookName;
        private TextView mCookMaterial;
        private TextView mCookingTime;
        private ImageView imageView;
    }
}
