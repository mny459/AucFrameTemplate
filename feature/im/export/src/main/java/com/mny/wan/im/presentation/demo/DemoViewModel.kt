package com.mny.wan.im.presentation.demo

import androidx.hilt.lifecycle.ViewModelInject
import com.mny.wan.im.domain.usecase.LoginUseCase
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel

/**
 * Desc:
 */
internal class DemoViewModel @ViewModelInject constructor(private val mLoginUseCase: LoginUseCase)
    : BaseViewModel<DemoViewModel.State, DemoViewModel.Action>(State()) {

    internal data class State(
            val isLoading: Boolean = false
    ) : BaseState

    internal sealed class Action : BaseAction {
        object LoginStart : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true)
    }

}