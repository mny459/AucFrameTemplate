package com.mny.wan.im.presentation.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mny.wan.base.BaseActivity
import com.mny.wan.im.R
import com.mny.wan.im.extension.loadBg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_user.*

@AndroidEntryPoint
class UserActivity : BaseActivity() {

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_user

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        loadBg(R.drawable.bg_src_tianjin, iv_bg)
    }

    companion object {
        fun go(context: Context) {
            val intent = Intent(context, UserActivity::class.java)
            if (context is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}