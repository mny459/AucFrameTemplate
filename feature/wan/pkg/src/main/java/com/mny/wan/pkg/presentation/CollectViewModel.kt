package com.mny.wan.pkg.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mny.wan.http.doSuccess
import com.mny.wan.pkg.domain.usecase.CollectUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val mUseCase: CollectUseCase
) :
    AndroidViewModel(context as Application) {

    fun collect(articleId: Int) {
        viewModelScope.launch {
            mUseCase.collect(articleId)
                .collect {
                    it.doSuccess {
                        mCollectIds.value = articleId
                        mCollectIds.value = 0
                    }
                }
        }
    }

    fun cancelCollect(articleId: Int) {
        viewModelScope.launch {
            mUseCase.cancelCollect(articleId)
                .collect {
                    it.doSuccess {
                        mCancelCollectIds.value = articleId
                        mCancelCollectIds.value = 0
                    }
                }
        }
    }

    private val mCollectIds = MutableStateFlow<Int>(0)
    private val mCancelCollectIds = MutableStateFlow<Int>(0)
    val collectIds: StateFlow<Int> = mCollectIds
    val cancelCollectIds: StateFlow<Int> = mCancelCollectIds
}