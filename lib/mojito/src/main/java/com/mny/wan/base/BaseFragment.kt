package com.mny.wan.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.mny.wan.base.delegate.IFragment

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId),
    IFragment {

    protected var mActivity: BaseActivity? = null
    protected var mRootView: View? = null

    // 标示是否第一次初始化数据
    protected var mIsFirstInitData = true;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) mActivity = context
        initArgs(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            val view = super.onCreateView(inflater, container, savedInstanceState)
            mRootView = view
            if (view != null) initView(view)
        } else {
            // 把当前 root 从其父控件中移除
            (mRootView?.parent as? ViewGroup)?.apply {
                removeView(mRootView)
            }
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mIsFirstInitData) {
            initObserver()
            // 触发一次以后就不会触发
            mIsFirstInitData = false
            // 触发
            onFirstInit()
        }
        initData(savedInstanceState)
    }

    override fun onDestroyView() {
        mRootView = null
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun initArgs(bundle: Bundle?) {}
    override fun initView(view: View) {}
    override fun initObserver() {}
    override fun initData(savedInstanceState: Bundle?) {}
    override fun useEventBus(): Boolean = true

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected open fun onFirstInit() {}

}