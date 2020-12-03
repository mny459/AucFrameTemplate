package com.mny.wan.base

import android.os.Bundle
import android.view.InflateException
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.base.delegate.IActivity

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {
    protected lateinit var mActivity: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        mActivity = this
        initWindow(savedInstanceState)
        super.onCreate(savedInstanceState)
        if (!initContentView(savedInstanceState)) return
        initView(savedInstanceState)
        initObserver()
        initData(savedInstanceState)
    }

    open fun initContentView(savedInstanceState: Bundle?): Boolean {
        try {
            if (initArgs(intent.extras, savedInstanceState)) {
                val layoutResID = layoutId(savedInstanceState)
                //如果initView返回0,框架则不会调用setContentView()
                if (layoutResID != 0) {
                    setContentView(layoutResID)
                }
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

    override fun initWindow(savedInstanceState: Bundle?) {}
    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean = true
    override fun initView(savedInstanceState: Bundle?) {}
    override fun initObserver() {}
    override fun initData(savedInstanceState: Bundle?) {}

    override fun useEventBus(): Boolean = true

}