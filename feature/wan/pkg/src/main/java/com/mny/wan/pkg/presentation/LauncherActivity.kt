package com.mny.wan.pkg.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.presentation.login.LoginActivity
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
            startActivity(intent)
            this@LauncherActivity.finish()
        }
    }
}