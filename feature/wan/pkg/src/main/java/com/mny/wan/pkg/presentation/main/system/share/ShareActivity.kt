package com.mny.wan.pkg.presentation.main.system.share

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_share

    companion object {
        fun show() {
            ActivityUtils.startActivity(ShareActivity::class.java)
        }
    }
}