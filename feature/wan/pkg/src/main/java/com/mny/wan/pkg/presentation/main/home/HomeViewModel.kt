package com.mny.wan.pkg.presentation.main.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.wan.pkg.base.BaseArticleViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val mUseCase: HomeUseCase,
    private val mArticleUseCase: ArticleUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) : BaseArticleViewModel<HomeViewModel.ViewState, HomeViewModel.Action>(
    ViewState(),
    mSavedStateHandle
) {

    val mBannerList = MutableLiveData<List<BeanBanner>>()
    val mTopArticles = MutableLiveData<List<BeanArticle>>()

    override fun getArticlePageSource(keyArticle: Any): PagingSource<Int, BeanArticle> =
        mArticleUseCase.homeArticlePageSource()

    override fun onLoadData() {
        fetchBannerList()
        fetchTopArticles()
        super.onLoadData()
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
                        else -> {
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
                        else -> {
                        }
                    }
                }
        }
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

}