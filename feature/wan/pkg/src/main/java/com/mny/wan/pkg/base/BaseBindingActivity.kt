package com.mny.wan.pkg.base

import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.base.BaseActivity
import java.lang.reflect.ParameterizedType

abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {
    private lateinit var mBinding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        mActivity = this
        initWindow(savedInstanceState)
        super.onCreate(savedInstanceState)
        try {
            if (initArgs(intent.extras, savedInstanceState)) {
                // 通过反射实例化 ViewBinding
                val type = javaClass.genericSuperclass as ParameterizedType
                val aClass = type.actualTypeArguments[0] as Class<*>
                val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                mBinding = method.invoke(null, layoutInflater) as VB
                setContentView(mBinding.root)
            } else {
                LogUtils.d("Activity 间跳转的参数不对，${this.javaClass.name}")
                finish()
                return
            }
        } catch (e: Exception) {
            LogUtils.v("$e")
            if (e is InflateException) throw e
//            e.printStackTrace()
        }
        initView(savedInstanceState)
        initObserver()
        initData(savedInstanceState)
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = 0
}