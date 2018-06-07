package com.guojian.weekcook.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guojian.weekcook.R;
import com.guojian.weekcook.utils.GetBitmapFromSdCardUtil;

/**
 * @author jkloshhm 2017-11-23  我的头像大图页面
 */
public class HeadImageShowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_head_image_shower);
        LinearLayout mMyHeadImage = (LinearLayout) findViewById(R.id.ll_my_header_image_shower);
        ImageView imageView = (ImageView) findViewById(R.id.iv_my_header_image_shower);
        if (mMyHeadImage != null) {
            mMyHeadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (GetBitmapFromSdCardUtil.hasSdcard()) {
            //sd路径
            String path = Environment.getExternalStorageDirectory() + "/YiChuFang/myHeadImg/";
            Bitmap bt = GetBitmapFromSdCardUtil.getBitmap(path + "head.jpg");
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);
                if (imageView != null) {
                    imageView.setImageDrawable(drawable);
                }
                if (bt.isRecycled()) {
                    bt.recycle();
                }
            }
        }
    }


}
