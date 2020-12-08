package com.mny.wan.pkg.presentation.login

import android.os.Bundle
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_login
}
