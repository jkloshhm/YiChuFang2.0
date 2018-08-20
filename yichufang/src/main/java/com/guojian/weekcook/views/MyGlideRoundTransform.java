package com.guojian.weekcook.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Glide 圆角图片转换类
 * Created by k.huang on 2017/3/9.
 */
public class MyGlideRoundTransform extends BitmapTransformation {
    private static float radius = 0f;
    private static final int DEFAULT_RADIUS = 4;


    public MyGlideRoundTransform(Context context) {
        this(context,DEFAULT_RADIUS);
    }

    public MyGlideRoundTransform(Context context , int radius) {
        super(context);
        this.radius = Resources.getSystem().getDisplayMetrics().density * radius;
    }



    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (null == source){
            return null;
        }
        // 先从BitmapPool中获取
        Bitmap result = pool.get(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        // 如果获取不到，在进行创建
        if (null == result){
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        // 使用result去创建画布
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        // 关于setShader的介绍，详情请看http://www.jianshu.com/p/78616a03148a
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f,0f,source.getWidth(),source.getHeight());
        canvas.drawRoundRect(rectF,radius,radius,paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}