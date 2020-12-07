package com.mny.wan.pkg.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mny.wan.base.BaseActivity
import com.mny.wan.nav.NavGraphBuilder
import com.mny.wan.pkg.R
import com.mny.wan.pkg.extension.enterFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavFixActivity : BaseActivity() {

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_main_nav_fix

    override fun initWindow(savedInstanceState: Bundle?) {
        super.initWindow(savedInstanceState)
        enterFullScreen()
    }
    override fun initView(savedInstanceState: Bundle?) {
        val navView: BottomNavigationView = findViewById(R.id.navigation)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        NavGraphBuilder.build(this, navController, fragment!!.id)
        navController.setGraph(R.navigation.main_fix_nav_graph)
        NavigationUI.setupWithNavController(navView, navController)
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
}