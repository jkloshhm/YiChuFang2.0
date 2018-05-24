package com.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guojian.weekcook.R;
import com.guojian.weekcook.statusbar.StatusBarCompat;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author jkloshhm 2018-01-23 分享页面
 */

public class ShareActivity extends AppCompatActivity {

    private Context mContext;

    public static void startShareActivity(Activity rootActivity, String fileName) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("fileName", fileName);
        intent.putExtras(bundle);
        intent.setClass(rootActivity, ShareActivity.class);
        rootActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(), R.color.black_00000000, null), false);
        mContext = this;
        //mFrameLayout = (FrameLayout) findViewById(R.id.share_activity) ;


        String fileName = getIntent().getExtras().getString("fileName");

        /*if (file){
            //showSharePic();
        } else {
            ShareAppPopupWindow();
        }*/

        ShareAppPopupWindow(fileName);

    }


    private void showShareAPP(String platform, String fileName) {
        OnekeyShare oks = new OnekeyShare();
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setDialogMode(true);
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址

        if (fileName != null) {
            oks.setTitle(getString(R.string.cook_details_shared_name));
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我正在使用《易厨房》APP分享菜谱,下载地址：http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath(fileName);//确保SDcard下面存在此张图片

            oks.setImagePath(fileName);//确保SDcard下面存在此张图片
        } else {
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
        }
        oks.show(mContext);
    }


    private void showSharePic(String platform, String fileName) {
        OnekeyShare oks = new OnekeyShare();
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.cook_details_shared_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在使用《易厨房》APP分享菜谱,下载地址：http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(fileName);//确保SDcard下面存在此张图片

        oks.show(this);
    }


    private void DismissPopupWindow() {
        this.finish();
    }

    private void ShareAppPopupWindow(final String fileName) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图

        final TextView mCancel
                = (TextView) findViewById(R.id.share_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DismissPopupWindow();
            }
        });

        final LinearLayout mWeChat
                = (LinearLayout) findViewById(R.id.wechat);
        mWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(Wechat.NAME);
                showShareAPP(plat.getName(), fileName);
                DismissPopupWindow();
            }
        });

        final LinearLayout mWeChatMoments
                = (LinearLayout) findViewById(R.id.wechatmoments);
        mWeChatMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(WechatMoments.NAME);
                showShareAPP(plat.getName(), fileName);
                DismissPopupWindow();
            }
        });

        final LinearLayout mQQ
                = (LinearLayout) findViewById(R.id.qq);
        mQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                showShareAPP(plat.getName(), fileName);
                DismissPopupWindow();
            }
        });


        final LinearLayout mSinaWeibo
                = (LinearLayout) findViewById(R.id.sinaweibo);
        mSinaWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                showShareAPP(plat.getName(), fileName);
                DismissPopupWindow();
            }
        });
    }

}
