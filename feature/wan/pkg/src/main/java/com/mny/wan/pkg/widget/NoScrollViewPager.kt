package com.mny.wan.pkg.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

/**
 * @Author CaiRj
 * @Date 2019/8/12 10:56
 * @Desc 可以禁止横向滑动的 ViewPager
 */
class NoScrollViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var mCanScroll: Boolean = true
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (mCanScroll) super.onTouchEvent(ev)
        else false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (mCanScroll) super.onInterceptTouchEvent(ev)
        else false
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }

    fun setDisableScroll() {
        this.mCanScroll = false
    }
}