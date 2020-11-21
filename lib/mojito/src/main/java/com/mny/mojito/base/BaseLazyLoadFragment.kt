package com.mny.mojito.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
abstract class BaseLazyLoadFragment(@LayoutRes contentLayoutId: Int) : BaseFragment(contentLayoutId) {
    private var isViewCreated = false// 界面是否已创建完成

    private var isVisibleToUser = false// 是否对用户可见

    private var isDataLoaded = false// 数据是否已请求


    /**
     * 第一次可见时触发调用,此处实现具体的数据请求逻辑
     */
    protected abstract fun lazyLoadData()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryLoadData()
    }

    /**
     * 保证在initData后触发
     */
    override fun onResume() {
        super.onResume()
        isViewCreated = true
        tryLoadData()
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     */
    private fun isParentVisible(): Boolean {
        val fragment: Fragment? = parentFragment
        return fragment == null || fragment is BaseLazyLoadFragment && fragment.isVisibleToUser()
    }

    /**
     * ViewPager场景下，当前fragment可见时，如果其子fragment也可见，则让子fragment请求数据
     */
    private fun dispatchParentVisibleState() {
        val fragmentManager: FragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is BaseLazyLoadFragment && child.isVisibleToUser()) {
                child.tryLoadData()
            }
        }
    }

    open fun tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && !isDataLoaded) {
            lazyLoadData()
            isDataLoaded = true
            //通知子Fragment请求数据
            dispatchParentVisibleState()
        }
    }

    fun isVisibleToUser() = isVisibleToUser
}