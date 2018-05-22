package com.guojian.weekcook.Api;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.guojian.weekcook.bean.CookClassBean;
import com.guojian.weekcook.bean.CookDetailBean;
import com.guojian.weekcook.bean.CookListBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author jkloshhm on 2016-11-02.
 * <p>
 * modified at 2018-05-22  修改网络请求：阿里的ApiGatewayClient修改为retrofit
 */
public class GetJsonUtils {

    private static final String BASE_URL = "http://jisusrecipe.market.alicloudapi.com/recipe/";

    /**
     * 根据菜谱名字查询
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/search?keyword=%E7%99%BD%E8%8F%9C&num=5&start=0
     */
    public static void getDataBySearchName(final Handler handler, String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService mApiService = retrofit.create(ApiService.class);
        Call<CookListBean> responseBodyCall = mApiService.getDataByKeyword(name, 50, 0);
        responseBodyCall.enqueue(new Callback<CookListBean>() {
            @Override
            public void onResponse(Call<CookListBean> call, Response<CookListBean> response) {
                try {
                    String errorMessage = response.body().getMsg();
                    CookListBean.ResultBean stringBody = response.body().getResult();
                    Bundle bundle = new Bundle();
                    bundle.putString("classType", "getDataBySearchName");
                    bundle.putString("errorMessage", errorMessage);
                    bundle.putSerializable("stringBody", stringBody);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    Log.i("jack_guo", "加载成功：errorMessage==11111===" + errorMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CookListBean> call, Throwable t) {
                Log.i("jack_guo", "加载失败：t====11111=" + t.toString());
            }
        });
    }

    /**
     * 按分类检索接口
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/byclass?classid=2&num=10&start=0
     */
    public static void getDataByClassId(final Handler handler, String classId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService mApiService = retrofit.create(ApiService.class);
        Call<CookListBean> responseBodyCall = mApiService.getDataByClassId(Integer.parseInt(classId), 50, 0);
        responseBodyCall.enqueue(new Callback<CookListBean>() {
            @Override
            public void onResponse(Call<CookListBean> call, Response<CookListBean> response) {
                try {
                    String errorMessage = response.body().getMsg();
                    CookListBean.ResultBean stringBody = response.body().getResult();
                    Bundle bundle = new Bundle();
                    bundle.putString("classType", "getDataBySearchName");
                    bundle.putString("errorMessage", errorMessage);
                    bundle.putSerializable("stringBody", stringBody);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CookListBean> call, Throwable t) {
            }
        });
    }


    public static void getDataById(final Handler handler, Integer nameId, final String tag) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<CookDetailBean> listBeanCall = apiService.getDataById((int) nameId);
        listBeanCall.enqueue(new Callback<CookDetailBean>() {
            @Override
            public void onResponse(Call<CookDetailBean> call, Response<CookDetailBean> response) {

                CookListBean.ResultBean.ListBean stringBody = response.body().getResult();
                Bundle bundle = new Bundle();
                bundle.putSerializable("stringBody", stringBody);
                bundle.putString("tag", tag);
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<CookDetailBean> call, Throwable t) {
            }
        });

    }

    public static void getDataClass(final Handler handler) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jisusrecipe.market.alicloudapi.com/recipe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<CookClassBean> listBeanCall = apiService.getDataClass();
        listBeanCall.enqueue(new Callback<CookClassBean>() {
            @Override
            public void onResponse(Call<CookClassBean> call, Response<CookClassBean> response) {

                CookClassBean stringBody = response.body();
                Bundle bundle = new Bundle();
                bundle.putSerializable("stringBody", stringBody);
                Message msg = new Message();
                msg.setData(bundle);
                Log.i("guo", "stringBody==========utils====" + stringBody);
                handler.sendMessage(msg);
                Log.i("jack_guo", "加载成功：errorMessage=====333333" + response.body().getMsg());
            }

            @Override
            public void onFailure(Call<CookClassBean> call, Throwable t) {

            }
        });


       /* RpcService rpcService = ApiGatewayClient.getRpcService();
        final ApiRequest apiRequest = new ApiRequest();
        apiRequest.setAddress("http://jisusrecipe.market.alicloudapi.com");
        apiRequest.setPath("/recipe/class");
        apiRequest.addQuery("num", "30");
        apiRequest.setMethod(HttpMethod.GET);
        apiRequest.setTrustServerCertificate(true);
        apiRequest.setTimeout(10000);
        rpcService.call(apiRequest, new ApiResponseCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                try {
                    String errorMessage = apiResponse.getErrorMessage();
                    String stringBody = apiResponse.getStringBody();
                    Bundle bundle = new Bundle();
                    bundle.putString("classType", "getDataClass");
                    bundle.putString("errorMessage", errorMessage);
                    bundle.putString("stringBody", stringBody);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(ApiInvokeException e) {
                e.printStackTrace();
            }
        });*/
    }


}
