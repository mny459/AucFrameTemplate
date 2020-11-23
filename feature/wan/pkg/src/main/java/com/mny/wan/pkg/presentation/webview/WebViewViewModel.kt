package com.mny.wan.pkg.presentation.webview

import androidx.hilt.lifecycle.ViewModelInject
import com.mny.wan.mvvm.BaseAction
import com.mny.wan.mvvm.BaseState
import com.mny.wan.mvvm.BaseViewModel

class WebViewViewModel @ViewModelInject constructor(
) : BaseViewModel<WebViewViewModel.ViewState, WebViewViewModel.Action>(ViewState()) {

    fun initUrl(url: String) {
        sendAction(Action.InitUrl(url))
    }

    fun updateTitle(title: String) {
        sendAction(Action.UpdateTitle(title))
    }

    data class ViewState(
        val url: String = "",
        val title: String = "",
    ) : BaseState

    sealed class Action : BaseAction {
        class InitUrl(val url: String) : Action()
        class UpdateTitle(val title: String) : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.InitUrl -> state.copy(url = viewAction.url)
        is Action.UpdateTitle -> state.copy(title = viewAction.title)
    }

}