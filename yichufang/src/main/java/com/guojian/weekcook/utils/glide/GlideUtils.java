package com.guojian.weekcook.utils.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.guojian.weekcook.R;

/**
 * 图片加载工具类
 *
 * @author jkloshhm Created on 2018/1/26.
 */

public class GlideUtils {

    public static void loadImage(final Context context, final String imageUrl, final ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.common_pic_default_fail)
                .placeholder(R.drawable.common_pic_default);
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    public static void loadRoundImage(final Context context, final String imageUrl, final ImageView imageView) {

        RequestOptions options = new RequestOptions()
                //图片加载失败后，显示的图片
                .error(R.mipmap.common_pic_default_fail)
                //图片加载出来前，显示的图片
                .placeholder(R.drawable.common_pic_default)
                .transform(new RoundTransform(context))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

                //圆形
                // .transforms(new CircleTransform(mContext,2, Color.DKGRAY))//外圈宽度，外圈颜色
                //黑白
                //  .transforms(new BlackWhiteTransformation());
                //高斯模糊 范围在 0 -- 25 越大模糊程度越高
                // .transforms(new BlurTransformation(mContext, 25)); // (0 < r <= 25)
                //可以使用多种
                // .transforms(new BlurTransformation(mContext, 25),new CircleTransform(mContext,2, Color.DKGRAY));

        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

}
