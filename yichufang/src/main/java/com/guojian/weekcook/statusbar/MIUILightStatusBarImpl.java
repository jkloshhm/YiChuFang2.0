package com.guojian.weekcook.statusbar;

import android.os.Environment;
import android.view.View;
import android.view.Window;

import com.guojian.weekcook.statusbar.LightStatusBarCompat.ILightStatusBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by jack.guo on 2018/1/15.
 * @author jack.guo
 */

public class MIUILightStatusBarImpl implements ILightStatusBar {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static String MIUIname;

    static boolean isMe() {
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            Properties prop = new Properties();
            prop.load(is);
            //获取MIUI版本号
            MIUIname = prop.getProperty(KEY_MIUI_VERSION_NAME);
            return prop.getProperty(KEY_MIUI_VERSION_CODE) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE) != null;
        } catch (final IOException e) {
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore all exception
                }
            }
        }
    }

    @Override
    public void setLightStatusBar(Window window, boolean lightStatusBar) {
        if (window != null) {

            int MIUIcode = Integer.parseInt(MIUIname.substring(1,MIUIname.length()));
            if(MIUIcode<9){
                Class clazz = window.getClass();
                try {
                    int darkModeFlag = 0;
                    Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                    darkModeFlag = field.getInt(layoutParams);
                    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                    if (lightStatusBar) {
                        //状态栏透明且黑色字体
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                    } else {
                        //清除黑色字体
                        extraFlagField.invoke(window, 0, darkModeFlag);
                    }
                } catch (Exception e) {

                }
            }else{
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


}
