package com.mny.wan.im.data.api.model

import com.blankj.utilcode.util.StringUtils
import com.mny.wan.im.R
import java.util.*

data class BaseResponse<T>(val code: Int, val message: String, val result: T, val time: Date) {
    fun isSuccess(): Boolean = code == SUCCEED

    companion object {
        const val SUCCEED = 1

        const val ERROR_UNKNOWN = 0

        const val ERROR_NOT_FOUND_USER = 4041
        const val ERROR_NOT_FOUND_GROUP = 4042
        const val ERROR_NOT_FOUND_GROUP_MEMBER = 4043

        const val ERROR_CREATE_USER = 3001
        const val ERROR_CREATE_GROUP = 3002
        const val ERROR_CREATE_MESSAGE = 3003

        const val ERROR_PARAMETERS = 4001
        const val ERROR_PARAMETERS_EXIST_ACCOUNT = 4002
        const val ERROR_PARAMETERS_EXIST_NAME = 4003

        const val ERROR_SERVICE = 5001

        const val ERROR_ACCOUNT_TOKEN = 2001
        const val ERROR_ACCOUNT_LOGIN = 2002
        const val ERROR_ACCOUNT_REGISTER = 2003

        const val ERROR_ACCOUNT_NO_PERMISSION = 2010
    }
}

fun BaseResponse<*>.decodeFailReason(): String {
    val strRes = when (code) {
        BaseResponse.ERROR_SERVICE -> R.string.data_rsp_error_service
        BaseResponse.ERROR_NOT_FOUND_USER -> R.string.data_rsp_error_not_found_user
        BaseResponse.ERROR_NOT_FOUND_GROUP -> R.string.data_rsp_error_not_found_group
        BaseResponse.ERROR_NOT_FOUND_GROUP_MEMBER -> R.string.data_rsp_error_not_found_group_member
        BaseResponse.ERROR_CREATE_USER -> R.string.data_rsp_error_create_user
        BaseResponse.ERROR_CREATE_GROUP -> R.string.data_rsp_error_create_group
        BaseResponse.ERROR_CREATE_MESSAGE -> R.string.data_rsp_error_create_message
        BaseResponse.ERROR_PARAMETERS -> R.string.data_rsp_error_parameters
        BaseResponse.ERROR_PARAMETERS_EXIST_ACCOUNT -> R.string.data_rsp_error_parameters_exist_account
        BaseResponse.ERROR_PARAMETERS_EXIST_NAME -> R.string.data_rsp_error_parameters_exist_name
        BaseResponse.ERROR_ACCOUNT_TOKEN -> R.string.data_rsp_error_account_token
        BaseResponse.ERROR_ACCOUNT_LOGIN -> R.string.data_rsp_error_account_login
        BaseResponse.ERROR_ACCOUNT_REGISTER -> R.string.data_rsp_error_account_register
        BaseResponse.ERROR_ACCOUNT_NO_PERMISSION -> R.string.data_rsp_error_account_no_permission
        BaseResponse.ERROR_UNKNOWN -> R.string.data_rsp_error_unknown
        else -> R.string.data_rsp_error_unknown
    }
    return StringUtils.getString(strRes)
}