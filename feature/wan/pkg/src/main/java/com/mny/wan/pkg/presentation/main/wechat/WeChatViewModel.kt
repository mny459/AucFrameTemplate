package com.mny.wan.pkg.presentation.main.wechat

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.domain.usecase.WeChatUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeChatViewModel @ViewModelInject constructor(
    private val mUseCase: WeChatUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseViewModel<WeChatViewModel.ViewState, WeChatViewModel.Action>(ViewState()) {
    val mTabs = MutableLiveData<MutableList<BeanSystemParent>>()
    override fun onLoadData() {
        super.onLoadData()
        viewModelScope.launch {
            mUseCase.fetchProjectTree().collect { result ->
                when (result) {
                    is MojitoResult.Success -> {
                        mTabs.postValue(result.data!!)
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
    ) : BaseState

    sealed class Action : BaseAction {
        object LoginStart : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.LoginStart -> state.copy(isLoading = true)
    }

}