package com.guojian.weekcook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.guojian.weekcook.R;

/**
 * 打赏Activity
 *
 * @author jack.guo 2018-02-12
 */
public class GratuityActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratuity);
        mBackButton = findViewById(R.id.bn_back_gratuity);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bn_back_gratuity:
                finish();
                break;
            default:
                break;
        }
    }
}
