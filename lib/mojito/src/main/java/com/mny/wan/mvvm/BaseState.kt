package com.mny.wan.mvvm

interface BaseState

data class BaseViewState(
        val loading: Boolean = false,
        val complete: Boolean = false,
        val errorMsg: String = ""
) : BaseState

interface BaseDataState
