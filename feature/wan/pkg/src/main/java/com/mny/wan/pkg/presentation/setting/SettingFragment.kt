package com.mny.wan.pkg.presentation.setting

import android.os.Bundle
import android.view.View
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R

class SettingFragment : BaseFragment(R.layout.fragment_setting) {

    override fun initView(view: View) {}

    override fun initData(savedInstanceState: Bundle?) {}

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }
}