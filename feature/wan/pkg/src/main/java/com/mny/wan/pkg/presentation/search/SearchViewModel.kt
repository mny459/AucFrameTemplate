package com.mny.wan.pkg.presentation.search

import android.os.StatFs
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.http.doSuccess
import com.mny.mojito.mvvm.BaseViewAction
import com.mny.mojito.mvvm.BaseViewState
import com.mny.wan.pkg.base.BaseArticleViewModel
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.domain.usecase.ArticleUseCase
import com.mny.wan.pkg.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val mUseCase: ArticleUseCase,
    private val mSearchUseCase: SearchUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseArticleViewModel<BaseViewState, BaseViewAction>(
        BaseViewState(),
        mSavedStateHandle
    ) {

    fun observerSearchKeys() {
        LogUtils.d("observerSearchKeys =================== ")
        viewModelScope.launch {
            mSearchStateFlow.debounce(200).collectLatest {
                LogUtils.d("搜索 $it")
                mClearListCh.offer(Unit)
                mSavedStateHandle.set(KEY_ARTICLE, it)
            }
        }
    }

    val mHotKey = MutableLiveData<MutableList<BeanHotKey>>()

    private val mSearchStateFlow = MutableStateFlow("")
    val searchKey: StateFlow<String> = mSearchStateFlow

    fun fetchHotKey() {
        viewModelScope.launch {
            mSearchUseCase.fetchHotKey().collect { result ->
                result.doSuccess { data ->
                    data?.apply {
                        mHotKey.postValue(this)
                    }
                }
            }
        }
    }

    fun search(name: String) {
        if (mSearchStateFlow.value == name) return
        mSearchStateFlow.value = name
//        mClearListCh.offer(Unit)
//        mSavedStateHandle.set(KEY_ARTICLE, name)
    }

    override fun getArticlePageSource(search: Any): PagingSource<Int, BeanArticle> =
        mUseCase.searchArticlePageSource(search)

    override fun onReduceState(viewAction: BaseViewAction): BaseViewState =
        onReduceViewState(viewAction)
}