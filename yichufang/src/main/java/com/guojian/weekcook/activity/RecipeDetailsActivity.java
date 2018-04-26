package com.guojian.weekcook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guojian.weekcook.MyScrollView;
import com.guojian.weekcook.R;
import com.guojian.weekcook.adapter.MaterialAdapter;
import com.guojian.weekcook.adapter.ProcessAdapter;
import com.guojian.weekcook.bean.CookBean;
import com.guojian.weekcook.bean.MaterialBean;
import com.guojian.weekcook.bean.ProcessBean;
import com.guojian.weekcook.bean.StepViewPagerBean;
import com.guojian.weekcook.dao.DBServices;
import com.guojian.weekcook.dao.MyDBServiceUtils;
import com.guojian.weekcook.utils.GetBitmapFromSdCardUtil;
import com.guojian.weekcook.utils.ImageLoaderWithGlide;
import com.guojian.weekcook.utils.ScreenShotUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecipeDetailsActivity extends Activity implements MyScrollView.OnScrollListener {
    private static DBServices db;
    private static ArrayList<String> cookIdList = new ArrayList<>();
    private static ProgressDialog dialog;
    private String realIp;
    private CookBean cookBean;
    private ImageView mCollectImg, mDetailsImage;
    private ListView mListViewMaterial, mListViewProcess;
    private LinearLayout mDetailsTitleLinearLayout, mCollectLinearLayout, mButtonBack,
            mShareLinearLayout, mEndMessage, mEndMessageScreenShot;
    private MyScrollView mScrollView;
    private boolean isRed;
    private TextView mTitleName, mName, mContent, mPeopleNum, mCookingTime, mTag, mTotalSteps;
    private final static String TAG = "RecipeDetailsActivity";
    private int screenWidth;
    private int mDetailsTitleHeight;
    private int mScrollViewTop;
    private List<ProcessBean> processBeenList;


    //是否取消收藏
    public void setCancelCollection() {
        //提示对话框
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setView(getLayoutInflater().inflate(R.layout.alert_dialog_view, null));
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "移除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCollectImg.setImageDrawable(getResources()
                                .getDrawable(R.mipmap.cook_no_collected_white));
                        isRed = false;
                        Toast.makeText(RecipeDetailsActivity.this, "已取消收藏~", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        Button pButton = builder.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(getResources().getColor(R.color.red_theme));
        Button nButton = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(getResources().getColor(R.color.gray));
    }

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        setContentView(R.layout.activity_details);
        initViews();
        mShareLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mScrollView.getChildCount(); i++) {
                    mScrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                }
                dialog = new ProgressDialog(RecipeDetailsActivity.this);
                dialog.setMessage(" 截屏中，请稍等...");
                dialog.show();
                //开始执行AsyncTask，并传入某些数据
                //mEndMessage.setVisibility(View.GONE);
                //mEndMessageScreenShot.setVisibility(View.VISIBLE);
                new ScreenShotTask().execute("New Text");
            }
        });
        mScrollView.setOnScrollListener(this);
        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();

    }

    @Override
    public void onScroll(int scrollY) {

        if (scrollY >= screenWidth - mDetailsTitleHeight) {
            mTitleName.setVisibility(View.VISIBLE);
            mDetailsTitleLinearLayout.setBackgroundColor(getResources()
                    .getColor(R.color.red_theme));
        } else if (scrollY > 0 && scrollY <= screenWidth - mDetailsTitleHeight) {
            updateActionBarAlpha(scrollY * (255 - 25) / (screenWidth - mDetailsTitleHeight) + 25);
            mDetailsTitleLinearLayout.setBackgroundColor(getResources()
                    .getColor(R.color.white_00FFFFFF));
        } else if (scrollY <= screenWidth - mDetailsTitleHeight) {
            mTitleName.setVisibility(View.GONE);
            mDetailsTitleLinearLayout.setBackgroundColor(getResources()
                    .getColor(R.color.white_00FFFFFF));
        }
    }

    /**
     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mDetailsTitleHeight = mDetailsTitleLinearLayout.getHeight();
            //buyLayoutTop = mBuyLayout.getTop();
            mScrollViewTop = mScrollView.getTop();
        }
    }

    public void setActionBarAlpha(int alpha) throws Exception {
        if (mDetailsTitleLinearLayout == null || mScrollView == null) {
            throw new Exception("acitonBar is not binding or bgDrawable is not set.");
        }
        mDetailsTitleLinearLayout.setAlpha(alpha);
        //mActionBar.setBackgroundDrawable(mBgDrawable);
    }

    void updateActionBarAlpha(int alpha) {
        try {
            setActionBarAlpha(alpha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        mTitleName = (TextView) findViewById(R.id.tv_details_cook_title_name);
        //ImageView mShareImg = (ImageView) findViewById(R.id.iv_share_the_cook_data);
        mScrollView = (MyScrollView) findViewById(R.id.scrollView_details);
        mDetailsTitleLinearLayout = (LinearLayout) findViewById(R.id.ll_details_title);
        mShareLinearLayout = (LinearLayout) findViewById(R.id.ll_share_the_cook_data);
        mButtonBack = (LinearLayout) findViewById(R.id.ll_details_back_to_list);
        mCollectLinearLayout = (LinearLayout) findViewById(R.id.ll_collect_the_cook_data);
        mCollectImg = (ImageView) findViewById(R.id.iv_collection_img);
        mDetailsImage = (ImageView) findViewById(R.id.iv_details_img);
        mName = (TextView) findViewById(R.id.tv_details_cook_name);
        mContent = (TextView) findViewById(R.id.tv_details_cook_content);
        mPeopleNum = (TextView) findViewById(R.id.tv_details_cook_peoplenum);
        mCookingTime = (TextView) findViewById(R.id.tv_details_cook_cookingtime);
        mTag = (TextView) findViewById(R.id.tv_details_cook_tag);
        mListViewMaterial = (ListView) findViewById(R.id.lv_listview_material);
        mListViewProcess = (ListView) findViewById(R.id.lv_listview_process);
        //mlinearLayout = (LinearLayout) findViewById(R.id.linear1);
        mEndMessage = (LinearLayout) findViewById(R.id.ll_end_message);
        mEndMessageScreenShot = (LinearLayout) findViewById(R.id.ll_end_message_screen_shot);
        mTotalSteps = (TextView) findViewById(R.id.tv_cook_total_process);
    }

    @Override
    protected void onResume() {
        //Log.i(TAG, "RecipeDetailsActivity ____________onResume()");
        super.onResume();
        initDB();
        Intent intent = this.getIntent();
        cookBean = (CookBean) intent.getSerializableExtra("cookBean01");
        setUpViews();
        realIp = cookBean.getReal_ip();
        Log.i(TAG, "realIp==" + realIp);
        if (realIp.equals("mary") && !cookIdList.contains(cookBean.getId_cook())) {
            mCollectImg.setImageDrawable(getResources()
                    .getDrawable(R.mipmap.cook_no_collected_white));
            isRed = false;
        } else {
            mCollectImg.setImageDrawable(getResources()
                    .getDrawable(R.mipmap.cook_collected_white));
            isRed = true;
        }
        Log.i(TAG, "isRed==" + realIp);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCollectLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRed) {//删除
                    setCancelCollection();
                } else {
                    mCollectImg.setImageDrawable(getResources()
                            .getDrawable(R.mipmap.cook_collected_white));
                    isRed = true;
                    Toast.makeText(RecipeDetailsActivity.this, "收藏成功~", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "DetailsActivity_onPause()");
        super.onPause();
        if (realIp.equals("mary") && !cookIdList.contains(cookBean.getId_cook())) {
            if (isRed) {//保存
                MyDBServiceUtils.saveData(cookBean, db);
            }
        } else {
            if (!isRed) {//删除
                MyDBServiceUtils.delectData(cookBean, db);
                cookBean.setReal_ip("mary");
            }
        }
    }

    private void initDB() {
        cookIdList.clear();
        db = MyDBServiceUtils.getInstance(this);
        ArrayList<CookBean> cookBeanList = MyDBServiceUtils.getAllObject(db);
        for (int i = 0; i < cookBeanList.size(); i++) {
            String cook_id = cookBeanList.get(i).getId_cook();
            cookIdList.add(cook_id);
        }
    }

    private void setUpViews() {
        mTitleName.setText(cookBean.getName_cook());
        mName.setText(cookBean.getName_cook());
        ImageLoaderWithGlide.loadImage(mContext,cookBean.getPic(),mDetailsImage);
        String mContentString = cookBean.getContent().replace("<br />", "");
        mContent.setText(mContentString);
        String mPeopleNumString = "用餐人数: " + cookBean.getPeoplenum();
        mPeopleNum.setText(mPeopleNumString);
        String mCookingTimeString = "烹饪时间: " + cookBean.getCookingtime();
        mCookingTime.setText(mCookingTimeString);
        String mTagString = "标签: " + cookBean.getTag_cook();
        mTag.setText(mTagString);
        List<MaterialBean> materialBeanList = cookBean.getMaterialBeen();
        MaterialAdapter mMaterialAdapter = new MaterialAdapter(this, materialBeanList);
        mListViewMaterial.setAdapter(mMaterialAdapter);
        setListViewHeightBasedOnChildren1(mListViewMaterial);
        processBeenList = cookBean.getProcessBeen();
        ProcessAdapter mProcessAdapter = new ProcessAdapter(this, processBeenList);
        mListViewProcess.setAdapter(mProcessAdapter);
        setListViewHeightBasedOnChildren1(mListViewProcess);
        mListViewProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String positionString = "" + position;
                StepViewPagerBean stepViewPagerBean = new StepViewPagerBean(processBeenList, positionString);
                Intent mIntent = new Intent(RecipeDetailsActivity.this, ProcessLargeImgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stepViewPagerBean", stepViewPagerBean);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }
        });
        String mTotalStepsString = "共" + processBeenList.size() + "步,点击进入大图";
        mTotalSteps.setText(mTotalStepsString);
    }

    public void setListViewHeightBasedOnChildren1(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //params.height += 16;// if without this statement,the listview will be a little short
        listView.setLayoutParams(params);
    }

    /**
     * 分享功能
     *
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }



    String screenShotFileName = null;

    private class ScreenShotTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                if (GetBitmapFromSdCardUtil.hasSdcard()) {
                    try {
                        Bitmap mScreenShotBitmap = ScreenShotUtils.getScrollViewBitmap(mScrollView);
                        screenShotFileName = ScreenShotUtils.savePic(mScreenShotBitmap,cookBean.getId_cook());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "无SD卡,存储失败!", Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            //更新UI的操作，这里面的内容是在UI线程里面执行的
            dialog.dismiss();
            //使用系统分享分享图片
            shareMsg("易厨房",
                    "《易厨房》--推荐下载",
                    "我正在使用《易厨房》APP分享菜谱,下载地址：http://a.app.qq.com/o/simple.jsp?pkgname=com.guojian.weekcook",
                    screenShotFileName);
        }
    }

}
