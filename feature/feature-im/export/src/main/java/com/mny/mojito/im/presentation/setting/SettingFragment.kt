package com.mny.mojito.im.presentation.setting

import android.os.Bundle
import android.view.View
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.im.R

class SettingFragment : BaseFragment(R.layout.fragment_setting) {

    override fun initView(view: View) {}

    override fun initData(savedInstanceState: Bundle?) {}

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }
}