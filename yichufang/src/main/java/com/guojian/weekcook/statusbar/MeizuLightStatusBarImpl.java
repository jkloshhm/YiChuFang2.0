package com.guojian.weekcook.statusbar;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.guojian.weekcook.statusbar.LightStatusBarCompat.ILightStatusBar;

import java.lang.reflect.Field;

/**
 * Created by jack.guo on 2018/1/15.
 * @author jack.guo
 */

public class MeizuLightStatusBarImpl implements ILightStatusBar {

    static boolean isMe() {
        return Build.DISPLAY.startsWith("Flyme");
    }

    @Override
    public void setLightStatusBar(Window window, boolean lightStatusBar) {
        WindowManager.LayoutParams params = window.getAttributes();
        try {
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(params);
            if (lightStatusBar) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(params, value);
            window.setAttributes(params);
            darkFlag.setAccessible(false);
            meizuFlags.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
