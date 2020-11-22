package com.mny.wan.pkg.presentation.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.mny.wan.pkg.domain.usecase.LoginUseCase
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.local.UserInfoManager
import kotlinx.coroutines.launch

internal class LoginViewModel @ViewModelInject constructor(private val mLoginUseCase: LoginUseCase)
    : BaseViewModel<LoginViewModel.ViewState, LoginViewModel.Action>(ViewState()) {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            sendAction(Action.LoginStart)
            when (val result = mLoginUseCase.login(username, password)) {
                is MojitoResult.Success -> {
                    result.data?.apply {
                        UserInfoManager.saveUserInfo(this)
                    }
                    sendAction(Action.LoginSuccess)
                }
                is MojitoResult.Error -> {
                    sendAction(Action.LoginFailure("${result.exception.message}"))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (val result = mLoginUseCase.logout()) {
//                is Result.Success -> {
//                    ToastUtils.showShort("登出成功")
//                }
//                is Result.Error -> {
//                    ToastUtils.showShort("${result.exception.message}")
//                }
            }
        }
    }

    internal data class ViewState(
            val isLoading: Boolean = false,
            val loginSuccess: Boolean = false,
            val isError: Boolean = false,
            val errorMsg: String = ""
    ) : BaseState

    internal sealed class Action : BaseAction {
        object LoginStart : Action()
        object LoginSuccess : Action()
        class LoginFailure(val errorMsg: String) : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true, loginSuccess = false, isError = false, errorMsg = "")
        Action.LoginSuccess -> state.copy(isLoading = false, loginSuccess = true, isError = false, errorMsg = "")
        is Action.LoginFailure -> state.copy(isLoading = false, loginSuccess = false, isError = true, errorMsg = viewAction.errorMsg)
    }

}
