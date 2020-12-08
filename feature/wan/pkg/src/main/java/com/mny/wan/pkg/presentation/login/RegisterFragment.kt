package com.mny.wan.pkg.presentation.login

import android.os.Bundle
import android.view.View
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.register_fragment) {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun initView(view: View) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}
