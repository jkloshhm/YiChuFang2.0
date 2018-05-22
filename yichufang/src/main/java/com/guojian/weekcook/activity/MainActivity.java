package com.guojian.weekcook.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.guojian.weekcook.R;
import com.guojian.weekcook.adapter.MyFragmentPagerAdapter;
import com.guojian.weekcook.fragment.ClassFragment;
import com.guojian.weekcook.fragment.HomeFragment;
import com.guojian.weekcook.fragment.MeFragment;
import com.guojian.weekcook.statusbar.StatusBarCompat;
import com.guojian.weekcook.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    private ViewPager viewPager;
    private RadioButton rbSearch, rbClass, rbDiscovery, rbMe;
    private long exitTime = 0;
    //退出时的时间
    private long mExitTime;
    private final static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(), R.color.white, null), true);
        setContentView(R.layout.activity_main);
        boolean hasPerm = ContextCompat.checkSelfPermission(MainActivity.this, "Manifest.permission.WRITE_EXTERNAL_STORAGE") ==
                PackageManager.PERMISSION_GRANTED;
        if (!hasPerm) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },
                    BAIDU_READ_PHONE_STATE);
        }


        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!rbSearch.isChecked()) {
                viewPager.setCurrentItem(0, false);
                return false;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //viewPager.setCurrentItem(0);
            exitApp();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    private void initView() {
        /**
         * RadioGroup部分
         */
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbSearch = (RadioButton) findViewById(R.id.rb_search);
        rbClass = (RadioButton) findViewById(R.id.rb_class);
        //rbDiscovery = (RadioButton) findViewById(R.id.rb_discovery);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        /*for(int i = 0;i<3;i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (i == 0) {
                rb.setChecked(true);
            }
            int width = DensityUtils.dp2px(MainActivity.this, 30);
            //drawables[i] = rb.getCompoundDrawables();
            d = rb.getCompoundDrawables();
            d[i].setBounds(0, 0, width, width);
            //rb.setCompoundDrawables(null, d[i], null, null);
        }*/
        /*int width = DensityUtils.dp2px(MainActivity.this, 30);
        d = rbMe.getCompoundDrawables();
        d[0].setBounds(0, 0, width, width);*/
        //定义底部标签图片大小
        /*Rect rect = new Rect();
        rect.set(0,0,92,92);
        //注意 xml没有设置 drawableTop 的图片话  drawableT 为null 的情况
        Drawable drawableT = rbMe.getCompoundDrawables()[1];
        drawableT.setBounds(rect);// 大小和位置控制
        rbMe.setCompoundDrawables(null,drawableT,null,null);*/

        int width = DensityUtils.dp2px(MainActivity.this, 22);
        Log.i(TAG, "width===" + width);
        Rect rect = new Rect();
        // 这里分别是left top right bottom,代表距离父view的距离. 长宽是right-left, bottom-top
        rect.set(0, 0, width, width);
        //注意 xml没有设置 drawableTop 的图片话  drawableT 为null 的情况
        // getCompoundDrawables()得到一个数组  0 1 2 3 对应 left top right bottom 方向的drawable
        Drawable drawableHome = rbSearch.getCompoundDrawables()[1];
        drawableHome.setBounds(rect);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbSearch.setCompoundDrawables(null, drawableHome, null, null);//只放上面

        Drawable drawableClass = rbClass.getCompoundDrawables()[1];
        drawableClass.setBounds(rect);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbClass.setCompoundDrawables(null, drawableClass, null, null);//只放上面

        Drawable drawableMe = rbMe.getCompoundDrawables()[1];
        drawableMe.setBounds(rect);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbMe.setCompoundDrawables(null, drawableMe, null, null);//只放上面

        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_search:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_class:
                        viewPager.setCurrentItem(1, false);
                        break;
                    /*case R.id.rb_discovery:
                        viewPager.setCurrentItem(2, true);
                        break;*/
                    case R.id.rb_me:
                        viewPager.setCurrentItem(2, false);
                        break;
                    default:
                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        HomeFragment weHomeFragment = new HomeFragment();
        ClassFragment classFragment = new ClassFragment();
        //DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        MeFragment meFragment = new MeFragment();
        List<Fragment> alFragment = new ArrayList<Fragment>();
        alFragment.add(weHomeFragment);
        alFragment.add(classFragment);
        //alFragment.add(discoveryFragment);
        alFragment.add(meFragment);
        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //radioGroup.check(R.id.rb_chat);
                        rbSearch.setChecked(true);
                        break;
                    case 1:
                        //radioGroup.check(R.id.rb_contacts);
                        rbClass.setChecked(true);
                        break;
                    /*case 2:
                        //radioGroup.check(R.id.rb_discovery);
                        rbDiscovery.setChecked(true);
                        break;*/
                    case 2:
                        //radioGroup.check(R.id.rb_me);
                        rbMe.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private static final int BAIDU_READ_PHONE_STATE = 1000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BAIDU_READ_PHONE_STATE) {
            // 首次进入提示定位
            //Toast.makeText(MainActivity.this,"存储权限授权成功~",Toast.LENGTH_SHORT).show();
        }
    }
}
