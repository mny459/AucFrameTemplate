package com.mny.mojito.im.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.mny.mojito.im.data.db.entity.Session
import com.mny.mojito.im.domain.usecase.GroupUseCase
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel


class GroupViewModel @ViewModelInject constructor(private val mGroupUseCase: GroupUseCase)
    : BaseViewModel<GroupViewModel.State, GroupViewModel.Action>(State()) {

    val mGroups by lazy { mGroupUseCase.loadAllGroups(viewModelScope) }

    override fun onLoadData() {
        super.onLoadData()
        mGroupUseCase.loadAllGroups(viewModelScope)
    }

    data class State(
            val loading: Boolean = false,
            val sessions: List<Session> = emptyList(),
            val errorMsg: String = ""
    ) : BaseState

    sealed class Action : BaseAction {
        object Start : Action()
        class Complete(val sessions: List<Session> = emptyList(),
                       val errorMsg: String = "") : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.Start -> state.copy(loading = true, errorMsg = "")
        is Action.Complete -> state.copy(loading = false, sessions = viewAction.sessions, errorMsg = viewAction.errorMsg)
    }

}