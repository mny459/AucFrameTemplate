package com.mny.mojito.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mny.mojito.BuildConfig
import com.mny.mojito.entension.asLiveData
import kotlin.properties.Delegates

/**
 * Desc:
 */
abstract class BaseViewModel<ViewState : BaseState, ViewAction : BaseAction>(initialState: ViewState) :
        ViewModel() {

    private val stateMutableLiveData = MutableLiveData<ViewState>()
    val stateLiveData = stateMutableLiveData.asLiveData()

    private val viewStateMutableLiveData = MutableLiveData<BaseViewState>()
    val viewStateLiveData = viewStateMutableLiveData.asLiveData()

    private var stateTimeTravelDebugger: StateTimeTravelDebugger? = null

    init {
        if (BuildConfig.DEBUG) {
            stateTimeTravelDebugger = StateTimeTravelDebugger(this::class.java.simpleName)
        }
    }

    // Delegate will handle state event deduplication
    // (multiple states of the same type holding the same data will not be dispatched multiple times to LiveData stream)
    protected var state by Delegates.observable(initialState) { _, old, new ->
        stateMutableLiveData.value = new

        if (new != old) {
            stateTimeTravelDebugger?.apply {
                addStateTransition(old, new)
                logLast()
            }
        }
    }
    protected var viewState by Delegates.observable(BaseViewState()) { _, old, new ->
        viewStateMutableLiveData.value = new

        if (new != old) {
            stateTimeTravelDebugger?.apply {
                addStateTransition(old, new)
                logLast()
            }
        }
    }

    fun sendAction(viewAction: ViewAction) {
        stateTimeTravelDebugger?.addAction(viewAction)
        state = onReduceState(viewAction)
    }

    fun sendAction(viewAction: BaseViewAction) {
        stateTimeTravelDebugger?.addAction(viewAction)
        viewState = onReduceViewState(viewAction)

    }

    fun loadData() {
        onLoadData()
    }

    protected open fun onLoadData() {}

    protected abstract fun onReduceState(viewAction: ViewAction): ViewState

    protected fun sendLoadingAction() {
        sendAction(BaseViewAction.Loading)
    }

    protected fun sendCompleteAction(errorMsg: String = "") {
        sendAction(BaseViewAction.Complete(errorMsg))
    }

    protected fun onReduceViewState(viewAction: BaseViewAction): BaseViewState =
            when (viewAction) {
                BaseViewAction.Loading -> viewState.copy(loading = true, complete = false, errorMsg = "")
                is BaseViewAction.Complete -> viewState.copy(loading = false, complete = true, errorMsg = viewAction.errorMsg)
            }


}