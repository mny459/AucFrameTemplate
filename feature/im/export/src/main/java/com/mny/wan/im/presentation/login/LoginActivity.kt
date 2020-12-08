package com.mny.wan.im.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mny.mojito.base.BaseActivity
import com.mny.wan.im.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    companion object {
        fun go(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {}

    override fun initData(savedInstanceState: Bundle?) {}
}
