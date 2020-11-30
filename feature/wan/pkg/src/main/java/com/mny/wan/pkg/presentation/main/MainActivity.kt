package com.mny.wan.pkg.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        val navView: BottomNavigationView = findViewById(R.id.navigation)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.homeFragment, R.id.weChatFragment, R.id.projectFragment, R.id.systemFragment, R.id.QAFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
//        navController.addOnDestinationChangedListener { _, destination, _ -> }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, null) ||
                super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        LogUtils.d("onOptionsItemSelected ${item.itemId}")
        return NavigationUI.onNavDestinationSelected(item, navController) ||
                super.onOptionsItemSelected(item)
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(MainActivity::class.java)
        }
    }
}