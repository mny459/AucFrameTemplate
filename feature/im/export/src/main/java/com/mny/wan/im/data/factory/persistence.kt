package com.mny.wan.im.data.factory

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.mny.wan.im.data.api.model.account.AccountRspModel

/**
 * Desc:
 */
object AccountManager {
    private const val KEY_PUSH_ID = "KEY_PUSH_ID"
    private const val KEY_IS_BIND = "KEY_IS_BIND"
    private const val KEY_TOKEN = "KEY_TOKEN"
    private const val KEY_USER_ID = "KEY_USER_ID"
    private const val KEY_ACCOUNT = "KEY_ACCOUNT"
    private const val KEY_ACCOUNT_INFO = "KEY_ACCOUNT_INFO"

    private const val KEY_SP_NAME = "account"
    private const val EMPTY_STR = ""

    // 设备的推送Id
    private var pushId: String = EMPTY_STR

    // 设备Id是否已经绑定到了服务器
    private var isBind = false

    // 登录状态的Token，用来接口请求
    private var token: String = EMPTY_STR

    // 登录的用户ID
    private var userId: String = EMPTY_STR

    // 登录的账户
    private var account: String = EMPTY_STR


    /**
     * 存储数据到XML文件，持久化
     */
    private fun save() {
        SPUtils.getInstance(KEY_SP_NAME).put(KEY_PUSH_ID, pushId)
        SPUtils.getInstance(KEY_SP_NAME).put(KEY_IS_BIND, isBind)
        SPUtils.getInstance(KEY_SP_NAME).put(KEY_TOKEN, token)
        SPUtils.getInstance(KEY_SP_NAME).put(KEY_USER_ID, userId)
        SPUtils.getInstance(KEY_SP_NAME).put(KEY_ACCOUNT, account)
    }

    /**
     * 进行数据加载
     */
    fun load() {
        pushId = SPUtils.getInstance(KEY_SP_NAME).getString(KEY_PUSH_ID, EMPTY_STR)
        isBind = SPUtils.getInstance(KEY_SP_NAME).getBoolean(KEY_IS_BIND, false)
        token = SPUtils.getInstance(KEY_SP_NAME).getString(KEY_TOKEN, EMPTY_STR)
        userId = SPUtils.getInstance(KEY_SP_NAME).getString(KEY_USER_ID, EMPTY_STR)
        account = SPUtils.getInstance(KEY_SP_NAME).getString(KEY_ACCOUNT, EMPTY_STR)
    }


    /**
     * 设置并存储设备的Id
     *
     * @param pushId 设备的推送ID
     */
    fun setPushId(pushId: String) {
        this.pushId = pushId
        save()
    }

    /**
     * 获取推送Id
     *
     * @return 推送Id
     */
    fun getPushId(): String {
        return pushId
    }

    /**
     * 返回当前账户是否登录
     *
     * @return True已登录
     */
    fun isLogin(): Boolean = !TextUtils.isEmpty(userId)
            && !TextUtils.isEmpty(token)

//    /**
//     * 是否已经完善了用户信息
//     *
//     * @return True 是完成了
//     */
//    fun isComplete(): Boolean {
//        // 首先保证登录成功
//        if (isLogin()) {
//            val self: User = getUser()
//            return (!TextUtils.isEmpty(self.desc)
//                    && !TextUtils.isEmpty(self.portrait)
//                    && self.sex != 0)
//        }
//        // 未登录返回信息不完全
//        return false
//    }

    /**
     * 是否已经绑定到了服务器
     *
     * @return True已绑定
     */
    fun isBind(): Boolean = isBind

    /**
     * 设置绑定状态
     */
    fun setBind(isBind: Boolean) {
        this.isBind = isBind
        save()
    }

    /**
     * 保存我自己的信息到持久化XML中
     *
     * @param model AccountRspModel
     */
    fun saveUserInfo(model: AccountRspModel, accountInfo: String) {
        // 存储当前登录的账户, token, 用户Id，方便从数据库中查询我的信息
        this.token = model.token
        this.account = model.account
        this.userId = model.user.serverId
        SPUtils.getInstance(KEY_SP_NAME).put(KEY_ACCOUNT_INFO, accountInfo)
        save()
    }

//    /**
//     * 获取当前登录的用户信息
//     *
//     * @return User
//     */
//    fun getUser(): User {
//        // 如果为null返回一个new的User，其次从数据库查询
//        return if (TextUtils.isEmpty(userId)) User() else
//            SQLite.select()
//                    .from(User::class.java)
//                    .where(User_Table.id.eq(userId))
//                    .querySingle() ?: User()
//    }

    /**
     * 获取当前登录的Token
     *
     * @return Token
     */
    fun getToken(): String = token

    fun getUserId(): String = this.userId
}