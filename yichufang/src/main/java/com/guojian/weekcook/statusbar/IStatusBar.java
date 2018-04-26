package com.guojian.weekcook.statusbar;

import android.app.Activity;
import android.view.Window;

/**
 * Created by jack.guo on 2018/1/15.
 * 状态栏 根据版本不同使用不同的实现方式
 * @see StatusBarLollipopImpl
 * @see StatusBarMImpl
 */

public interface IStatusBar {
    //设置状态栏颜色
    void setStatusBarColor(Window window, int color);
    //设置状态栏全透明，顶部布局会顶上去
    boolean translucentStatusBar(Activity activity, boolean hideStatusBarBackground);
}
