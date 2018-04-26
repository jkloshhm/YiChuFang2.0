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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.activity.CollectionActivity;
import com.guojian.weekcook.activity.GratuityActivity;
import com.guojian.weekcook.activity.LoginActivity;
import com.guojian.weekcook.activity.MyInformationActivity;
import com.guojian.weekcook.activity.MySettingsActivity;
import com.guojian.weekcook.activity.ShareActivity;
import com.guojian.weekcook.utils.GetBitmapFromSdCardUtil;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * A simple {@link Fragment} subclass.
 * @author jack.guo
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView mMyname;
    private String TAG = "guojianMe_MeFragment";
    private Context mContext;
    private LinearLayout mFragmentMeLinearLayout;
    private ImageView mMyTitleImg;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        mFragmentMeLinearLayout =  (LinearLayout) view.findViewById(R.id.ll_fragment_me);
        mMyTitleImg = (ImageView) view.findViewById(R.id.iv_my_title_img);
        mMyname = (TextView) view.findViewById(R.id.tv_me_name);
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
        //getSharedPreferences("cooking", MODE_PRIVATE);
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
            String path = Environment.getExternalStorageDirectory() + "/YiChuFang/myHeadImg/";//sd路径
            Bitmap bt = GetBitmapFromSdCardUtil.getBitmap(path + "head.jpg");
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);
                mMyTitleImg.setImageDrawable(drawable);
                if (bt.isRecycled()){
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
                ShareActivity.startShareActivity(getActivity(),null);
                //ShareAppPopupWindow();
                //mPopupWindowShareApp.showAtLocation(mFragmentMeLinearLayout, Gravity.BOTTOM, 0, -500);
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


    private void showShareAPP(String platform) {
        OnekeyShare oks = new OnekeyShare();
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setDialogMode(true);
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
        oks.setTitle("《易厨房》--推荐下载");
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook");
        oks.setText("一款史上最简洁易用的菜谱类APP，快来和我一起使用吧。");
        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
        //oks.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg");
        oks.setImageUrl("http://api.jisuapi.com//recipe//upload//20160719//180351_56079.jpg");
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook"); //微信不绕过审核分享链接
        //oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
        oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl("http://mob.com");//QZone分享参数
        oks.setVenueName("ShareSDK");
        oks.setVenueDescription("This is a beautiful place!");
        oks.show(mContext);
    }


    private PopupWindow mPopupWindowShareApp;

    private void DismissPopupWindow(){
        if (mPopupWindowShareApp != null && mPopupWindowShareApp.isShowing()) {
            mPopupWindowShareApp.dismiss();
            mPopupWindowShareApp = null;
            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
            params.alpha = 1f;
            getActivity().getWindow().setAttributes(params);
        }
    }
    private void ShareAppPopupWindow() {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        final View popupWindow_sharePic = getActivity().getLayoutInflater()
                .inflate(R.layout.share_pop_window_me_fragment, null, false);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow_sharePic.setFocusable(true); // 这个很重要
        popupWindow_sharePic.setFocusableInTouchMode(true);
        mPopupWindowShareApp = new PopupWindow(popupWindow_sharePic, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        mPopupWindowShareApp.setFocusable(true);
        // 重写onKeyListener
        popupWindow_sharePic.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    DismissPopupWindow();
                }
                return false;
            }
        });
        mPopupWindowShareApp.setAnimationStyle(R.style.AnimationFade_Settings);
        //设置actiivity透明度
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        // 点击其他地方消失
        popupWindow_sharePic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DismissPopupWindow();
                return false;
            }
        });

        final TextView mCancel
                = (TextView) popupWindow_sharePic.findViewById(R.id.share_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DismissPopupWindow();
            }
        });

        final LinearLayout mWeChat
                = (LinearLayout) popupWindow_sharePic.findViewById(R.id.wechat);
        mWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(Wechat.NAME);
                showShareAPP(plat.getName());
                DismissPopupWindow();
            }
        });

        final LinearLayout mWeChatMoments
                = (LinearLayout) popupWindow_sharePic.findViewById(R.id.wechatmoments);
        mWeChatMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(WechatMoments.NAME);
                showShareAPP(plat.getName());
                DismissPopupWindow();
            }
        });

        final LinearLayout mQQ
                = (LinearLayout) popupWindow_sharePic.findViewById(R.id.qq);
        mQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                showShareAPP(plat.getName());
                DismissPopupWindow();
            }
        });
    }

}
