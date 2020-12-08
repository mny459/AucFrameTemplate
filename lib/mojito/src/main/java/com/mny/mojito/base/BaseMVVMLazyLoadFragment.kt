package com.mny.mojito.base

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
abstract class BaseMVVMLazyLoadFragment<VM : ViewModel>(@LayoutRes contentLayoutId: Int) : BaseFragment(contentLayoutId) {}