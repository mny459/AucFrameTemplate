package com.mny.wan.pkg.presentation.main.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.wan.pkg.data.local.entity.UiHomeArticle
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.domain.paging.HomeArticlePageSource
import com.mny.wan.pkg.domain.usecase.HomeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class HomeViewModel @ViewModelInject constructor(
    private val mUseCase: HomeUseCase,
    private val mHomeMediator: HomeArticlePageSource,
    @Assisted val mSavedStateHandle: SavedStateHandle
) : BaseViewModel<HomeViewModel.ViewState, HomeViewModel.Action>(
    ViewState(),
) {
    companion object {
        const val KEY_ARTICLE = "articles"
        private const val DEFAULT_PAGE = 0
    }

    val mBannerList = MutableLiveData<List<BeanBanner>>()
    val mTopArticles = MutableLiveData<List<BeanArticle>>()


    private val mClearListCh = Channel<Unit>(Channel.CONFLATED)

    // PagingData: 负责通知DataSource何时获取数据，以及如何获取数据
    val mArticleList: Flow<PagingData<UiHomeArticle>> = Pager(
        config = PagingConfig(
            // 每页显示的数据的大小。对应 PagingSource 里 LoadParams.loadSize
            pageSize = 20,
//                        // 预刷新的距离，距离最后一个 item 多远时加载数据
            prefetchDistance = 4,
//                        // 初始化加载数量，默认为 pageSize * 3
            initialLoadSize = 20,
//                        // 一次应在内存中保存的最大数据
            maxSize = 200,
            enablePlaceholders = false
        ),
        remoteMediator = mHomeMediator,
        pagingSourceFactory = {
            getArticlePageSource()
        }
    ).flow
        .cachedIn(viewModelScope) // 将数据缓存在 CoroutineScope，这里使用 viewModelScope

    open fun fetchArticleList() {
        mClearListCh.offer(Unit)
        mSavedStateHandle.set(KEY_ARTICLE, DEFAULT_PAGE)
    }

    fun getArticlePageSource(): PagingSource<Int, UiHomeArticle> {
        return mUseCase.homeArticlePageSource()
    }


    override fun onLoadData() {
        fetchBannerList()
        fetchTopArticles()
        super.onLoadData()
        fetchArticleList()
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