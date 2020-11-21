package com.mny.mojito.base.delegate

import android.os.Bundle

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
interface IActivity {
    fun initWindow(savedInstanceState: Bundle?)
    fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean
    fun layoutId(savedInstanceState: Bundle?): Int
    fun initView(savedInstanceState: Bundle?)
    fun initObserver()
    fun initData(savedInstanceState: Bundle?)
    fun useEventBus(): Boolean
}