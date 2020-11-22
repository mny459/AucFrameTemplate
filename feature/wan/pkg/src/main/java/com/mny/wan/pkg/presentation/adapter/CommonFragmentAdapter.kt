package com.mny.wan.pkg.presentation.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @Author CaiRj
 * @Date 2019/8/12 10:27
 * @Desc
 */
class CommonFragmentAdapter(
    fm: FragmentManager,
    private val fragments: MutableList<Fragment>,
    val keepWhenInvisible: Boolean = true
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
    override fun destroyItem(container: View, position: Int, `object`: Any) {
        if (!keepWhenInvisible) super.destroyItem(container, position, `object`)
    }
}