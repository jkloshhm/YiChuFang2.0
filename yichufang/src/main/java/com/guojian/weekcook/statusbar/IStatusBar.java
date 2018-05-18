package com.guojian.weekcook.statusbar;

import android.app.Activity;
import android.view.Window;

/**
 * 状态栏 根据版本不同使用不同的实现方式
 *
 * @author jack.guo on 2018/1/15.
 * @see StatusBarLollipopImpl
 * @see StatusBarMImpl
 */

public interface IStatusBar {
    /**
     * 设置状态栏颜色
     *
     * @param window the window
     * @param color  status bar color
     */
    void setStatusBarColor(Window window, int color);

    /**
     * 设置状态栏全透明，顶部布局会顶上去
     *
     * @param activity                activity
     * @param hideStatusBarBackground 是否隐藏状态栏背景
     * @return 状态栏是否透明
     */
    boolean translucentStatusBar(Activity activity, boolean hideStatusBarBackground);
}
