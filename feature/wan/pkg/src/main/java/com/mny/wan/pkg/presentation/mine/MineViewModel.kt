package com.mny.wan.pkg.presentation.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.http.MojitoResult
import com.mny.mojito.mvvm.*
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo
import com.mny.wan.pkg.domain.usecase.UserUseCase
import com.mny.wan.pkg.presentation.AppViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MineViewModel @ViewModelInject constructor(
) : BaseViewModel<BaseViewState, BaseViewAction>(BaseViewState()) {
    override fun onReduceState(viewAction: BaseViewAction): BaseViewState =
        onReduceViewState(viewAction)
}