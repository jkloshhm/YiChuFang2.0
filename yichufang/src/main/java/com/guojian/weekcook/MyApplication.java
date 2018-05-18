package com.guojian.weekcook;

import com.mob.MobSDK;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * @author Created by jkloshhm on 11/16/16.
 * <p>
 * MyApplication: 自定义的MyApplication类
 */
public class MyApplication extends TinkerApplication {


    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.guojian.weekcook.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
    }
}
