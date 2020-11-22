package com.mny.wan.im.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.mny.wan.http.Result
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.domain.usecase.FollowUseCase
import com.mny.wan.mvvm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowViewModel @ViewModelInject constructor(private val mFollowUseCase: FollowUseCase)
    : BaseViewModel<FollowViewModel.State, FollowViewModel.Action>(State()) {

    fun follow(userId: String) {
        sendAction(Action.Start)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                mFollowUseCase.followUser(userId)
            }
            when (result) {
                is Result.Success -> {
                    sendAction(Action.Complete(userCard = result.data))
                }
                is Result.Error -> {
                    sendAction(Action.Complete(errorMsg = result.exception.message ?: ""))
                }
                else -> {
                }
            }
        }
    }

    data class State(
            val loading: Boolean = false,
            val userCard: UserCard? = null,
            val errorMsg: String = ""
    ) : BaseState

    sealed class Action : BaseAction {
        object Start : Action()
        class Complete(val userCard: UserCard? = null,
                       val errorMsg: String = "") : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.Start -> state.copy(loading = true, userCard = null, errorMsg = "")
        is Action.Complete -> state.copy(loading = false, userCard = viewAction.userCard, errorMsg = viewAction.errorMsg)
    }
}