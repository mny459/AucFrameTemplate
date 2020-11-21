package com.mny.mojito.im.gt

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.igexin.sdk.PushConsts
import com.mny.mojito.im.data.api.model.PushModel
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.data.card.GroupMemberCard
import com.mny.mojito.im.data.card.MessageCard
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.data.factory.AccountManager
import com.mny.mojito.im.domain.usecase.MessageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Desc:
 */
@AndroidEntryPoint
class GTMessageReceiver : HiltReceiver() {
    @Inject
    lateinit var mMessageUseCase: MessageUseCase

    @Inject
    lateinit var mGson: Gson
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent == null) return
        val bundle = intent.extras
        when (bundle!!.getInt(PushConsts.CMD_ACTION)) {
            PushConsts.GET_CLIENTID -> {
                LogUtils.i("GET_CLIENTID:$bundle")
                // 当Id初始化的时候
                // 获取设备Id
                onClientInit(bundle.getString("clientid", ""))
            }
            PushConsts.GET_MSG_DATA -> {
                // 常规消息送达
                val payload = bundle.getByteArray("payload")
                if (payload != null) {
                    val message = String(payload)
                    onMessageArrived(message)
                }
            }
            else -> LogUtils.i("OTHER:$bundle")
        }
    }

    /**
     * 当Id初始化的试试
     *
     * @param cid 设备Id
     */
    private fun onClientInit(cid: String) {
        // 设置设备Id
        AccountManager.setPushId(cid)
        if (!TextUtils.isEmpty(cid) && AccountManager.isLogin()) {
            // 账户登录状态，进行一次PushId绑定
            // 没有登录是不能绑定PushId的
            // TODO 绑定 push ID
//            AccountHelper.bindPush(null)
        }
    }

    /**
     * 消息达到时
     *
     * @param message 新消息
     */
    private fun onMessageArrived(message: String) {
        LogUtils.i("onMessageArrived $message")
        // 首先检查登录状态
        if (!AccountManager.isLogin())
            return;

        val model = PushModel.decode(mGson, message) ?: return
        LogUtils.e(model.toString())
        // 对推送集合进行遍历
        for (entity in model.entities) {
            when (entity.type) {
                PushModel.ENTITY_TYPE_LOGOUT ->
                    // 退出情况下，直接返回，并且不可继续
                    return

                PushModel.ENTITY_TYPE_MESSAGE -> {
                    // 普通消息
                    val card = mGson.fromJson(entity.content, MessageCard::class.java)
                    GlobalScope.launch(Dispatchers.IO) {
                        mMessageUseCase.handleMessage(card)
                    }
                }

                PushModel.ENTITY_TYPE_ADD_FRIEND -> {
                    // 好友添加
                    val card = mGson.fromJson(entity.content, UserCard::class.java)
                }

                PushModel.ENTITY_TYPE_ADD_GROUP -> {
                    // 添加群
                    val card = mGson.fromJson(entity.content, GroupCard::class.java)
                }
                PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS,
                PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS
                -> {
                    // 群成员变更, 回来的是一个群成员的列表
                    val type = object : TypeToken<List<GroupMemberCard>>() {}.type
                    val card: List<GroupMemberCard> = mGson.fromJson(entity.content, type)
                    // 把数据集合丢到数据中心处理
                }
                PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS -> {
                    // TODO 成员退出的推送
                }

            }
        }

    }
}