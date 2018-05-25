package com.guojian.weekcook.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.guojian.weekcook.R;


/**
 * Created by guojian on 10/26/16.
 */
public class ConnectionUtil {
    /**
     * 检测是否已经连接网络。
     *
     * @param context 传过来的上下文
     * @return 当且仅当连上网络时返回true, 否则返回false。
     */
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {
            //执行相关操作
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开设置网络界面
     */
    public static void setNetworkMethod(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示")
                .setIcon(R.mipmap.tishi)
                .setMessage("网络连接不可用,是否进行设置?")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        //判断手机系统的版本  即API大于10 就是3.0或以上版本
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            intent = new Intent();
                            ComponentName component = new ComponentName("com.android.settings",
                                    "com.android.settings.Settings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName("com.android.settings",
                                    "com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        context.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}
