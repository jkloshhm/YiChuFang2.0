package com.guojian.weekcook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.guojian.weekcook.ProcessViewPager;
import com.guojian.weekcook.R;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.bean.StepViewPagerBean;

import java.util.List;

/**
 * @author jkloshhm 2017-01-23  菜谱制作步骤大图页面
 */

public class ProcessLargeImgActivity extends AppCompatActivity {

    private ProcessViewPager mProcessViewPager;
    private StepViewPagerBean mStepViewPagerBeen;
    private List<CookListBean.ResultBean.ListBean.ProcessBean> mProcessBeanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_large_img);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mStepViewPagerBeen = (StepViewPagerBean) getIntent().getSerializableExtra("stepViewPagerBean");
        mProcessBeanList = mStepViewPagerBeen.getmProcessBeanList();
        int position = Integer.parseInt(mStepViewPagerBeen.getPosition());
        mProcessViewPager = (ProcessViewPager) findViewById(R.id.pv_process_viewpager_layout);
        if (null != mProcessViewPager && null != mStepViewPagerBeen ){
            mProcessViewPager.initialize(mProcessBeanList).build(position);

        }

    }
}
