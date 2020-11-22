package com.mny.wan.im.presentation.personal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mny.wan.entension.observe
import com.mny.wan.entension.setOnDebouncedClickListener
import com.mny.wan.im.R
import com.mny.wan.im.base.BaseToolbarActivity
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.presentation.message.MessageActivity
import com.mny.wan.im.presentation.viewmodel.FollowViewModel
import com.mny.wan.im.widget.PortraitView
import dagger.hilt.android.AndroidEntryPoint
import net.qiujuer.genius.res.Resource

@AndroidEntryPoint
class PersonalActivity : BaseToolbarActivity() {
    // 关注
    private var mFollowItem: MenuItem? = null
    private val mViewModel: PersonalViewModel by viewModels()
    private val mFollowViewModel: FollowViewModel by viewModels()

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        val userId = bundle?.getString(BOUND_KEY_ID)
        if (!userId.isNullOrEmpty()) {
            mViewModel.initUserId(userId)
            return super.initArgs(bundle, savedInstanceState)
        }
        return false
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_personal

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
        observe(mFollowViewModel.stateLiveData, FollowStateObserver())
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        findViewById<View>(R.id.btn_say_hello)?.setOnDebouncedClickListener {
            mViewModel.stateLiveData.value?.user?.apply {
                MessageActivity.go(mActivity, this.id)
            }
        }
        title = ""
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.getUserInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.personal, menu)
        mFollowItem = menu.findItem(R.id.action_follow)
        changeFollowItemStatus(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_follow) {
            mViewModel.stateLiveData.value?.user?.apply {
                mFollowViewModel.follow(this.id)
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    /**
     * 更改关注菜单状态
     */
    private fun changeFollowItemStatus(isFollow: Boolean) {
        if (mFollowItem == null) return
        // 根据状态设置颜色
        var drawable = resources.getDrawable(
                if (isFollow) R.drawable.ic_favorite
                else R.drawable.ic_favorite_border
        )
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, Resource.Color.WHITE)
        mFollowItem?.icon = drawable
    }

    private fun updateUserInfo(user: UserCard) {
        findViewById<PortraitView>(R.id.iv_portrait).setup(Glide.with(this), user)
        findViewById<TextView>(R.id.tv_name).text = user.name
        findViewById<TextView>(R.id.tv_desc).text = user.desc
        findViewById<TextView>(R.id.tv_follows).text = String.format(getString(R.string.label_follows), user.follows)
        findViewById<TextView>(R.id.tv_following).text = String.format(getString(R.string.label_following), user.following)
    }

    companion object {
        private const val BOUND_KEY_ID = "BOUND_KEY_ID"
        fun go(activity: Activity, userId: String) {
            val intent = Intent(activity, PersonalActivity::class.java)
            intent.putExtra(BOUND_KEY_ID, userId)
            activity.startActivity(intent)
        }
    }

    inner class ViewStateObserver : Observer<PersonalViewModel.State> {
        override fun onChanged(state: PersonalViewModel.State) {
            if (state.loading) {

            } else {
                changeFollowItemStatus(state.isFollow)
                findViewById<TextView>(R.id.btn_say_hello).visibility = if (state.isFollow) View.VISIBLE else View.GONE
                if (state.user != null) {
                    updateUserInfo(state.user)
                } else if (!state.errorMsg.isNullOrEmpty()) {
                    ToastUtils.showShort(state.errorMsg)
                }

            }
        }
    }

    inner class FollowStateObserver : Observer<FollowViewModel.State> {
        override fun onChanged(state: FollowViewModel.State) {
            if (state.loading) {

            } else {
                if (state.userCard != null) {
                    mViewModel.refreshUser(state.userCard)
                } else if (state.errorMsg.isNotEmpty()) {
                    ToastUtils.showShort("${state.errorMsg}")
                }
            }
        }
    }

}