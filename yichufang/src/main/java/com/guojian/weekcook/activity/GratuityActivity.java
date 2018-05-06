package com.guojian.weekcook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

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
    private Button mSaveImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratuity);
        mBackButton = findViewById(R.id.btn_back_gratuity);
        mAlipayButton = findViewById(R.id.btn_alipay);
        mWeChatButton = findViewById(R.id.btn_wechat);
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
            case R.id.btn_alipay:
                //支付宝
                break;
            case R.id.btn_wechat:
                //微信
                break;
            case R.id.btn_save_image:
                //保存照片
                break;
            default:
                break;
        }
    }
}
