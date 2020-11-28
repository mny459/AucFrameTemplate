package com.mny.wan.pkg.presentation.collect

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_collect

    companion object {
        fun show() {
            ActivityUtils.startActivity(CollectActivity::class.java)
        }
    }
}