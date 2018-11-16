package com.guojian.weekcook.adapter;

import android.content.Context;
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
 * @author Created by guojian on 11/17/16. 菜谱详情页面制作步骤ProcessAdapter
 */
public class ProcessAdapter extends BaseAdapter {

    private Context mContext;
    private List<CookListBean.ResultBean.ListBean.ProcessBean> processBeanList;

    public ProcessAdapter(Context mContext, List<CookListBean.ResultBean.ListBean.ProcessBean> processBeanList) {
        this.mContext = mContext;
        this.processBeanList = processBeanList;
    }

    @Override
    public int getCount() {
        return processBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return processBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProcessViewHolder holder;
        if (convertView == null) {
            holder = new ProcessViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.process_list_item, null);
            holder.pContent = (TextView) convertView.findViewById(R.id.tv_item_process_content);
            holder.pImageView = (ImageView) convertView.findViewById(R.id.iv_item_process_img);
            holder.pStep = (TextView) convertView.findViewById(R.id.tv_steps);
            convertView.setTag(holder);
        } else {
            holder = (ProcessViewHolder) convertView.getTag();
        }

        CookListBean.ResultBean.ListBean.ProcessBean processBean = processBeanList.get(position);
        String steps = (position+1)+"";
        holder.pStep.setText(steps);
        String processString = processBean.getPcontent().replace("<br />","");
        holder.pContent.setText(processString);
        GlideUtils.loadImage(mContext,processBean.getPic(),holder.pImageView);

        return convertView;
    }

    private class ProcessViewHolder {
        private TextView pContent,pStep;
        private ImageView pImageView;
    }

}
