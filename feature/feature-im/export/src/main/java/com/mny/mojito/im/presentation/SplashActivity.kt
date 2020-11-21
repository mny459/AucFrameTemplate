package com.mny.mojito.im.presentation

import android.animation.*
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Property
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ColorUtils
import com.mny.mojito.base.BaseActivity
import com.mny.mojito.im.R
import com.mny.mojito.im.data.factory.AccountManager
import com.mny.mojito.im.presentation.account.AccountActivity
import com.mny.mojito.im.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.qiujuer.genius.res.Resource

class SplashActivity : BaseActivity() {
    private var mBgDrawable: ColorDrawable? = null
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        mBgDrawable = ColorDrawable(ColorUtils.getColor(R.color.colorPrimary))
        ll_root?.background = mBgDrawable
    }

    override fun initData(savedInstanceState: Bundle?) {
        startAnim(0.5f) {
            skip()
        }
    }

    private fun waitPushReceiverId() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(500)
            waitPushReceiverId()
        }
    }

    private fun skip() {
        startAnim(1f) {
            reallySkip()
        }
    }

    private fun reallySkip() {
        lifecycleScope.launch(Dispatchers.Main) {
            if (PermissionFragment.haveAll(mActivity, supportFragmentManager, OnPermissionGranted())) {
                delay(100)
                next()
            }
        }
    }

    private fun next() {
        if (AccountManager.isLogin()) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        } else {
//                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            startActivity(Intent(this@SplashActivity, AccountActivity::class.java))
        }
        finish()
    }

    private val property: Property<SplashActivity, Any> = object : Property<SplashActivity, Any>(Any::class.java, "color") {
        override fun set(activity: SplashActivity, value: Any) {
            activity.mBgDrawable?.color = value as Int
        }

        override fun get(activity: SplashActivity): Any {
            return activity.mBgDrawable?.color ?: 0
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画的结束进度
     * @param endCallback 动画结束时触发
     */
    private fun startAnim(endProgress: Float, endCallback: () -> Unit) {
        // 获取一个最终的颜色
        val finalColor = Resource.Color.WHITE
        // 运算当前进度的颜色
        val evaluator = ArgbEvaluator()
        val endColor = evaluator.evaluate(endProgress, mBgDrawable!!.color, finalColor) as Int
        // 构建一个属性动画
        val valueAnimator: ValueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor)
        valueAnimator.duration = 1500 // 时间
        valueAnimator.setIntValues(mBgDrawable!!.color, endColor) // 开始结束值
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // 结束时触发
                endCallback.invoke()
            }
        })
        valueAnimator.start()
    }

    inner class OnPermissionGranted : PermissionFragment.OnPermissionGrantedListener {
        override fun onGranted() {
            next()
        }
    }
}
