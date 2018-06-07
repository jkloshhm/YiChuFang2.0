package com.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.activity.CollectionActivity;
import com.guojian.weekcook.activity.GratuityActivity;
import com.guojian.weekcook.activity.LoginActivity;
import com.guojian.weekcook.activity.MyInformationActivity;
import com.guojian.weekcook.activity.MySettingsActivity;
import com.guojian.weekcook.activity.ShareActivity;
import com.guojian.weekcook.utils.GetBitmapFromSdCardUtil;

/**
 * @author Created by jkloshhm on 2017/6/24. 我的页面
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView mMyname;
    private Context mContext;
    private ImageView mMyTitleImg;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        mMyTitleImg =  view.findViewById(R.id.iv_my_title_img);
        mMyname =  view.findViewById(R.id.tv_me_name);
        LinearLayout mMyCollectionLinearLayout = (LinearLayout) view
                .findViewById(R.id.ll_my_collection_me);
        LinearLayout mMyInformationLinearLayout = (LinearLayout) view
                .findViewById(R.id.ll_my_information_me);
        LinearLayout mMySettingsLinearLayout = (LinearLayout) view
                .findViewById(R.id.ll_my_settings_me);
        LinearLayout mMyShareAppLinearLayout = (LinearLayout) view
                .findViewById(R.id.ll_share_app);
        LinearLayout mLoginLayout = (LinearLayout) view
                .findViewById(R.id.ll_login);
        mMyCollectionLinearLayout.setOnClickListener(this);
        mMyInformationLinearLayout.setOnClickListener(this);
        mMySettingsLinearLayout.setOnClickListener(this);
        mMyShareAppLinearLayout.setOnClickListener(this);
        mLoginLayout.setOnClickListener(this);
        view.findViewById(R.id.ll_gratuity).setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("cooking",
                Context.MODE_PRIVATE);
        mMyname.setText(mSharedPreferences.getString("name", "Mary"));
        if (GetBitmapFromSdCardUtil.hasSdcard()) {
            //sd路径
            String path = Environment.getExternalStorageDirectory() + "/YiChuFang/myHeadImg/";
            Bitmap bt = GetBitmapFromSdCardUtil.getBitmap(path + "head.jpg");
            if (bt != null) {
                Drawable drawable = new BitmapDrawable(bt);
                mMyTitleImg.setImageDrawable(drawable);
                if (bt.isRecycled()) {
                    bt.recycle();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.ll_my_collection_me:
                mIntent = new Intent(mContext, CollectionActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_my_information_me:
                mIntent = new Intent(mContext, MyInformationActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_my_settings_me:
                mIntent = new Intent(mContext, MySettingsActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_share_app:
                ShareActivity.startShareActivity(getActivity(), null);
                break;
            case R.id.ll_login:
                mIntent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_gratuity:
                mIntent = new Intent(mContext, GratuityActivity.class);
                mContext.startActivity(mIntent);
                break;
            default:
                break;
        }
    }


}
