package com.mny.wan.pkg.presentation.main.system

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mny.wan.http.MojitoResult
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
import com.mny.wan.pkg.data.remote.model.BeanNav
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.domain.usecase.SystemUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SystemTagViewModel @ViewModelInject constructor(
    private val mUseCase: SystemUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseViewModel<SystemTagViewModel.ViewState, SystemTagViewModel.Action>(ViewState()) {
    private var mTag = SystemTagFragment.TAG_SYSTEM
    val mSystemTree = MutableLiveData<MutableList<BeanSystemParent>>()
    val mNavTree = MutableLiveData<MutableList<BeanNav>>()

    fun initTag(tag: Int) {
        this.mTag = tag
    }

    override fun onLoadData() {
        super.onLoadData()
        if (mTag == SystemTagFragment.TAG_SYSTEM) {
            viewModelScope.launch {
                mUseCase.fetchSystemTree().collect { result ->
                    when (result) {
                        is MojitoResult.Success -> {
                            mSystemTree.postValue(result.data)
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
        } else {
            viewModelScope.launch {
                mUseCase.fetchNavTree().collect { result ->
                    when (result) {
                        is MojitoResult.Success -> {
                            mNavTree.postValue(result.data)
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

    data class ViewState(
        val isLoading: Boolean = false,
    ) : BaseState

    sealed class Action : BaseAction {
        object LoginStart : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.LoginStart -> state.copy(
            isLoading = true
        )
    }

    inner class SystemTagObserver : Observer<MutableList<BeanSystemParent>> {
        override fun onChanged(tabs: MutableList<BeanSystemParent>) {

        }

    }

}