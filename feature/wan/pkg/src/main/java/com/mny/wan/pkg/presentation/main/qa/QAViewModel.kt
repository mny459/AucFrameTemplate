package com.mny.wan.pkg.presentation.main.qa

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingSource
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.pkg.base.BaseArticleViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.domain.usecase.ArticleUseCase

class QAViewModel @ViewModelInject constructor(
    private val mUseCase: ArticleUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseArticleViewModel<QAViewModel.ViewState, QAViewModel.Action>(
        ViewState(),
        mSavedStateHandle
    ) {

    data class ViewState(
        val isLoading: Boolean = false,
    ) : BaseState

    sealed class Action : BaseAction {
        object LoginStart : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true)
    }

    override fun getArticlePageSource(keyArticle: Any): PagingSource<Int, BeanArticle> =
        mUseCase.qAArticlePageSource()

}