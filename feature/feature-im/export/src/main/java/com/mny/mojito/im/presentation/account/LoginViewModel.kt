package com.mny.mojito.im.presentation.account

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.mny.mojito.im.R
import com.mny.mojito.im.domain.usecase.LoginUseCase
import com.mny.mojito.http.Result
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.mojito.mvvm.BaseState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class LoginViewModel @ViewModelInject constructor(private val mLoginUseCase: LoginUseCase)
    : BaseViewModel<LoginViewModel.State, LoginViewModel.Action>(State()) {

    fun login(phone: String, password: String) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            sendCompleteAction(StringUtils.getString(R.string.data_account_login_invalid_parameter))
            return
        }
        viewModelScope.launch {
            mLoginUseCase.login(phone, password)
                    .collect {
                        when (it) {
                            is Result.Success -> sendCompleteAction()
                            is Result.Error -> sendCompleteAction("${it.exception.message}")
                            Result.Loading -> sendLoadingAction()
                            else -> {
                            }
                        }
                    }
        }
    }

    fun logout() {

    }

    internal data class State(
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

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true, loginSuccess = false, isError = false, errorMsg = "")
        Action.LoginSuccess -> state.copy(isLoading = false, loginSuccess = true, isError = false, errorMsg = "")
        is Action.LoginFailure -> state.copy(isLoading = false, loginSuccess = false, isError = true, errorMsg = viewAction.errorMsg)
    }

}
