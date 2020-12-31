package com.mny.wan.pkg.presentation.main.project

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_project

    companion object {
        fun show() {
            ActivityUtils.startActivity(ProjectActivity::class.java)
        }
    }
}