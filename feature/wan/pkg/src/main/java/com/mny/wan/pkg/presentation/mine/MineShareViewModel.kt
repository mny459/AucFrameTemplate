package com.mny.wan.pkg.presentation.mine

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.pkg.base.BaseArticleViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.domain.usecase.ArticleUseCase

/**
 * 广场
 */
class MineShareViewModel @ViewModelInject constructor(
    private val mUseCase: ArticleUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) : BaseArticleViewModel<MineShareViewModel.ViewState, MineShareViewModel.Action>(
    ViewState(),
    mSavedStateHandle
) {

    override fun getArticlePageSource(keyArticle: Any): PagingSource<Int, BeanArticle> =
        mUseCase.mineShareArticlePageSource()

    data class ViewState(
        val isLoading: Boolean = false,
        val loginSuccess: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = ""
    ) : BaseState

    sealed class Action : BaseAction {
        object LoginStart : Action()
        object LoginSuccess : Action()
        class LoginFailure(val errorMsg: String) : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.LoginStart -> state.copy(
            isLoading = true,
            loginSuccess = false,
            isError = false,
            errorMsg = ""
        )
        Action.LoginSuccess -> state.copy(
            isLoading = false,
            loginSuccess = true,
            isError = false,
            errorMsg = ""
        )
        is Action.LoginFailure -> state.copy(
            isLoading = false,
            loginSuccess = false,
            isError = true,
            errorMsg = viewAction.errorMsg
        )
    }

}