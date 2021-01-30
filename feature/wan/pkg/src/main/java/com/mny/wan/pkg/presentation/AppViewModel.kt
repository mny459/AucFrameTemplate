package com.mny.wan.pkg.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.http.doSuccess
import com.mny.wan.pkg.data.local.dao.ArticleDao
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo
import com.mny.wan.pkg.domain.usecase.CollectUseCase
import com.mny.wan.pkg.domain.usecase.UserUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val mUseCase: CollectUseCase,
    private val mUserUseCase: UserUseCase,
    private val mArticleDao: ArticleDao
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

    fun collect(article: BeanArticle) {
        viewModelScope.launch {
            mUseCase.collect(article.id)
                .collect {
                    it.doSuccess {
                        mCollectIds.value = article.id
                        mCollectIds.value = 0
                        article.collect = false
                        withContext(Dispatchers.IO){
                            mArticleDao.collect(article)
                        }
                    }
                }
        }
    }

    fun cancelCollect(article: BeanArticle) {
        viewModelScope.launch {
            mUseCase.cancelCollect(article.id)
                .collect {
                    it.doSuccess {
                        mCancelCollectIds.value = article.id
                        mCancelCollectIds.value = 0
                        article.collect = false
                        withContext(Dispatchers.IO){
                            mArticleDao.cancelCollect(article)
                        }
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

    fun logout() {
        viewModelScope.launch {
            mUserUseCase.logout()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            ToastUtils.showShort("退出登录成功")
                            updateUserInfo(null)
                            updateCoinInfo(null)
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