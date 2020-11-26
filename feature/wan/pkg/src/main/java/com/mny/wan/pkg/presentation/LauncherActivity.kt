package com.mny.wan.pkg.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.presentation.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = 0
    override fun initObserver() {
        super.initObserver()
        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@LauncherActivity, MainActivity::class.java)
//                intent.putExtra(CLICK_X, ScreenUtils.getAppScreenWidth() / 2)
//                intent.putExtra(CLICK_Y, ScreenUtils.getAppScreenHeight() / 2)
            startActivity(intent)
            this@LauncherActivity.finish()
        }
    }
}