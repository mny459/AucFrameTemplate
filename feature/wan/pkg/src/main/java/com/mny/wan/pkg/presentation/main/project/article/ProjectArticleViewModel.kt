package com.mny.wan.pkg.presentation.main.project.article

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.mny.wan.http.MojitoResult
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
import com.mny.wan.pkg.base.BaseArticleViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanProject
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.ProjectUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProjectArticleViewModel @ViewModelInject constructor(
    private val mUseCase: ArticleUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseArticleViewModel<ProjectArticleViewModel.ViewState, ProjectArticleViewModel.Action>(
        ViewState(),
        mSavedStateHandle
    ) {
    private var mCId: Int = 0

    fun initCId(cId: Int) {
        this.mCId = cId;
    }

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

    override fun getArticlePageSource(): PagingSource<Int, BeanArticle> =
        mUseCase.projectArticlePageSource(mCId)

}