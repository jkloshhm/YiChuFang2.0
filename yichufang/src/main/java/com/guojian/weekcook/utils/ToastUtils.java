package com.guojian.weekcook.utils;

import android.content.Context;
import android.widget.Toast;

import com.guojian.weekcook.MyApplication;

/**
 * PS: Activity
 *
 * @author jack.guo,  Date on 2018/6/1.
 */
public class ToastUtils {
    public static void showLongToast(String toast) {
        Toast.makeText(MyApplication.getContext(), toast, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String toast) {
        Toast.makeText(MyApplication.getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
