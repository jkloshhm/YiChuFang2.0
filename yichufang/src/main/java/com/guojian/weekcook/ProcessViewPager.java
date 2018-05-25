package com.guojian.weekcook;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.utils.ImageLoaderWithGlide;

import java.util.List;


/**
 * 创建时间: 2016年1月10日 下午3:43:22<br>
 * 版本: [v1.0]<br>
 * 类描述: 实现ViewPager轮播图<br>
 * 使用了ImageLoader 对图片进行加载，所以使用前必须初始化ImageLoader<br>
 * stopPlaying() 当轮播所在页面不在顶栈时，有必要停止定时并且释放资源<br>
 * startPlaying() 当再次恢复时调用<br>
 *
 * @author jkloshhm
 */
public class ProcessViewPager extends FrameLayout {

    /**
     * ViewPageItem点击回调接口
     */
    private ProcessViewPager.OnPageItemClickListener onPageItemClickListener;

    private final static String TAG = "ProcessViewPager";

    /**
     * 轮播图图片数量
     */
    private int IMAGE_COUNT;

    /**
     * 自定义轮播图资源
     */
    private List<CookListBean.ResultBean.ListBean.ProcessBean> mProcessBeanList;

    /**
     * 轮播容器
     */
    private ViewPager mViewPager;
    /**
     * 当前轮播页
     */
    private int currentItem = 0;

    private Context mContext;
    private LayoutInflater mInflate;
    private TextView mPosition;

    public ProcessViewPager(Context context) {
        this(context, null);
    }

    public ProcessViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessViewPager(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public ProcessViewPager initialize(List<CookListBean.ResultBean.ListBean.ProcessBean> processBeanList) {

        if (processBeanList != null && !processBeanList.isEmpty()) {
            mProcessBeanList = processBeanList;
        }/* else {//没有数据使用默认的图片资源
            mAutoPlayInfoList = getDefaultUrlAutoPlayInfoList();
		}*/
        IMAGE_COUNT = mProcessBeanList.size();
        return this;
    }

    public void setOnPageItemClickListener(ProcessViewPager.OnPageItemClickListener onPageItemClickListener) {
        this.onPageItemClickListener = onPageItemClickListener;
    }

    public interface OnPageItemClickListener {
        /**
         * ViewPageItem点击事件回调
         */
        //void onPageItemClick(int position, ProcessBean processBean);
    }

    /**
     * 初始化Views 及组件UI
     */
    public void build(int position) {
        if (mProcessBeanList == null || mProcessBeanList.isEmpty()) {
            Log.d(TAG, "init image fail ");
            return;
        }
        mInflate = LayoutInflater.from(mContext);
        mInflate.inflate(R.layout.view_layout_slideshow_process_viewpager, this, true);
        mPosition = (TextView) findViewById(R.id.tv_process_position);

        mViewPager = (ViewPager) findViewById(R.id.viewPager_process);
        //setViewPagerScrollSpeed();

        mPosition.setText("" + (position + 1) + "/" + mProcessBeanList.size());
        mViewPager.setFocusable(true);
        mViewPager.setOffscreenPageLimit(2);// 设置缓存页面，当前页面的相邻N各页面都会被缓存
        mViewPager.setAdapter(new ProcessPagerAdapter());
        mViewPager.setCurrentItem(position);
        AutoPlayingPageChangeListener mPageChangeListener = new AutoPlayingPageChangeListener();
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class ProcessPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final CookListBean.ResultBean.ListBean.ProcessBean processBean = mProcessBeanList.get(position % IMAGE_COUNT);
            View view = mInflate.inflate(R.layout.item_process_viewpager, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_item_process_pic_viewpager);
            TextView labelTitle = (TextView) view.findViewById(R.id.tv_item_process_content_viewpager);

            if (!TextUtils.isEmpty(processBean.getPcontent())) {//通过URL时使用ImageLoader加载图片
                //ImageLoaderUtil.setPicBitmap(imageView, processBean.getProcess_pic());
                ImageLoaderWithGlide.loadImage(mContext, processBean.getPic(), imageView);
            }
            if (!TextUtils.isEmpty(processBean.getPcontent())) {//有标题数据才显示
                labelTitle.setText(processBean.getPcontent().replace("<br />", ""));
            } else {//没有标题数据不显示文本透明背景
                labelTitle.setBackgroundDrawable(null);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mProcessBeanList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    /**
     * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
     */
    private class AutoPlayingPageChangeListener implements OnPageChangeListener {
        // boolean isChange = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            int p = (pos % IMAGE_COUNT);
            mPosition.setText("" + (p + 1) + "/" + IMAGE_COUNT);

        }
    }


}
