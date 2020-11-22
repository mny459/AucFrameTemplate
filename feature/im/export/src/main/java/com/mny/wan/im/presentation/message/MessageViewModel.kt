package com.mny.wan.im.presentation.message

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.http.Result
import com.mny.wan.im.data.api.message.MsgCreateModel
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.data.db.entity.Group
import com.mny.wan.im.data.db.entity.Message
import com.mny.wan.im.data.db.entity.UserEntity
import com.mny.wan.im.domain.usecase.GroupUseCase
import com.mny.wan.im.domain.usecase.MessageUseCase
import com.mny.wan.im.domain.usecase.UserUseCase
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MessageViewModel @ViewModelInject constructor(private val mUserUseCase: UserUseCase,
                                                    private val mMessageUseCase: MessageUseCase,
                                                    private val mGroupUseCase: GroupUseCase
) : BaseViewModel<MessageViewModel.State, MessageViewModel.Action>(State()) {
    val mChatAllMessages by lazy {
        mMessageUseCase.getMessagesByReceiver(state.receiverId, state.receiverType == Message.RECEIVER_TYPE_GROUP)
    }

    val mGroupMembers by lazy {
        mGroupUseCase.loadAllGroupMembers(viewModelScope, state.group!!)
    }

    fun init(receiverId: String, receiverType: Int) {
        if (receiverType == Message.RECEIVER_TYPE_NONE) {
            initUser(receiverId)
        } else {
            initGroup(receiverId)
        }

    }

    private fun initGroup(receiverId: String) {
        viewModelScope.launch {
            when (val group = mGroupUseCase.find(receiverId)) {
                is Result.Success -> {
                    group.data?.apply {
                        sendAction(Action.Init(receiverId, Message.RECEIVER_TYPE_GROUP, user = null, group = this))
                    }
                }
                is Result.Error -> {
                    sendAction(Action.Init(receiverId, Message.RECEIVER_TYPE_GROUP, user = null, group = null))
                }
                else -> {
                }
            }
        }
    }

    private fun initUser(receiverId: String) {
        viewModelScope.launch {
            when (val user = mUserUseCase.findUser(receiverId)) {
                is Result.Success -> {
                    user.data?.apply {
                        sendAction(Action.Init(receiverId, Message.RECEIVER_TYPE_NONE, user = this.buildUser(), group = null))
                    }
                }
                is Result.Error -> {
                    sendAction(Action.Init(receiverId, Message.RECEIVER_TYPE_NONE, user = null, group = null))
                }
                else -> {
                }
            }
        }
    }

    data class State(
            val receiverId: String = "",
            val receiverType: Int = Message.RECEIVER_TYPE_NONE,
            val loading: Boolean = false,
            val user: UserEntity? = null,
            val group: Group? = null,
            val errorMsg: String = ""
    ) : BaseState

    sealed class Action : BaseAction {
        class Init(val receiverId: String, val receiverType: Int, val user: UserEntity?, val group: Group?) : Action()
        object Start : Action()
        class Complete(val userCard: UserCard? = null,
                       val errorMsg: String = "") : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.Start -> state.copy(loading = true, user = null, errorMsg = "")
        is Action.Complete -> state.copy(loading = false, errorMsg = viewAction.errorMsg)
        is Action.Init -> State(receiverId = viewAction.receiverId,
                receiverType = viewAction.receiverType,
                user = viewAction.user,
                group = viewAction.group)
    }

    fun pushText(content: String) {

        viewModelScope.launch {
            // 构建一个新的消息
            val model: MsgCreateModel = MsgCreateModel.Builder()
                    .receiver(state.receiverId, state.receiverType)
                    .content(content, Message.TYPE_STR)
                    .build()
            // 进行网络发送
            mMessageUseCase.push(model)
                    .catch { error ->
                        LogUtils.e("发送消息失败 $error, ${error.printStackTrace()}")
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        LogUtils.d("发送消息 状态 $it(创建 = ${Message.STATUS_CREATED}，成功 = ${Message.STATUS_DONE}，失败 = ${Message.STATUS_FAILED}) ${Thread.currentThread().name}")
                    }

        }
    }
}