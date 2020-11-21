package com.mny.mojito.im.presentation.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mny.mojito.base.BaseActivity
import com.mny.mojito.im.R
import com.mny.mojito.im.data.factory.AccountManager
import com.mny.mojito.im.presentation.search.SearchActivity
import com.mny.mojito.entension.setOnDebouncedClickListener
import com.mny.mojito.im.data.db.AppRoomDatabase
import com.mny.mojito.im.presentation.login.LoginActivity
import com.mny.mojito.im.presentation.group.GroupCreateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var mDb: AppRoomDatabase
    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        return if (AccountManager.isLogin()) {
            super.initArgs(bundle, savedInstanceState)
        } else {
//            UserActivity.go(mActivity)
            true
        }
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        val navView: BottomNavigationView = findViewById(R.id.navigation)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            tv_title.text = destination.label
            // 对浮动按钮进行隐藏与显示的动画
            var transY = 0f
            var rotation = 0f
            if (destination.id == R.id.fragment_home) {
                // 主界面时隐藏
                transY = SizeUtils.dp2px(76f).toFloat()
            } else {
                // transY 默认为0 则显示
                rotation = if (destination.id == R.id.fragment_group) {
                    // 群
                    btn_action.setImageResource(R.drawable.ic_group_add)
                    -360f
                } else {
                    // 联系人
                    btn_action.setImageResource(R.drawable.ic_contact_add)
                    360f
                }
            }
            // 开始动画
            // 旋转，Y轴位移，弹性差值器，时间
            btn_action.animate()
                    .rotation(rotation)
                    .translationY(transY)
                    .setInterpolator(AnticipateOvershootInterpolator(1f))
                    .setDuration(480)
                    .start()
        }
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(object : ViewTarget<View, Drawable>(appbar) {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        view?.background = resource.current
                    }
                })

        btn_action?.setOnDebouncedClickListener {
            onActionClick()
        }
        iv_portrait?.setOnDebouncedClickListener {
            LoginActivity.go(mActivity)
//            PersonalActivity.go(mActivity, AccountManager.getUserId())
        }
        iv_search?.setOnDebouncedClickListener {
            goSearch()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
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

    private fun goSearch() {
        if (Navigation.findNavController(this, R.id.nav_host_fragment).currentDestination?.id == R.id.fragment_group) {
            SearchActivity.go(mActivity, SearchActivity.SEARCH_TYPE_GROUP)
        } else {
            SearchActivity.go(mActivity, SearchActivity.SEARCH_TYPE_USER)
        }
    }
    private fun onActionClick() {
        if (Navigation.findNavController(this, R.id.nav_host_fragment).currentDestination?.id == R.id.fragment_group) {
            GroupCreateActivity.go(mActivity)
        } else {
            SearchActivity.go(mActivity, SearchActivity.SEARCH_TYPE_USER)
        }
    }

}