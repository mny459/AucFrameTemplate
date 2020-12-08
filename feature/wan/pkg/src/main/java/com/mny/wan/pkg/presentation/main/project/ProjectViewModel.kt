package com.mny.wan.pkg.presentation.main.project

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.wan.pkg.data.remote.model.BeanProject
import com.mny.wan.pkg.domain.usecase.ProjectUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProjectViewModel @ViewModelInject constructor(
    private val mUseCase: ProjectUseCase,
    @Assisted mSavedStateHandle: SavedStateHandle
) :
    BaseViewModel<ProjectViewModel.ViewState, ProjectViewModel.Action>(ViewState()) {
    val mTabs = MutableLiveData<MutableList<BeanProject>>()
    override fun onLoadData() {
        super.onLoadData()
        viewModelScope.launch {
            mUseCase.fetchProjectTree().collect { result ->
                when (result) {
                    is MojitoResult.Success -> {
                        mTabs.postValue(result.data)
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