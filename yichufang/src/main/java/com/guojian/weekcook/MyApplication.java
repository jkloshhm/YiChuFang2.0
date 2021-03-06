package com.guojian.weekcook;

import android.content.Context;

import com.mob.MobSDK;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * MyApplication: 自定义的MyApplication类
 *
 * @author jkloshhm on 11/16/16.
 */
public class MyApplication extends TinkerApplication {

    private static Context context;

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.guojian.weekcook.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
