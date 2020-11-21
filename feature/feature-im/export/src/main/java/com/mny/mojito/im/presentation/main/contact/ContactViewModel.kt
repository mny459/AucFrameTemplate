package com.mny.mojito.im.presentation.main.contact

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.domain.usecase.ContactUseCase
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import kotlinx.coroutines.launch

class ContactViewModel @androidx.hilt.lifecycle.ViewModelInject constructor(private val mUseCase: ContactUseCase)
    : BaseViewModel<ContactViewModel.State, ContactViewModel.Action>(State()) {
    var loadFromLocal: Boolean = true

     val contacts: LiveData<List<UserEntity>> = mUseCase.getContactsFromLocal().map {
        LogUtils.d("ContactViewModel $it")
        sendAction(Action.Complete(users = it, isFromLocal = loadFromLocal))
        it
    }

    override fun onLoadData() {
        super.onLoadData()
        // 加载网络数据
        loadFromLocal = false
        viewModelScope.launch {
            mUseCase.getContacts()
        }
    }

    data class State(
            val isLoading: Boolean = false,
            val errorMsg: String? = null,
            val users: MutableList<UserEntity> = mutableListOf(),
            val isFromLocal: Boolean = true
    ) : BaseState

    sealed class Action : BaseAction {
        object Start : Action()
        class Complete(val users: List<UserEntity> = emptyList(),
                       val errorMsg: String? = null,
                       val isFromLocal: Boolean = true) : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.Start -> state.copy(isLoading = true, errorMsg = null).apply { users.clear() }
        is Action.Complete -> state.copy(isLoading = false,
                errorMsg = viewAction.errorMsg,
                isFromLocal = viewAction.isFromLocal).apply {
            this.users.clear()
            this.users.addAll(viewAction.users)
        }
    }
}