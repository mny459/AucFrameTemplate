package com.mny.wan.pkg.presentation.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo
import com.mny.wan.pkg.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MineViewModel @ViewModelInject constructor(
    private val mUseCase: UserUseCase,
) : BaseViewModel<MineViewModel.ViewState, MineViewModel.Action>(ViewState()) {

    override fun onLoadData() {
        super.onLoadData()
        fetchUserInfo()
        fetchCoinInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = mUseCase.fetchLocalUserInfo()
            sendAction(Action.UpdateUser(user))
        }
    }

    private fun fetchCoinInfo() {
        viewModelScope.launch {
            mUseCase.fetchCoinInfo()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            UserHelper.loginOut()
                            it.data?.apply {
                                sendAction(Action.UpdateCoin(this))
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
            mUseCase.logout()
                .collect {
                    when (it) {
                        is MojitoResult.Success -> {
                            ToastUtils.showShort("退出登录成功")
                            sendAction(Action.LoginOut)
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
        val user: BeanUserInfo? = null,
        val coin: BeanCoin? = null
    ) : BaseState

    sealed class Action : BaseAction {
        class UpdateUser(val user: BeanUserInfo?) : Action()
        class UpdateCoin(val coin: BeanCoin) : Action()
        object LoginOut : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.UpdateUser -> state.copy(user = viewAction.user)
        is Action.UpdateCoin -> state.copy(coin = viewAction.coin)
        Action.LoginOut -> state.copy(coin = null, user = null)
    }

}