package com.mny.wan.pkg.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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

class CommonFragmentViewPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val tabs: List<String> = emptyList(),
    private val keepWhenInvisible: Boolean = true
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
    override fun destroyItem(container: View, position: Int, `object`: Any) {
        if (!keepWhenInvisible) super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (tabs.isEmpty()) "" else tabs[position]
    }
}