package com.mny.wan.pkg.presentation.main.project

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mny.wan.http.MojitoResult
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel
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