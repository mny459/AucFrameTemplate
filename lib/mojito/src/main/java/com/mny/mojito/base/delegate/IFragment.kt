package com.mny.mojito.base.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
interface IFragment {
    fun initArgs(bundle: Bundle?)
    fun initView(view: View)
    fun initObserver()
    fun initData(savedInstanceState: Bundle?)
    fun useEventBus(): Boolean
}