package com.guojian.weekcook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    private Button mSaveImageButton;
    private RadioGroup mRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratuity);
        mBackButton = findViewById(R.id.btn_back_gratuity);
        mAlipayButton = findViewById(R.id.btn_alipay);
        mWeChatButton = findViewById(R.id.btn_wechat);
        mAlipayQr = findViewById(R.id.iv_alipay_qr);
        mWeChatQr = findViewById(R.id.iv_wechat_qr);
        mSaveImageButton = findViewById(R.id.btn_save_image);
        mRadioGroup = findViewById(R.id.radioGroup_qr_btn);
        mBackButton.setOnClickListener(this);
        mAlipayButton.setOnClickListener(this);
        mWeChatButton.setOnClickListener(this);
        mSaveImageButton.setOnClickListener(this);
        /*mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_alipay:
                        //支付宝
                        //mAlipayButton.setChecked(true);
                        //mWeChatButton.setChecked(false);
                        mAlipayQr.setVisibility(View.VISIBLE);
                        mWeChatQr.setVisibility(View.GONE);
                        break;
                    case R.id.btn_wechat:
                        //微信
                        //mAlipayButton.setChecked(false);
                        //mWeChatButton.setChecked(true);
                        mAlipayQr.setVisibility(View.GONE);
                        mWeChatQr.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_gratuity:
                finish();
                break;
            case R.id.btn_alipay:
                //支付宝
                //mAlipayButton.setChecked(true);
                //mWeChatButton.setChecked(false);
                mAlipayQr.setVisibility(View.VISIBLE);
                mWeChatQr.setVisibility(View.GONE);
                break;
            case R.id.btn_wechat:
                //微信
                //mAlipayButton.setChecked(false);
                //mWeChatButton.setChecked(true);
                mAlipayQr.setVisibility(View.GONE);
                mWeChatQr.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_save_image:
                //保存照片
                break;
            default:
                break;
        }
    }
}
