package com.mny.mojito.im.presentation.group

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.mny.mojito.im.domain.usecase.GroupUseCase
import com.mny.mojito.im.presentation.group.adapter.GroupMember
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel

class GroupMemberViewModel @ViewModelInject constructor(private val mGroupUseCase: GroupUseCase)
    : BaseViewModel<GroupMemberViewModel.State, GroupMemberViewModel.Action>(State()) {

    val mMembers by lazy { mGroupUseCase.loadAllGroupMembers(state.groupId).switchMap {
        val map = it.map { member -> GroupMember(member.user!!) }
        MutableLiveData(map)
    } }

    data class State(
            val groupId: String = "",
            val loading: Boolean = false,
            val isAdmin: Boolean = false,
            val errorMsg: String = ""
    ) : BaseState

    sealed class Action : BaseAction {
        class Init(val groupId: String, val isAdmin: Boolean) : Action()
        object Start : Action()
        class Complete(val errorMsg: String = "") : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.Start -> state.copy(loading = true, errorMsg = "")
        is Action.Complete -> state.copy(loading = false, errorMsg = viewAction.errorMsg)
        is Action.Init -> State(groupId = viewAction.groupId, isAdmin = viewAction.isAdmin)
    }


    fun init(groupId: String, admin: Boolean) {
        sendAction(Action.Init(groupId, admin))

    }

    fun refreshMembers() {
//        viewModelScope.launch {
//            mGroupUseCase.loadAllGroupMembers(groupId)
//        }
    }

    fun submit() {

    }
}