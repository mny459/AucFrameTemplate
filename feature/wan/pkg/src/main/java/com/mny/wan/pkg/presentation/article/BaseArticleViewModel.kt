package com.mny.wan.pkg.presentation.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.http.doSuccess
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.CollectUseCase
import com.mny.wan.pkg.event.CollectEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseArticleViewModel<ViewState : BaseState, ViewAction : BaseAction>(
    private val mArticleUseCase: ArticleUseCase,
    private val mUseCase: CollectUseCase,
    initialState: ViewState
) :
    BaseViewModel<ViewState, ViewAction>(initialState) {

    private val mPageState = MutableLiveData(PageState(curPage = firstPage, refresh = true))
    val pageState: LiveData<PageState> = mPageState
    private val mArticlesLiveData = MutableLiveData<MutableList<BeanArticle>>()
    val articles: LiveData<MutableList<BeanArticle>> = mArticlesLiveData
    private val mArticles = mutableListOf<BeanArticle>()
    private var mCurPage = 0

    open fun refresh() {
        mCurPage = firstPage
        loadArticles()
    }

    fun collect(id: Int, collect: Boolean) {
        if (collect) {
            collectArticle(id)
        } else {
            cancelCollect(id)
        }
    }

    private fun collectArticle(id: Int) {
        viewModelScope.launch {
            mUseCase.collect(id)
                .collect {
                    it.doSuccess {
                        CollectEvent.collect(id)
                    }
                }
        }
    }

    private fun cancelCollect(id: Int) {
        viewModelScope.launch {
            mUseCase.cancelCollect(id)
                .collect {
                    it.doSuccess {
                        CollectEvent.cancelCollect(id)
                    }
                }
        }
    }

    fun loadMore() {
        if (mPageState.value?.loadNoMore == false) {
            mCurPage += 1
            loadArticles()
        }
    }

    private fun loadArticles() {
        viewModelScope.launch {
            mArticleUseCase.articlesByUrl(url(mCurPage))
                .collect {
                    var success = true
                    var loadNoMore = false
                    var errorMsg = ""
                    var refresh = true
                    when (it) {
                        MojitoResult.Loading -> {
                            success = false
                        }
                        is MojitoResult.Success -> {
                            success = true
                            it.data?.apply {
                                mCurPage = curPage
                                refresh = offset == 0
                                loadNoMore = over
                                if (!articles.isNullOrEmpty()) {
                                    if (refresh) {
                                        mArticles.clear()
                                        mArticles.addAll(articles)
                                    } else {
                                        mArticles.addAll(articles)
                                    }
                                    mArticlesLiveData.postValue(mArticles.toMutableList())
                                }
                            }
                        }
                        is MojitoResult.Error -> {
                            success = false
                            errorMsg = it.exception.message ?: ""
                        }
                    }
                    if (it != MojitoResult.Loading) {
                        val curPage: Int = mCurPage
                        mPageState.postValue(
                            PageState(
                                curPage = curPage,
                                success = success,
                                loadNoMore = loadNoMore,
                                errorMsg = errorMsg,
                                refresh = refresh
                            )
                        )
                    }
                }
        }
    }

    abstract fun url(page: Int): String
    abstract val firstPage: Int

    /**
     * [curPage] 当前页
     * [success] 操作成功
     * [loadNoMore] 是否还有更多数据
     * [errorMsg] 加载失败的错误信息
     * [refresh] 是否是刷新操作
     */
    data class PageState(
        val curPage: Int = 1,
        val success: Boolean = true,
        val loadNoMore: Boolean = false,
        val errorMsg: String = "",
        val refresh: Boolean = true,
    )

}