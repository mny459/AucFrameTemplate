package com.mny.wan.pkg.presentation.main.qa

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mny.mojito.mvvm.*
import com.mny.wan.pkg.data.UrlManager
import com.mny.wan.pkg.data.local.entity.UiQaArticle
import com.mny.wan.pkg.domain.paging.QAArticlePageSource
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.CollectUseCase
import com.mny.wan.pkg.domain.usecase.QAUseCase
import com.mny.wan.pkg.presentation.article.BaseArticleViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class QAViewModel @ViewModelInject constructor(
    private val articleUseCase: ArticleUseCase,
    private val collectUseCase: CollectUseCase,
    @Assisted val mSavedStateHandle: SavedStateHandle
) :
    BaseArticleViewModel<BaseViewState, BaseViewAction>(
        articleUseCase,
        collectUseCase,
        BaseViewState(),
    ) {

    override fun onReduceState(viewAction: BaseViewAction): BaseViewState =
        onReduceState(viewAction)

    override fun url(page: Int): String = UrlManager.qaArticleList(page)
    override val firstPage: Int
        get() = 1
}