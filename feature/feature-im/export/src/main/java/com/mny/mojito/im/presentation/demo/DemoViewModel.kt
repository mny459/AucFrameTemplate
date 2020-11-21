package com.mny.mojito.im.presentation.demo

import androidx.hilt.lifecycle.ViewModelInject
import com.mny.mojito.im.domain.usecase.LoginUseCase
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel

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