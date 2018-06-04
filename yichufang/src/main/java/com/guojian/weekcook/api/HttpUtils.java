package com.guojian.weekcook.api;

import com.guojian.weekcook.MyApplication;
import com.guojian.weekcook.utils.NetWorkUtils;
import com.guojian.weekcook.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * PS: 网络请求工具类
 *
 * @author jack.guo,  Date on 2018/5/31.
 */
public class HttpUtils {
    private static final String BASE_URL = "http://jisusrecipe.market.alicloudapi.com/recipe/";
    private Retrofit retrofit;
    /**
     * 7天：无网超时时间
     */
    private static final int NO_NET_MAX = 60 * 60 * 24 * 7;
    /**
     * 30秒：有网超时时间
     */
    private static final int NET_MAX = 30;

    private HttpUtils() {
        Interceptor mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtils.networkIsAvailable(MyApplication.getContext())) {
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
                .connectTimeout(15, TimeUnit.SECONDS)
                .cache(new Cache(new File(MyApplication.getContext().getCacheDir() + "http"), 1024 * 1024 * 10))
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();
    }

    /**
     * 单例设计模式
     */
    private static class SingleRetrofit {
        private static final HttpUtils HTTP_UTILS_INSTANCE = new HttpUtils();
    }

    private static HttpUtils getInstance() {
        return SingleRetrofit.HTTP_UTILS_INSTANCE;
    }

    public static ApiCook createApiCook() {
        return getInstance().retrofit.create(ApiCook.class);
    }

    /**
     * 执行网络请求
     *
     * @param observable 请求方法
     * @param listener   ResponseListener
     * @param <T>        泛型T
     */
    public static <T> void request(Observable<T> observable, final IResponseListener<T> listener) {

        if (!NetWorkUtils.networkIsAvailable(MyApplication.getContext())) {
            ToastUtils.showShortToast("网络不可用,请检查网络");
            if (listener != null) {
                listener.onFail();
            }
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   if (listener != null) {
                                       listener.onFail();
                                   }
                               }

                               @Override
                               public void onComplete() {
                               }

                               @Override
                               public void onSubscribe(Disposable disposable) {
                               }

                               @Override
                               public void onNext(T data) {
                                   if (listener != null) {
                                       listener.onSuccess(data);
                                   }
                               }
                           }
                );


    }

    public interface IResponseListener<T> {
        /**
         * 请求成功回调
         *
         * @param data 请求返回的bean
         */
        void onSuccess(T data);

        /**
         * 请求失败回调
         */
        void onFail();
    }

}
