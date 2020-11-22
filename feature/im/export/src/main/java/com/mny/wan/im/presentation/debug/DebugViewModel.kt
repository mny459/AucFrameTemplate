package com.mny.wan.im.presentation.debug

import androidx.hilt.lifecycle.ViewModelInject
import com.mny.wan.im.domain.usecase.UserUseCase
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel

class DebugViewModel @ViewModelInject constructor(private val mUserCase: UserUseCase)
    : BaseViewModel<DebugViewModel.State, DebugViewModel.Action>(State()) {

    data class State(
            val isLoading: Boolean = false
    ) : BaseState

    sealed class Action : BaseAction {
        object LoginStart : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true)
    }

    fun createUser() {

    }

    fun createContact() {

    }

    fun createGroup() {

    }

    fun showSendBtn() {

    }

}