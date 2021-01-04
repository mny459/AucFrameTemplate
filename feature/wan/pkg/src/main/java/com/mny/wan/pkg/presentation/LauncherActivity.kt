package com.mny.wan.pkg.presentation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.presentation.main.MainNavFixActivity
import com.mny.wan.pkg.presentation.main.MainViewPagerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = 0
    override fun initObserver() {
        super.initObserver()
        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(
                this@LauncherActivity,
//                if (UserHelper.isLogin()) WanMainActivity::class.java else LoginActivity::class.java
//                if (UserHelper.isLogin()) WanMainActivity::class.java else LoginActivity::class.java
//                if (UserHelper.isLogin()) MainNavFixActivity::class.java else LoginActivity::class.java
//                SettingsActivity::class.java
                MainViewPagerActivity::class.java
            )
            startActivity(intent)
            this@LauncherActivity.finish()
        }
    }
}