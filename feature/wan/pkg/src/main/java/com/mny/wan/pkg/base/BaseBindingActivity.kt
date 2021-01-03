package com.mny.wan.pkg.base

import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.BaseActivity
import java.lang.reflect.ParameterizedType

/**
 * 目前没有找到什么好的方式，就暂时先用反射的形式来初始化 ViewBinding 吧
 */
abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {
    protected lateinit var mBinding: VB

    override fun initContentView(savedInstanceState: Bundle?): Boolean {
        try {
            if (initArgs(intent.extras, savedInstanceState)) {
                // 通过反射实例化 ViewBinding
                val type = javaClass.genericSuperclass as ParameterizedType
                val aClass = type.actualTypeArguments[0] as Class<*>
                val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                val binding = method.invoke(null, layoutInflater) as VB
                setContentView(binding.root)
                mBinding = binding
            } else {
                LogUtils.d("Activity 间跳转的参数不对，${this.javaClass.name}")
                finish()
                return false
            }
        } catch (e: Exception) {
            LogUtils.v("$e")
            if (e is InflateException) throw e
//            e.printStackTrace()
        }
        return true
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = 0
}