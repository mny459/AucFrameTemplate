package com.mny.mojito.im.presentation

import androidx.hilt.lifecycle.ViewModelInject
import com.mny.mojito.im.domain.usecase.LoginUseCase
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.mojito.mvvm.BaseState

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