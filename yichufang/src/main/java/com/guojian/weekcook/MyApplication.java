package com.guojian.weekcook;

import android.app.Application;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.Bugly;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by guojian on 11/16/16.
 * @author jack.guo
 *
 * @ 自定义的MyApplication类
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
