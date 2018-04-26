package com.guojian.weekcook.statusbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jack.guo on 2018/1/15.
 * @author jack.guo
 */

public class StatusBarCompat {

    private static final IStatusBar IMPL;

    static {
        //根据版本做判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMPL = new StatusBarMImpl();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && (MeizuLightStatusBarImpl.isMe() || MIUILightStatusBarImpl.isMe()) ) {
            IMPL = new StatusBarLollipopImpl();
        } else {
            //低于5.0没有沉浸式标题栏
            IMPL = new IStatusBar() {
                @Override
                public void setStatusBarColor(Window window, @ColorInt int color) {
                }

                @Override
                public boolean translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
                    return false;
                }
            };
        }
    }

    /**
     * Set system status bar color.
     *
     * @param color          status bar color
     * @param lightStatusBar if the status bar color is light. Only effective in some devices.
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color, boolean lightStatusBar) {
        setStatusBarColor(activity.getWindow(), color, lightStatusBar);
    }

    /**
     * Set the system status bar color
     *
     * @param window         the window
     * @param color          status bar color
     * @param lightStatusBar if the status bar color is light. Only effective in some devices.
     */
    private static void setStatusBarColor(Window window, @ColorInt int color, boolean lightStatusBar) {
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) > 0) {
            return;
        }
        IMPL.setStatusBarColor(window, color);
        LightStatusBarCompat.setLightStatusBar(window, lightStatusBar);
    }

    //判断是否展示沉浸式标题栏
    public static boolean isShowStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && (MeizuLightStatusBarImpl.isMe() || MIUILightStatusBarImpl.isMe()) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * change to full screen mode
     * @param hideStatusBarBackground hide status bar alpha Background when SDK > 21, true if hide it
     * @return ifshowtranslucentStatusBar 是否开启沉浸式标题栏
     */
    public static boolean translucentStatusBar(@NonNull Activity activity, boolean hideStatusBarBackground, boolean lightStatusBar) {
        Window window = activity.getWindow();
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) > 0) {
            return false;
        }
        boolean ifshowtranslucentStatusBar = IMPL.translucentStatusBar(activity, hideStatusBarBackground);
        LightStatusBarCompat.setLightStatusBar(window, lightStatusBar);
        return ifshowtranslucentStatusBar;
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

}
