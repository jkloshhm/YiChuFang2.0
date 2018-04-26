package com.guojian.weekcook.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guojian.weekcook.R;

/**
 * Created by jack.guo on 2018/1/26.
 * @author jack.guo
 *
 * 图片加载工具类
 *
 */

public class ImageLoaderWithGlide {

    public static void loadImage(final Context context, final String imageUrl, final ImageView imageView) {

        Glide.with(context)
                .load(imageUrl)
                //图片加载出来前，显示的图片
                .placeholder(R.mipmap.common_pic_default)
                //图片加载失败后，显示的图片
                .error(R.drawable.common_pic_default)
                .into(imageView);
    }


}
