package com.guojian.weekcook.api;


import com.guojian.weekcook.bean.CookClassBean;
import com.guojian.weekcook.bean.CookDetailBean;
import com.guojian.weekcook.bean.CookListBean;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author jkloshhm at 2018-05-18  网络请求接口
 */
public interface ApiCook {

    /**
     * 根据菜谱名称查询
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/search?keyword=%E7%99%BD%E8%8F%9C&num=5&start=0
     *
     * @param keyword 菜谱名称关键字
     * @param num     获取数据条数
     * @param start   起始条数，默认0
     * @return CookListBean
     */
    @Headers("Authorization:APPCODE d8da87a316f745b1b7b413d910a1ac72")
    @GET("search")
    Call<CookListBean> getDataByKeyword(@Query("keyword") String keyword, @Query("num") int num, @Query("start") int start);


    /**
     * 按分类检索接口
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/byclass?classid=2&num=10&start=0
     *
     * @param classid 菜谱分类的id
     * @param num     获取数据条数
     * @param start   起始条数，默认0
     * @return CookListBean
     */
    @Headers("Authorization:APPCODE d8da87a316f745b1b7b413d910a1ac72")
    @GET("byclass")
    Call<CookListBean> getDataByClassId(@Query("classid") int classid, @Query("num") int num, @Query("start") int start);


    /**
     * 按id检索接口
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/detail?id=5
     *
     * @param id 起始条数，默认0
     * @return CookDetailBean
     */
    @Headers("Authorization:APPCODE d8da87a316f745b1b7b413d910a1ac72")
    @GET("detail")
    Call<CookDetailBean> getDataById(@Query("id") int id);


    /**
     * 检索菜谱分类接口
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/class
     *
     * @return CookClassBean
     */
    @Headers("Authorization:APPCODE d8da87a316f745b1b7b413d910a1ac72")
    @GET("class")
    Call<CookClassBean> getDataClass();


    /**
     * 检索菜谱分类接口
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/class
     *
     * @return CookClassBean
     */
    @Headers("Authorization:APPCODE d8da87a316f745b1b7b413d910a1ac72")
    @GET("class")
    Observable<CookClassBean> getDataClassNew();



    /**
     * 按id检索接口
     * 请求示例：http://jisusrecipe.market.alicloudapi.com/recipe/detail?id=5
     *
     * @param id 起始条数，默认0
     * @return CookDetailBean
     */
    @Headers("Authorization:APPCODE d8da87a316f745b1b7b413d910a1ac72")
    @GET("detail")
    Observable<CookDetailBean> getDataByIdNew(@Query("id") int id);
}
