package com.mny.wan.pkg.presentation.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.mny.wan.pkg.domain.usecase.UserUseCase
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.mojito.http.MojitoResult
import com.mny.wan.pkg.presentation.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class LoginViewModel @ViewModelInject constructor(
    private val mAppViewModel: AppViewModel,
    private val mUserUseCase: UserUseCase
) :
    BaseViewModel<LoginViewModel.ViewState, LoginViewModel.Action>(ViewState()) {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            delay(1000)
            mUserUseCase.login(username, password)
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            mAppViewModel.updateUserInfo(it.data)
                            sendAction(Action.LoginSuccess)
                        }
                        is MojitoResult.Error -> {
                            sendAction(Action.LoginFailure("${it.exception.message}"))
                        }
                        MojitoResult.Loading -> {
                            sendAction(Action.LoginStart)
                        }
                    }
                }
        }
    }

    fun register(
        username: String,
        password: String,
        rePassword: String
    ) {
        viewModelScope.launch {
            mUserUseCase.register(username, password, rePassword)
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            mAppViewModel.updateUserInfo(it.data)
                            sendAction(Action.LoginSuccess)
                        }
                        is MojitoResult.Error -> {
                            sendAction(Action.LoginFailure("${it.exception.message}"))
                        }
                        MojitoResult.Loading -> {
                            sendAction(Action.LoginStart)
                        }
                    }
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
        Action.LoginStart -> state.copy(
            isLoading = true,
            loginSuccess = false,
            isError = false,
            errorMsg = ""
        )
        Action.LoginSuccess -> state.copy(
            isLoading = false,
            loginSuccess = true,
            isError = false,
            errorMsg = ""
        )
        is Action.LoginFailure -> state.copy(
            isLoading = false,
            loginSuccess = false,
            isError = true,
            errorMsg = viewAction.errorMsg
        )
    }

}
