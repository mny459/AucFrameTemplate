package com.mny.wan.error

/**
 * @Author CaiRj
 * @Date 2019/11/21 10:43
 * @Desc
 */
abstract class BaseError(errCode: Int, val errorMsg: String = "", cause: Throwable?) :
    Exception(errorMsg, cause)

data class HttpError(val throwable: Throwable, var errorCode: Int, var errorMsg: String = "") :
    Exception(errorMsg, throwable)

data class BadResponseError(val errorCode: Int, val errorMsg: String) : Exception(errorMsg)

sealed class CommonError {
    data class BadResponseError(var errCode: Int, var errMsg: String = "") : CommonError()
    data class HttpError(var errCode: Int, var errMsg: String = "网络异常", val cause: Throwable) :
        CommonError()

    data class SqlError(var errCode: Int, var errMsg: String = "数据库异常", val cause: Throwable) :
        CommonError()

    data class UnknownError(var errCode: Int, var errMsg: String = "未知异常", val cause: Throwable) :
        CommonError()
}