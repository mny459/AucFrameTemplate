package com.mny.mojito.im.data.api.service

import com.mny.mojito.im.data.api.message.MsgCreateModel
import com.mny.mojito.im.data.api.model.BaseResponse
import com.mny.mojito.im.data.api.model.GroupCreateModel
import com.mny.mojito.im.data.api.model.GroupMemberAddModel
import com.mny.mojito.im.data.api.model.account.AccountRspModel
import com.mny.mojito.im.data.api.model.account.LoginModel
import com.mny.mojito.im.data.api.model.account.RegisterModel

import com.mny.mojito.im.data.api.model.user.UserUpdateModel
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.data.card.GroupMemberCard
import com.mny.mojito.im.data.card.MessageCard
import com.mny.mojito.im.data.card.UserCard
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

/**
 * Desc:
 */
const val PREFIX = "/Gradle___iTalker___iTalker_1_0_SNAPSHOT_war/api/"

interface UserService {

    @POST("${PREFIX}account/login")
    suspend fun accountLogin(@Body model: LoginModel): BaseResponse<AccountRspModel>

    @POST("${PREFIX}account/register")
    suspend fun accountRegister(@Body model: RegisterModel): BaseResponse<AccountRspModel>

    @POST("${PREFIX}account/bind/{pushId}")
    suspend fun accountBind(@Path(encoded = true, value = "pushId") pushId: String): BaseResponse<AccountRspModel>

    // 用户更新的接口
    @PUT("${PREFIX}user")
    suspend fun userUpdate(@Body model: UserUpdateModel): BaseResponse<UserCard>

    // 用户搜索的接口
    @GET("${PREFIX}user/search/{name}")
    suspend fun userSearch(@Path("name") name: String): BaseResponse<List<UserCard>>

    // 用户关注接口
    @PUT("${PREFIX}user/follow/{userId}")
    suspend fun userFollow(@Path("userId") userId: String): BaseResponse<UserCard>

    // 获取联系人列表
    @GET("${PREFIX}user/contact")
    suspend fun userContacts(): BaseResponse<List<UserCard>>

    // 查询某人的信息
    @GET("${PREFIX}user/{userId}")
    suspend fun userFind(@Path("userId") userId: String): BaseResponse<UserCard>

    //
    // 发送消息的接口
    @POST("${PREFIX}msg/push")
    suspend fun msgPush(@Body model: MsgCreateModel): BaseResponse<MessageCard>

    // 创建群
    @POST("${PREFIX}group/create")
    suspend fun groupCreate(@Body model: GroupCreateModel): BaseResponse<GroupCard>

    // 拉取群信息
    @GET("${PREFIX}group/{groupId}")
    suspend fun groupFind(@Path("groupId") groupId: String): BaseResponse<GroupCard>

    // 群搜索的接口
    @GET("${PREFIX}group/search/{name}")
    suspend fun groupSearch(@Path(value = "name", encoded = true) name: String): BaseResponse<List<GroupCard>>

    // 我的群列表
    @GET("${PREFIX}group/list/{date}")
    suspend fun groups(@Path(value = "date", encoded = true) date: String): BaseResponse<List<GroupCard>>

    //
//    // 我的群的成员列表
    @GET("${PREFIX}group/{groupId}/member")
    suspend fun groupMembers(@Path("groupId") groupId: String): BaseResponse<List<GroupMemberCard>>

    //
    // 给群添加成员
    @POST("${PREFIX}group/{groupId}/member")
    suspend fun groupMemberAdd(@Path("groupId") groupId: String, @Body model: GroupMemberAddModel): BaseResponse<List<GroupMemberCard>>

    //分割线

    // 查询某人的信息
    @GET("${PREFIX}user/{userId}")
    suspend fun findUser(@Path("userId") userId: String): Flow<BaseResponse<UserCard>>
}