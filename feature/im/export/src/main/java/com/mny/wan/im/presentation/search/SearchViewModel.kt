package com.mny.wan.im.presentation.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mny.wan.http.Result
import com.mny.wan.im.data.card.GroupCard
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.domain.paging.SearchUserPagingSource
import com.mny.wan.im.domain.usecase.GroupUseCase
import com.mny.wan.im.domain.usecase.SearchUserUseCase
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Desc:
 */
class SearchViewModel @ViewModelInject constructor(private val mUseCase: SearchUserUseCase,
                                                   private val mGroupUseCase: GroupUseCase,
                                                   private val mPageData: SearchUserPagingSource,
                                                   @Assisted private val savedStateHandle: SavedStateHandle)
    : BaseViewModel<SearchViewModel.State, SearchViewModel.Action>(State()) {
    companion object {
        private const val KEY_SEARCH = "search"
        private const val DEFAULT_SEARCH = ""
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    val mUsers = flowOf(
            clearListCh.receiveAsFlow().map { PagingData.empty<UserCard>() },
            savedStateHandle.getLiveData<String>(KEY_SEARCH)
                    .asFlow()
                    .flatMapLatest {
                        Pager(
                                PagingConfig(
                                        // 每页显示的数据的大小。对应 PagingSource 里 LoadParams.loadSize
                                        pageSize = 20,
                                        // 预刷新的距离，距离最后一个 item 多远时加载数据
                                        prefetchDistance = 3,
                                        // 初始化加载数量，默认为 pageSize * 3
                                        initialLoadSize = 60,
                                        // 一次应在内存中保存的最大数据
                                        maxSize = 200
                                ), it
                        ) {
                            mPageData
                        }.flow
                    }
                    .cachedIn(viewModelScope)
    ).flattenMerge(2)



    data class State(
            val isLoading: Boolean = false,
            val complete: Boolean = false,
            val errorMsg: String? = null,
            val users: MutableList<UserCard> = mutableListOf<UserCard>(),
            val groups: MutableList<GroupCard> = mutableListOf<GroupCard>()
    ) : BaseState

    sealed class Action : BaseAction {
        object SearchStart : Action()
        class SearchComplete(val users: List<UserCard> = emptyList(), val groups: List<GroupCard> = emptyList(), val errorMsg: String? = null) : Action()
    }

    override fun onReduceState(viewAction: Action): State = when (viewAction) {
        Action.SearchStart -> state.copy(isLoading = true, complete = true)
        is Action.SearchComplete -> state.copy(isLoading = false, complete = true, errorMsg = viewAction.errorMsg).apply {
            users.clear()
            users.addAll(viewAction.users)
            groups.clear()
            groups.addAll(viewAction.groups)
        }
    }

    private fun shouldSearch(query: String) = savedStateHandle.get<String>(KEY_SEARCH) != query

    /**
     * 搜索群或者用户
     * [query] 搜索的文字
     */
    fun searchUsers(query: String) {
        if (!shouldSearch(query)) return
        clearListCh.offer(Unit)
        savedStateHandle.set(KEY_SEARCH, query)
//        sendAction(Action.SearchStart)
        // TODO 对于这个搜索需要可取消
        viewModelScope.launch {
//            mUseCase.searchUser(query)
//                    .collect { result ->
//                        when (result) {
//                            is Result.Success -> {
//                                sendAction(Action.SearchComplete(users = result.data
//                                        ?: emptyList()))
//                            }
//                            is Result.Error -> {
////                    sendAction(Action.SearchComplete(errorMsg = result.exception.message))
//                                sendAction(Action.SearchComplete(users = fakeUsers()))
//                            }
//                            else -> {
//                            }
//                        }
//                    }
        }
    }


    /**
     * 搜索群或者用户
     * [query] 搜索的文字
     */
    fun searchGroups(query: String) {
        sendAction(Action.SearchStart)
        // TODO 对于这个搜索需要可取消
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                mGroupUseCase.searchGroup(query)
            }

            when (result) {
                is Result.Success -> {
                    sendAction(Action.SearchComplete(groups = result.data ?: emptyList()))
                }
                is Result.Error -> {
//                    sendAction(Action.SearchComplete(errorMsg = result.exception.message))
                    sendAction(Action.SearchComplete(groups = fakeGroups()))
                }
                else -> {
                }
            }
        }
    }

    fun fakeUsers(): List<UserCard> {
        val result = mutableListOf<UserCard>()
        result.add(UserCard("1", name = "小明"))
        result.add(UserCard("2", name = "小明"))
        result.add(UserCard("3", name = "小明"))
        result.add(UserCard("4", name = "小明"))
        return result
    }

    fun fakeGroups(): List<GroupCard> {
        val result = mutableListOf<GroupCard>()
        result.add(GroupCard("1", name = "小明群", picture = "", desc = "fsh", ownerId = "0", joinAt = null, modifyAt = null))
        result.add(GroupCard("2", name = "小明群", picture = "", desc = "fsh", ownerId = "0", joinAt = null, modifyAt = null))
        result.add(GroupCard("3", name = "小明群", picture = "", desc = "fsh", ownerId = "0", joinAt = null, modifyAt = null))
        result.add(GroupCard("4", name = "小明群", picture = "", desc = "fsh", ownerId = "0", joinAt = null, modifyAt = null))
        return result
    }
}