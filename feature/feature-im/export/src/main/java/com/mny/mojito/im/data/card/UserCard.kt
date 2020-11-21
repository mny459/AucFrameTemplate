package com.mny.mojito.im.data.card

import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.data.factory.AccountManager
import java.util.*

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
data class UserCard(override var id: String = "",
                    override var name: String = "",
                    var phone: String = "",
                    override var portrait: String? = null,
                    var desc: String? = null,
                    var sex: Int = 0,
                    var follows: Int = 0, // 用户关注人的数量
                    val following: Int = 0, // 用户粉丝的数量
                    var isFollow: Boolean = false, // 我与当前User的关系状态，是否已经关注了这个人
                    val modifyAt: Date? = null  // 用户信息最后的更新时间
) : Author {

    // 缓存一个对应的User, 不能被GSON框架解析使用ø
    @Transient
    private var userEntity: UserEntity? = null

    fun buildUser(): UserEntity {
        if (userEntity == null) {
            val user = UserEntity(
                    serverId = id,
                    name = name,
                    portrait = portrait,
                    phone = phone,
                    desc = desc,
                    sex = sex,
                    isFollow = isFollow,
                    follows = follows,
                    following = following,
                    modifyAt = modifyAt
            )
            this.userEntity = user
        }
        return userEntity!!
    }

    fun isSelf(): Boolean = AccountManager.getUserId() == this.id

}