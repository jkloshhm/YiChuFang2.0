package com.guojian.weekcook.statusbar;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * Created by jack.guo on 2018/1/15.
 */

public class LightStatusBarCompat {

    interface ILightStatusBar {
        void setLightStatusBar(Window window, boolean lightStatusBar);
    }

    private static final ILightStatusBar IMPL;

    static {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && MIUILightStatusBarImpl.isMe()) {
            //小米系统设置开启状态栏暗色文字
            IMPL = new MIUILightStatusBarImpl();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //android 6.0 及以上系统设置开启状态栏暗色文字
            IMPL = new MLightStatusBarImpl();
        }else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && MeizuLightStatusBarImpl.isMe()){
            //flyme系统设置开启状态栏暗色文字
            IMPL = new MeizuLightStatusBarImpl();
        }else  {
            IMPL = new ILightStatusBar() {
                @Override
                public void setLightStatusBar(Window window, boolean lightStatusBar) {
                }
            };
        }
    }

    public static void setLightStatusBar(Window window, boolean lightStatusBar) {
        IMPL.setLightStatusBar(window, lightStatusBar);
    }

    private static class MLightStatusBarImpl implements ILightStatusBar {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void setLightStatusBar(Window window, boolean lightStatusBar) {
            // 设置浅色状态栏时的界面显示
            View decor = window.getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (lightStatusBar) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);

        }
    }

}
