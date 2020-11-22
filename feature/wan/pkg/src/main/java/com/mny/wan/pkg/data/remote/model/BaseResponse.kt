package com.mny.wan.pkg.data.remote.model

import com.mny.wan.pkg.data.remote.service.WanApi

data class BaseResponse<T>(val errorCode: Int, val errorMsg: String, val data: T) {
    fun isSuccess(): Boolean = errorCode == WanApi.RequestSuccess
}