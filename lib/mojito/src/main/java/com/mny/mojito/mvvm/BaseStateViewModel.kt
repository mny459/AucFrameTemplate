package com.mny.mojito.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.BuildConfig
import com.mny.mojito.entension.asLiveData
import kotlin.properties.Delegates

/**
 * Desc:
 */
abstract class BaseStateViewModel : BaseViewModel<BaseViewState, BaseViewAction>(BaseViewState()) {
    override fun onReduceState(viewAction: BaseViewAction): BaseViewState =
        onReduceViewState(viewAction)
}