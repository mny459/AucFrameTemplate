package com.mny.wan.pkg.presentation.main.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewAction
import com.mny.mojito.mvvm.BaseViewState
import com.mny.wan.pkg.data.UrlManager
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.CollectUseCase
import com.mny.wan.pkg.domain.usecase.HomeUseCase
import com.mny.wan.pkg.presentation.article.BaseArticleViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class HomeViewModel @ViewModelInject constructor(
    private val mUseCase: HomeUseCase,
    private val articleUseCase: ArticleUseCase,
    private val collectUseCase: CollectUseCase,
    @Assisted val mSavedStateHandle: SavedStateHandle
) : BaseArticleViewModel<BaseViewState, BaseViewAction>(
    articleUseCase,
    collectUseCase,
    BaseViewState(),
) {

    val mBannerList = MutableLiveData<List<BeanBanner>>()
    val mTopArticles = MutableLiveData<List<BeanArticle>>()
    override fun refresh() {
        super.refresh()
        fetchBannerList()
        fetchTopArticles()
    }

    private fun fetchTopArticles() {
        viewModelScope.launch {
            mUseCase.fetchTopArticles()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            it.data.apply {
                                mTopArticles.postValue(this)
                            }
                        }
                        is MojitoResult.Error -> {
                        }
                        MojitoResult.Loading -> {
                        }
                    }
                }
        }
    }

    private fun fetchBannerList() {
        viewModelScope.launch {
            mUseCase.fetchBannerList()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            it.data.apply {
                                mBannerList.postValue(this)
                            }
                        }
                        is MojitoResult.Error -> {
                        }
                        MojitoResult.Loading -> {
                        }
                    }
                }
        }
    }

    override fun onReduceState(viewAction: BaseViewAction): BaseViewState =
        onReduceState(viewAction)

    override fun url(page: Int): String = UrlManager.urlHomeArticleList(page)
    override val firstPage: Int
        get() = 0
}