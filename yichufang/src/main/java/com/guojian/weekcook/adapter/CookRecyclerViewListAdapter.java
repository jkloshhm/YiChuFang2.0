package com.guojian.weekcook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.utils.glide.GlideUtils;

import java.util.List;

/**
 * PS: Activity
 *
 * @author jack.guo,  Date on 2018/6/12.
 */
public class CookRecyclerViewListAdapter extends RecyclerView.Adapter<CookRecyclerViewListAdapter.MyViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<CookListBean.ResultBean.ListBean> cookBeanList;

    public CookRecyclerViewListAdapter(Context mContext, List<CookListBean.ResultBean.ListBean> cookBeanList) {
        this.mContext = mContext;
        this.cookBeanList = cookBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new MyViewHolder(View.inflate(mContext, R.layout.cook_recycle_view_list_item, null));//使用这句代码不能match_parent
        View view = LayoutInflater.from(mContext).inflate(R.layout.cook_recycle_view_list_item, parent, false);
        //view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
        GlideUtils.loadRoundImage(mContext, cookBean.getPic(), holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cookBeanList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mCookName;
        private TextView mCookMaterial;
        private TextView mCookingTime;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCookName = itemView.findViewById(R.id.tv_item_cook_name);
            mCookMaterial = itemView.findViewById(R.id.tv_item_cook_material);
            mCookingTime = itemView.findViewById(R.id.tv_item_cook_time);
            imageView = itemView.findViewById(R.id.iv_item_cook_pic);
        }
    }

    public OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
