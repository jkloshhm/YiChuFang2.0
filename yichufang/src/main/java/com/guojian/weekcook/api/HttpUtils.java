package com.guojian.weekcook.api;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * PS: 网络请求工具类
 *
 * @author jack.guo,  Date on 2018/5/31.
 */
public class HttpUtils {
    private static final String BASE_URL = "http://jisusrecipe.market.alicloudapi.com/recipe/";
    private Retrofit retrofit;
    //7天 无网超时时间
    private static final int NO_NET_MAX = 60 * 60 * 24 * 7;
    //30秒  有网超时时间
    private static final int NET_MAX = 30;

    private HttpUtils() {
        Interceptor mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (true/*!NetWorkUtils.networkIsAvailable(AaronApplication.getObjectContext())*/) {
                    request = request.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "private, only-if-cached, max-stale=" + NO_NET_MAX)
                            .build();
                } else {
                    request = request.newBuilder()
                            //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                            .removeHeader("Pragma")
                            //添加缓存请求头
                            .header("Cache-Control", "private, max-age=" + NET_MAX)
                            .build();
                }

                return chain.proceed(request);
            }
        };

        OkHttpClient mClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(mInterceptor)
                //.cache(new Cache(new File(AaronApplication.getObjectContext().getCacheDir() + "http"), 1024 * 1024 * 10))
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mClient)
                .build();
    }

    /**
     * 单例设计模式
     */
    private static class singRetrofit {
        private static final HttpUtils instance = new HttpUtils();
    }

    public static HttpUtils getInstance() {
        return singRetrofit.instance;
    }

    public static ApiCook createApiCook() {
        //GetWeatherService getWeatherService = getInstance().retrofit.create(GetWeatherService.class);
        return getInstance().retrofit.create(ApiCook.class);
    }

}
