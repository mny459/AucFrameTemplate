package com.mny.wan.pkg.data.local

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.mny.wan.pkg.domain.model.UserInfoDomainModel
import com.tencent.mmkv.MMKV

/**
 * @Author CaiRj
 * @Date 2019/10/23 14:15
 * @Desc
 */
object UserInfoManager {
    private const val EMPTY = ""
    private const val KEY_USER_INFO = "user_info"
    private const val KEY_TOKEN_PASS = "token_pass"
    private const val KEY_LOGIN_STATUS = "login_status"
    private var mIsLogin = false
    private var mUserInfo: UserInfoDomainModel? = null
    private var mTokenPass: String = EMPTY

    private var mmkv: MMKV

    init {
        val root = MMKV.initialize(Utils.getApp())
        LogUtils.v("$root")
        mmkv = MMKV.defaultMMKV()
    }

    fun saveUserInfo(info: UserInfoDomainModel) {
        mmkv.encode(KEY_USER_INFO, GsonUtils.toJson(info))
        mmkv.encode(KEY_LOGIN_STATUS, true)
        mUserInfo = info
        mIsLogin = true
    }

    fun clearUserInfo() {
        mmkv.encode(KEY_USER_INFO, EMPTY)
        mmkv.encode(KEY_LOGIN_STATUS, false)
        mUserInfo = null
        mIsLogin = false
        clearTokenPass()
    }

    fun userInfo(): UserInfoDomainModel? = mUserInfo

    fun saveTokenPass(tokenPass: String) {
        mmkv.encode(KEY_TOKEN_PASS, tokenPass)
        mTokenPass = tokenPass
    }

    fun clearTokenPass() {
        mmkv.encode(KEY_TOKEN_PASS, EMPTY)
        mTokenPass = EMPTY
    }

    fun getTokenPass(): String = mTokenPass

    fun isLogin(): Boolean = mIsLogin

    fun initUserInfo() {
        val userInfo = mmkv.decodeString(KEY_USER_INFO, EMPTY)
        mUserInfo = if (userInfo.isNullOrEmpty()) null else GsonUtils.fromJson(
                userInfo,
                UserInfoDomainModel::class.java
        )
        mIsLogin = mmkv.decodeBool(KEY_LOGIN_STATUS, false)

        val tokenPass = mmkv.decodeString(KEY_TOKEN_PASS, EMPTY)
        mTokenPass = tokenPass
        LogUtils.d("$mUserInfo")
    }
}