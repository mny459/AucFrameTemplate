package com.mny.wan.pkg.presentation.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.http.MojitoResult
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.pkg.base.BaseArticleViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val mUseCase: ArticleUseCase,
    private val mSearchUseCase: SearchUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseArticleViewModel<SearchViewModel.ViewState, SearchViewModel.Action>(
        ViewState(),
        mSavedStateHandle
    ) {

    data class ViewState(
        val isLoading: Boolean = false
    ) : BaseState

    sealed class Action : BaseAction {
        object LoginStart : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true)
    }

    override fun getArticlePageSource(search: Any): PagingSource<Int, BeanArticle> =
        mUseCase.searchArticlePageSource(search)

    val mHotKey = MutableLiveData<MutableList<BeanHotKey>>()

    fun fetchHotKey() {
        viewModelScope.launch {
            mSearchUseCase.fetchHotKey()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> mHotKey.postValue(it.data)
                        is MojitoResult.Error -> {
                        }
                        MojitoResult.Loading -> {
                        }
                    }
                }
        }
    }

    fun search(name: String) {
        LogUtils.d("ArticlePageSource fetchArticleList")
        mClearListCh.offer(Unit)
        mSavedStateHandle.set(KEY_ARTICLE, name)
    }
}