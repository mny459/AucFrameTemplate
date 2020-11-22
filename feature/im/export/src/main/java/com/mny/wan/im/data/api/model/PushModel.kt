package com.mny.wan.im.data.api.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * 消息推送 Model
 */
data class PushModel(val entities: List<Entity> = arrayListOf()) {

    data class Entity(  // 消息类型
            var type: Int = 0,
            // 消息实体
            var content: String,
            // 消息生成时间
            var createAt: Date)

    companion object {
        // 退出登录
        const val ENTITY_TYPE_LOGOUT = -1

        // 普通消息送达
        const val ENTITY_TYPE_MESSAGE = 200

        // 新朋友添加
        const val ENTITY_TYPE_ADD_FRIEND = 1001

        // 新群添加
        const val ENTITY_TYPE_ADD_GROUP = 1002

        // 新的群成员添加
        const val ENTITY_TYPE_ADD_GROUP_MEMBERS = 1003

        // 群成员信息更改
        const val ENTITY_TYPE_MODIFY_GROUP_MEMBERS = 2001

        // 群成员退出
        const val ENTITY_TYPE_EXIT_GROUP_MEMBERS = 3001

        /**
         * 把一个Json字符串，转化为一个实体数组
         * 并把数组封装到PushModel中，方便后面的数据流处理
         *
         * @param json Json数据
         * @return
         */
        fun decode(gson: Gson, json: String): PushModel? {
            val type = object : TypeToken<List<Entity>>() {}.type
            try {
                val entities: List<Entity> = gson.fromJson(json, type)
                if (entities.isNotEmpty()) return PushModel(entities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    override fun toString(): String {
        return "PushModel(entities=$entities)"
    }
}
