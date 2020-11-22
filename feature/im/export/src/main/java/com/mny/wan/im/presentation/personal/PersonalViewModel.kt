package com.mny.wan.im.presentation.personal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.mny.wan.http.Result
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.domain.usecase.FollowUseCase
import com.mny.wan.im.domain.usecase.UserUseCase
import com.mny.wan.mvvm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Desc:
 */
class PersonalViewModel @ViewModelInject constructor(private val mUseCase: UserUseCase, private val mFollowUseCase: FollowUseCase) :
        BaseViewModel<PersonalViewModel.State, PersonalViewModel.Action>(State(loading = false)) {

    fun getUserInfo() {
        if (state.userId.isEmpty()) return
        sendAction(BaseViewAction.Loading)
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { mUseCase.findUser(state.userId) }) {
                is Result.Success -> {
                    sendAction(Action.OnComplete(user = result.data))
                }
                is Result.Error -> {
                    sendAction(Action.OnComplete(errMsg = result.exception.message))
                }
                else -> {
                }
            }
        }
    }

    fun follow() {
        sendAction(BaseViewAction.Loading)
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { mFollowUseCase.followUser(state.userId) }) {
                is Result.Success -> {
                    sendAction(Action.OnComplete(user = result.data))
                }
                is Result.Error -> {
                    sendAction(Action.OnComplete(errMsg = result.exception.message))
                }
                else -> {
                }
            }
        }
    }

    fun initUserId(userId: String) {
        sendAction(Action.InitUserId(userId))
    }

    data class State(
            val loading: Boolean,
            val userId: String = "",
            val user: UserCard? = null,
            val allowSayHello: Boolean = false,
            val isFollow: Boolean = false,
            val errorMsg: String? = ""
    ) : BaseState

    sealed class Action : BaseAction {
        object Loading : Action()
        class InitUserId(val userId: String) : Action()
        class OnComplete(val user: UserCard? = null, val errMsg: String? = "") : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        is Action.InitUserId -> state.copy(userId = viewAction.userId)
        Action.Loading -> state.copy(loading = true, user = null, errorMsg = "")
        is Action.OnComplete -> {
            var allowSayHello = false
            var isFollow = false
            val user = viewAction.user ?: state.user
            if (user != null) {
                isFollow = user.isSelf() || user.isFollow
                allowSayHello = !user.isSelf() && user.isFollow
            }
            state.copy(loading = false, user = viewAction.user, errorMsg = viewAction.errMsg, allowSayHello = allowSayHello, isFollow = isFollow)
        }
    }

    fun refreshUser(userCard: UserCard) {
        sendAction(Action.OnComplete(user = userCard))
    }

}