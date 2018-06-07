package com.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.guojian.weekcook.R;
import com.guojian.weekcook.activity.CookListActivity;
import com.guojian.weekcook.activity.RecipeDetailsActivity;
import com.guojian.weekcook.activity.SearchActivity;
import com.guojian.weekcook.AutoPlayingViewPager;
import com.guojian.weekcook.api.HttpUtils;
import com.guojian.weekcook.bean.CookDetailBean;
import com.guojian.weekcook.bean.CookListBean;
import com.guojian.weekcook.utils.RandomNum;
import com.guojian.weekcook.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jkloshhm on 2017-11-16. 首页
 */
public class HomeFragment extends Fragment {

    private List<CookListBean.ResultBean.ListBean> cookBeanList = null;
    private AutoPlayingViewPager mAutoPlayingViewPager;
    private Context mContext;
    private AutoPlayingViewPager.OnPageItemClickListener
            onPageItemClickListener = new AutoPlayingViewPager.OnPageItemClickListener() {

        @Override
        public void onPageItemClick(int position, CookListBean.ResultBean.ListBean cookBean) {
            // 直接返回链接,使用WebView加载
            if (cookBean != null) {
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                intent.putExtra("cookBean01", cookBean);
                mContext.startActivity(intent);
            }
        }

    };
    private final int num[] = RandomNum.makeCount();
    private List<Integer> numList = null;
    private int imgId[] = {R.mipmap.hot_material_nangua, R.mipmap.hot_material_muer,
            R.mipmap.hot_material_ou, R.mipmap.hot_material_shanyao,
            R.mipmap.hot_material_baicai, R.mipmap.hot_material_hongshu,
            R.mipmap.hot_material_yangrou, R.mipmap.hot_material_niurou};
    private String imgName[] = {"南瓜", "木耳", "藕", "山药", "白菜", "红薯", "羊肉", "牛肉"};
    private int imgIdFood[] = {R.mipmap.hot_class_sifangcai, R.mipmap.hot_class_shanxixiaochi,
            R.mipmap.hot_class_chuancai, R.mipmap.hot_class_jianfei,
            R.mipmap.hot_class_kuaishoucai, R.mipmap.hot_class_beijingxiaochi,
            R.mipmap.hot_class_yidalicai, R.mipmap.hot_class_meirongcai};
    private String imgNameFood[] = {"私房菜", "陕西小吃", "川菜", "减肥",
            "快手菜", "北京小吃", "意大利菜", "美容"};
    private String classId[] = {"303", "285", "224", "2", "304", "270", "258", "6"};

    private GridView mGridViewHotMaterial, mGridViewHotClassFood;
    private List<Map<String, Object>> mHotMaterialList;
    private List<Map<String, Object>> mHotClassFoodList;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout mSearchLinearLayout =  view.findViewById(R.id.ll_search);
        mSearchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(mIntent);
            }
        });

        SharedPreferences preferences = mContext.getSharedPreferences("cooking",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //String dateFormat = "yyyyMMdd";
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        String date = df.format(new Date());
        Log.i("jkloshhm", date);
        if (preferences.getString("date", null) == null) {
            editor.putString("date", df.format(new Date()));
        }
        editor.putString("num01", String.valueOf(num[0]));
        editor.putString("num02", String.valueOf(num[1]));
        editor.putString("num03", String.valueOf(num[2]));
        if (date.equals(preferences.getString("date", null))) {
            Log.i("jkloshhm______if", date);
            editor.putString("num01", preferences.getString("num01", null));
            editor.putString("num02", preferences.getString("num02", null));
            editor.putString("num03", preferences.getString("num03", null));
        } else {
            Log.i("jkloshhm______else", date);
            int num[] = RandomNum.makeCount();
            editor.putString("date", date);
            editor.putString("num01", String.valueOf(num[0]));
            editor.putString("num02", String.valueOf(num[1]));
            editor.putString("num03", String.valueOf(num[2]));
        }
        editor.apply();

        numList = new ArrayList<>();
        numList.add(0, Integer.parseInt(preferences.getString("num01", null)));
        numList.add(1, Integer.parseInt(preferences.getString("num02", null)));
        numList.add(2, Integer.parseInt(preferences.getString("num03", null)));
        Log.i("jkloshhm", numList.toString());

        mAutoPlayingViewPager =  view.findViewById(R.id.auto_play_viewpager);
        cookBeanList = new ArrayList<>();
        setViewPager();
        mGridViewHotMaterial =  view.findViewById(R.id.gv_hot_material);
        setUpViewHotMaterial();
        mGridViewHotClassFood =  view.findViewById(R.id.gv_hot_class_food);
        setUpViewHotClassFood();
        return view;
    }


    private void setUpViewHotClassFood() {
        //新建List
        mHotClassFoodList = new ArrayList<Map<String, Object>>();
        //获取数据
        getHotClassFoodData();
        //新建适配器
        String[] from = {"image", "name"};
        int[] to = {R.id.iv_hot_class_food_item_img, R.id.tv_hot_class_food_item_name};
        SimpleAdapter mHotClassFoodSimpleAdapter = new SimpleAdapter(mContext, mHotClassFoodList,
                R.layout.hot_class_food_gv_adapter_item, from, to);
        //配置适配器
        mGridViewHotClassFood.setAdapter(mHotClassFoodSimpleAdapter);
        mGridViewHotClassFood.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridViewHotClassFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map1 = mHotClassFoodList.get(position);
                String classId = (String) map1.get("classId");
                String name = (String) map1.get("name");
                Intent intent = new Intent(mContext, CookListActivity.class);
                intent.putExtra("classId", classId);
                intent.putExtra("name", name);
                intent.putExtra("CookType", "getDataByClassId");
                startActivity(intent);
            }
        });
    }

    private void setUpViewHotMaterial() {
        //新建List
        mHotMaterialList = new ArrayList<>();
        //获取数据
        getHotMaterialData();
        //新建适配器
        String[] from = {"image", "name"};
        int[] to = {R.id.iv_hot_material_item_img, R.id.tv_hot_material_item_name};
        SimpleAdapter mHotMaterialSimpleAdapter = new SimpleAdapter(mContext, mHotMaterialList,
                R.layout.hot_material_gv_adapter_item, from, to);
        //配置适配器
        mGridViewHotMaterial.setAdapter(mHotMaterialSimpleAdapter);
        mGridViewHotMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = mHotMaterialList.get(position);
                String name = (String) map.get("name");
                Intent intent = new Intent(mContext, CookListActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("CookType", "getDataBySearchName");
                startActivity(intent);
            }
        });
    }

    public void getHotMaterialData() {
        for (int i = 0; i < imgId.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("name", imgName[i]);
            map.put("image", imgId[i]);
            mHotMaterialList.add(map);
        }
    }

    public void getHotClassFoodData() {
        for (int i = 0; i < imgIdFood.length; i++) {
            Map<String, Object> map = new HashMap<>(3);
            map.put("name", imgNameFood[i]);
            map.put("image", imgIdFood[i]);
            map.put("classId", classId[i]);
            mHotClassFoodList.add(map);
        }
    }

    private void setViewPager() {
        try {
            for (int i = 0; i < 3; i++) {
                HttpUtils.request(HttpUtils.createApiCook().getDataByIdNew(numList.get(i)), new HttpUtils.IResponseListener<CookDetailBean>() {
                    @Override
                    public void onSuccess(CookDetailBean data) {
                        ToastUtils.showShortToast("banner加载成功~" + numList.size());
                        cookBeanList.add(data.getResult());
                        if (null != cookBeanList && cookBeanList.size() == 3) {
                            mAutoPlayingViewPager.initialize(cookBeanList).build();
                            mAutoPlayingViewPager.setOnPageItemClickListener(onPageItemClickListener);
                            mAutoPlayingViewPager.startPlaying();
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.showShortToast("banner加载失败~" + numList.size());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        //没有数据时不执行startPlaying,避免执行几次导致轮播混乱
        if (cookBeanList != null && !cookBeanList.isEmpty()) {
            mAutoPlayingViewPager.startPlaying();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        mAutoPlayingViewPager.stopPlaying();
        super.onPause();
    }

}

