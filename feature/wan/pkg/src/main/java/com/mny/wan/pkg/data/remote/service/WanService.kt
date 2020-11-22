package com.mny.wan.pkg.data.remote.service

import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.login.data.remote.model.UserInfoModel
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
}