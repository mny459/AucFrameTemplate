package com.mny.wan.im.presentation.account

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.mny.wan.im.R
import com.mny.wan.im.domain.usecase.LoginUseCase
import com.mny.wan.http.Result
import com.mny.wan.mvvm.BaseViewAction
import com.mny.wan.mvvm.BaseViewModel
import com.mny.wan.mvvm.BaseViewState
import kotlinx.coroutines.launch

/**
 * Desc:
 */
internal class RegisterViewModel @ViewModelInject constructor(private val mLoginUseCase: LoginUseCase)
    : BaseViewModel<BaseViewState, BaseViewAction>(BaseViewState()) {

    fun register(phone: String, name: String, password: String) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            sendCompleteAction(StringUtils.getString(R.string.data_account_login_invalid_parameter))
            return
        }

        // 校验
        if (!RegexUtils.isMobileExact(phone)) {
            // 提示
            sendCompleteAction(StringUtils.getString(R.string.data_account_register_invalid_parameter_mobile))
            return
        } else if (name.length < 2) {
            // 姓名需要大于2位
            sendCompleteAction(StringUtils.getString(R.string.data_account_register_invalid_parameter_name))
            return
        } else if (password.length < 6) {
            // 密码需要大于6位
            sendCompleteAction(StringUtils.getString(R.string.data_account_register_invalid_parameter_password))
            return
        }

        viewModelScope.launch {
            sendLoadingAction()
            when (val result = mLoginUseCase.register(phone, name, password)) {
                is Result.Success -> {
                    sendCompleteAction()
                }
                is Result.Error -> {
                    sendCompleteAction()
                }
            }
        }
    }

    override fun onReduceState(viewAction: BaseViewAction): BaseViewState = onReduceViewState(viewAction)
}