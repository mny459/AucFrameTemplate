package com.mny.mojito.error

import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import android.net.ParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import java.net.ConnectException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

object HttpExceptionHandler {
    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    internal object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 1002

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006
    }

    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val SERVICE_UNAVAILABLE = 503

    fun handleException(e: Throwable): CommonError.HttpError {
        var errCode: Int = ERROR.HTTP_ERROR
        var errMsg = "未知错误"
        when (e) {
            is HttpException -> {
                errCode = e.code()
                errMsg = when (e.code()) {
                    UNAUTHORIZED -> "操作未授权"
                    FORBIDDEN -> "请求被拒绝"
                    NOT_FOUND -> "资源不存在"
                    REQUEST_TIMEOUT -> "服务器执行超时"
                    INTERNAL_SERVER_ERROR -> "服务器内部错误"
                    SERVICE_UNAVAILABLE -> "服务器不可用"
                    else -> "网络错误"
                }
            }
            is JsonParseException, is JSONException,
            is ParseException, is MalformedJsonException -> {
                errCode = ERROR.PARSE_ERROR
                errMsg = "解析错误"
            }
            is ConnectException -> {
                errCode = ERROR.NETWORK_ERROR
                errMsg = "连接失败"
            }
            is SSLHandshakeException -> {
                errCode = ERROR.SSL_ERROR
                errMsg = "证书验证失败"
            }
            is ConnectTimeoutException, is SocketTimeoutException -> {
                errCode = ERROR.TIMEOUT_ERROR
                errMsg = "连接超时"
            }
            is UnknownHostException -> {
                errCode = ERROR.TIMEOUT_ERROR
                errMsg = "主机地址未知"
            }
            else -> {
                errCode = ERROR.UNKNOWN
                errMsg = "未知错误"
            }
        }
        return CommonError.HttpError(errCode, errMsg, e)
    }

}

