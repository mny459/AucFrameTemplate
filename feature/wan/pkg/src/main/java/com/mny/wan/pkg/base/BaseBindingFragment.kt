package com.mny.wan.pkg.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mny.wan.base.BaseFragment
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseBindingFragment<VB : ViewBinding> : BaseFragment(0) {
    protected var mBinding: VB? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            val type = javaClass.genericSuperclass as ParameterizedType
            val aClass = type.actualTypeArguments[0] as Class<*>
            val method = aClass.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            val binding = method.invoke(null, layoutInflater, container, false) as VB
            binding.apply {
                mBinding = this
                mRootView = this.root
                initView(this.root)
            }
        } else {
            // 把当前 root 从其父控件中移除
            (mRootView?.parent as? ViewGroup)?.apply {
                removeView(mRootView)
            }
        }
        return mRootView
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}