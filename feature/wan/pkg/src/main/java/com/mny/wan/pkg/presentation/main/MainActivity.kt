package com.mny.wan.pkg.presentation.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import com.mny.wan.pkg.widget.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var currentNavController: LiveData<NavController>? = null
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        val navGraphIds = listOf(
            R.navigation.main_home_nav_graph,
            R.navigation.main_qa_nav_graph,
//            R.navigation.main_discover_nav_graph,
            R.navigation.main_mine_nav_graph
        )
        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        return NavigationUI.navigateUp(navController, null) ||
//                super.onSupportNavigateUp()
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        LogUtils.d("onOptionsItemSelected ${item.itemId}")
//        return NavigationUI.onNavDestinationSelected(item, navController) ||
//                super.onOptionsItemSelected(item)
//    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(MainViewPagerActivity::class.java)
        }
    }
}