package com.mny.wan.pkg.presentation.login

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_login

    companion object {
        fun go() {
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
    }
}
