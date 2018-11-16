package com.guojian.weekcook.utils.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * PS: 圆角图片转换器
 *
 * @author jack.guo,  Date on 2018/11/16.
 */
public class RoundTransform extends BitmapTransformation {
    private static float radius = 0f;
    private static final int DEFAULT_RADIUS = 4;


    public RoundTransform(Context context) {
        this(context, DEFAULT_RADIUS);
    }

    public RoundTransform(Context context, int radius) {
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * radius;
    }


    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (null == source) {
            return null;
        }
        // 先从BitmapPool中获取
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        // 如果获取不到，在进行创建
        if (null == result) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        // 使用result去创建画布
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        // 关于setShader的介绍，详情请看http://www.jianshu.com/p/78616a03148a
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }


    public String getId() {
        return getClass().getName();
    }
}