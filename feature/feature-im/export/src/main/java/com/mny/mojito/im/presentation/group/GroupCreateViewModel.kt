package com.mny.mojito.im.presentation.group

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.http.Result
import com.mny.mojito.im.R
import com.mny.mojito.im.data.api.model.GroupCreateModel
import com.mny.mojito.im.domain.usecase.ContactUseCase
import com.mny.mojito.im.domain.usecase.GroupUseCase
import com.mny.mojito.im.presentation.group.adapter.GroupMember
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import kotlinx.coroutines.launch

class GroupCreateViewModel @ViewModelInject constructor(private val mGroupUseCase: GroupUseCase, private val mContactUseCase: ContactUseCase)
    : BaseViewModel<GroupCreateViewModel.State, GroupCreateViewModel.Action>(State()) {

    override fun onLoadData() {
        super.onLoadData()
        viewModelScope.launch {
            when (val contacts = mContactUseCase.getContacts()) {
                is Result.Success -> {
                    val groupMembers = mutableListOf<GroupMember>()
                    contacts.data?.forEach {
                        groupMembers.add(GroupMember(it.buildUser()))
                    }
                    sendAction(Action.LoadContact(groupMembers))
                }
                is Result.Error -> {

                }
                else -> {
                }
            }
        }
    }

    data class State(
            val groupMembers: MutableList<GroupMember> = mutableListOf<GroupMember>(),
            val loading: Boolean = false,
            val errorMsg: String = "",
            val users: HashSet<String> = hashSetOf<String>()
    ) : BaseState

    sealed class Action : BaseAction {
        object Start : Action()
        class Complete(val errorMsg: String = "") : Action()
        class LoadContact(val groupMembers: MutableList<GroupMember>) : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.Start -> state.copy(loading = true, errorMsg = "")
        is Action.Complete -> state.copy(loading = false, errorMsg = viewAction.errorMsg)
        is Action.LoadContact -> state.copy().apply {
            groupMembers.clear()
            groupMembers.addAll(viewAction.groupMembers)
        }
    }

    /**
     * 创建群
     */
    fun create(name: String, desc: String, picture: String) {
        // 判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(picture) || state.users.size == 0) {
            ToastUtils.showShort(R.string.label_group_create_invalid)
            return
        }

        /// TODO 上传图片
        val url: String = picture//uploadPicture(picture)
//        if (TextUtils.isEmpty(url)) return
        // 进行网络请求
        val model = GroupCreateModel(name, desc, url, state.users)
        viewModelScope.launch {
            when (val result = mGroupUseCase.createGroup(model)) {
                is Result.Success -> {
                    ToastUtils.showShort("创建群成功")
                    sendCompleteAction("")
                }
                is Result.Error -> {
                    ToastUtils.showShort("${result.exception.message}")
                }
                else -> {
                }
            }
        }
    }

    fun onSelectUserChanged(userId: String, selected: Boolean) {
        if (selected) {
            state.users.add(userId)
        } else {
            state.users.remove(userId)
        }
    }
}