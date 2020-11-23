package com.mny.wan.pkg.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author CaiRj
 * @Date 2019/8/12 10:27
 * @Desc
 */
class CommonFragmentAdapter(
    fragment: Fragment,
    private val fragments: MutableList<Fragment>,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

class CommonFragmentAdapterForActivity(
    activity: FragmentActivity,
    private val fragments: MutableList<Fragment>,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}