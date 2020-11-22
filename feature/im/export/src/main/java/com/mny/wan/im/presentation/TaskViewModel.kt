package com.mny.wan.im.presentation

import androidx.hilt.lifecycle.ViewModelInject
import com.mny.wan.im.domain.usecase.LoginUseCase
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseViewModel
import com.mny.wan.mvvm.BaseState

/**
 * Desc:
 */
internal class TaskViewModel @ViewModelInject constructor(private val mLoginUseCase: LoginUseCase)
    : BaseViewModel<TaskViewModel.State, TaskViewModel.Action>(State()) {
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