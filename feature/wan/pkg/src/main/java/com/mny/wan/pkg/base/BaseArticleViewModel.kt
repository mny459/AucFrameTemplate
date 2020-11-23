package com.mny.wan.pkg.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class BaseArticleViewModel<ViewState : BaseState, ViewAction : BaseAction>(
    initialState: ViewState,
    protected val mSavedStateHandle: SavedStateHandle
) :
    BaseViewModel<ViewState, ViewAction>(initialState) {
    companion object {
         const val KEY_ARTICLE = "articles"
        protected const val DEFAULT_PAGE = 0
    }

    protected val mClearListCh = Channel<Unit>(Channel.CONFLATED)

    // PagingData: 负责通知DataSource何时获取数据，以及如何获取数据
    val mArticleList: Flow<PagingData<BeanArticle>> = flowOf(
        mClearListCh.receiveAsFlow().map { PagingData.empty<BeanArticle>() },
        mSavedStateHandle.getLiveData<Any>(KEY_ARTICLE)
            .asFlow()
            .flatMapLatest {
                LogUtils.d("ArticlePageSource mArticleList = ${it}")
                Pager(
                    config = PagingConfig(
                        // 每页显示的数据的大小。对应 PagingSource 里 LoadParams.loadSize
                        pageSize = 20,
//                        // 预刷新的距离，距离最后一个 item 多远时加载数据
                        prefetchDistance = 4,
//                        // 初始化加载数量，默认为 pageSize * 3
                        initialLoadSize = 20,
//                        // 一次应在内存中保存的最大数据
                        maxSize = 200
                    )
                ) {
                    getArticlePageSource(it)
                }.flow
            }
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    override fun onLoadData() {
        super.onLoadData()
        fetchArticleList()
    }

    open fun fetchArticleList() {
        LogUtils.d("ArticlePageSource fetchArticleList")
        mClearListCh.offer(Unit)
        mSavedStateHandle.set(KEY_ARTICLE, DEFAULT_PAGE)
    }

    abstract fun getArticlePageSource(keyArticle: Any): PagingSource<Int, BeanArticle>
}