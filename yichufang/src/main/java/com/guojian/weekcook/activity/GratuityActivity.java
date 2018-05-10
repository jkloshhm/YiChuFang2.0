package com.guojian.weekcook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.guojian.weekcook.R;

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
                break;
            case R.id.btn_alipay:
                //支付宝
                mAlipayQr.setVisibility(View.VISIBLE);
                mWeChatQr.setVisibility(View.GONE);
                mAlipayQrTips.setVisibility(View.VISIBLE);
                mWeChatQrTips.setVisibility(View.GONE);
                break;
            case R.id.btn_save_image:
                //保存照片
                break;
            default:
                break;
        }
    }
}
