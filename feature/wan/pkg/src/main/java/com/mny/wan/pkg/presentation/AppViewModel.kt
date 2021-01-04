package com.mny.wan.pkg.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.http.doSuccess
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo
import com.mny.wan.pkg.domain.usecase.CollectUseCase
import com.mny.wan.pkg.domain.usecase.UserUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val mUseCase: CollectUseCase,
    private val mUserUseCase: UserUseCase,
) :
    AndroidViewModel(context as Application) {

    private val mUserInfo = MutableLiveData<BeanUserInfo?>()
    val userInfo: LiveData<BeanUserInfo?> = mUserInfo
    private val mCoinInfo = MutableLiveData<BeanCoin?>()
    val coinInfo: LiveData<BeanCoin?> = mCoinInfo


    private val mCollectIds = MutableStateFlow<Int>(0)
    private val mCancelCollectIds = MutableStateFlow<Int>(0)
    val collectIds: StateFlow<Int> = mCollectIds
    val cancelCollectIds: StateFlow<Int> = mCancelCollectIds

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

    fun updateUserInfo(user: BeanUserInfo?) {
        mUserInfo.postValue(user)
        if (user != null) {
            fetchCoinInfo()
        }
    }

    fun updateCoinInfo(coin: BeanCoin?) {
        mCoinInfo.postValue(coin)
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            val user = mUserUseCase.fetchLocalUserInfo()
            updateUserInfo(user)
//            sendAction(Action.UpdateUser(user))
        }
    }

    private fun fetchCoinInfo() {
        viewModelScope.launch {
            mUserUseCase.fetchCoinInfo()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            it.data?.apply {
                                updateCoinInfo(this)
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

}