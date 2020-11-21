package com.mny.mojito.mvvm

interface BaseAction

sealed class BaseViewAction : BaseAction {
    object Loading : BaseViewAction()
    class Complete(val errorMsg: String = "") : BaseViewAction()
}
