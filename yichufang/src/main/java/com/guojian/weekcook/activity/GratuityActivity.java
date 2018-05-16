package com.guojian.weekcook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guojian.weekcook.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 打赏Activity
 *
 * @author jack.guo 2018-02-12
 */
public class GratuityActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackButton;
    private RadioButton mAlipayButton;
    private RadioButton mWeChatButton;
    private ImageView mAlipayQr;
    private ImageView mWeChatQr;
    private TextView mWeChatQrTips;
    private TextView mAlipayQrTips;
    private Button mSaveImageButton;
    private boolean mIsWechat = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratuity);
        mBackButton = findViewById(R.id.btn_back_gratuity);
        mAlipayButton = findViewById(R.id.btn_alipay);
        mWeChatButton = findViewById(R.id.btn_wechat);
        mAlipayQr = findViewById(R.id.iv_alipay_qr);
        mWeChatQr = findViewById(R.id.iv_wechat_qr);
        mWeChatQrTips = findViewById(R.id.tv_wechat_qr_tips);
        mAlipayQrTips = findViewById(R.id.tv_alipay_qr_tips);
        mSaveImageButton = findViewById(R.id.btn_save_image);
        mBackButton.setOnClickListener(this);
        mAlipayButton.setOnClickListener(this);
        mWeChatButton.setOnClickListener(this);
        mSaveImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_gratuity:
                finish();
                break;
            case R.id.btn_wechat:
                //微信
                mAlipayQr.setVisibility(View.GONE);
                mWeChatQr.setVisibility(View.VISIBLE);
                mAlipayQrTips.setVisibility(View.GONE);
                mWeChatQrTips.setVisibility(View.VISIBLE);
                mIsWechat = true;
                break;
            case R.id.btn_alipay:
                //支付宝
                mAlipayQr.setVisibility(View.VISIBLE);
                mWeChatQr.setVisibility(View.GONE);
                mAlipayQrTips.setVisibility(View.VISIBLE);
                mWeChatQrTips.setVisibility(View.GONE);
                mIsWechat = false;
                break;
            case R.id.btn_save_image:
                //保存二维码图片
                if (mIsWechat) {
                    saveImageToAlbum(R.mipmap.qr_wechat, "微信二维码保存成功~");
                } else {
                    saveImageToAlbum(R.mipmap.qr_alipay, "支付宝二维码保存成功~");
                }
                break;
            default:
                break;
        }
    }

    private void saveImageToAlbum(int res, String toast) {
        //Bitmap mBitmap = ((BitmapDrawable) getResources().getDrawable(res)).getBitmap();
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), res);

        String path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera";
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String fileName = "YiChuFang" + new SimpleDateFormat("_yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())) + ".png";
        File file = new File(path, fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        } finally {
            if (mBitmap != null) {
                mBitmap.recycle();
            }
        }
    }
}
