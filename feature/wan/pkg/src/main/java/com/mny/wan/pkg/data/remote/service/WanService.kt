package com.mny.wan.pkg.data.remote.service

import com.mny.wan.pkg.data.remote.model.UserInfoModel
import com.mny.wan.pkg.data.remote.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface WanService {

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String)
            : BaseResponse<UserInfoModel>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login1(@Field("username") username: String, @Field("password") password: String)
            : Flow<BaseResponse<UserInfoModel>>

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
            @Field("username") username: String
            , @Field("password") password: String
            , @Field("repassword") rePassword: String
    ): BaseResponse<UserInfoModel>

    @GET("/user/logout/json")
    suspend fun logout(): BaseResponse<String>

    @GET
    suspend fun fetchArticleList(@Url path: String): BaseResponse<BeanArticleList>

    @GET("/article/top/json")
    suspend fun fetchArticleTopList(): BaseResponse<MutableList<BeanArticle>>

    @GET("/banner/json")
    suspend fun fetchBannerList(): BaseResponse<MutableList<BeanBanner>>

    @GET("/project/tree/json")
    suspend fun fetchProjectTree(): BaseResponse<MutableList<BeanProject>>

    @GET("/tree/json")
    suspend fun fetchSystemTree(): BaseResponse<MutableList<BeanSystemParent>>

    @GET("/wxarticle/chapters/json")
    suspend fun fetchWeChatTree(): BaseResponse<MutableList<BeanSystemParent>>

    @GET("/navi/json")
    suspend fun fetchNavTree(): BaseResponse<MutableList<BeanNav>>

    @FormUrlEncoded
    @POST
    suspend fun search(
        @Url path: String, @Field("k") keyWord: String
    ): BaseResponse<BeanArticleList>

    @POST("/lg/collect/{id}/json")
    suspend fun collectWanArticle(@Path("id") id: Int): BaseResp

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectWanArticle(@Path("id") id: Int): BaseResp

    @GET("/lg/coin/userinfo/json")
    suspend fun coinInfo(): BaseResponse<BeanCoin>

    @GET("/lg/coin/list/{page}/json")
    suspend fun coinList(@Path("page") page: Int): BaseResponse<BaseListData<BeanCoinOpDetail>>

    @GET("/coin/rank/{page}/json")
    suspend fun rankList(@Path("page") page: Int): BaseResponse<BaseListData<BeanRanking>>

    @GET("/hotkey/json")
    suspend fun hotKey(): BaseResponse<MutableList<BeanHotKey>>

    // 广场
    @GET
    suspend fun fetchMineShareArticleList(@Url path: String): BaseResponse<BeanMineShareArticle>
}