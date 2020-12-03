package com.mny.wan.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.BuildConfig
import com.mny.wan.entension.asLiveData
import kotlin.properties.Delegates

/**
 * Desc:
 */
abstract class BaseStateViewModel : BaseViewModel<BaseViewState, BaseViewAction>(BaseViewState()) {
    override fun onReduceState(viewAction: BaseViewAction): BaseViewState =
        onReduceViewState(viewAction)
}