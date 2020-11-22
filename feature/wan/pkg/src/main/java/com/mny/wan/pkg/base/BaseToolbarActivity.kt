package com.mny.wan.pkg.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.R

abstract class BaseToolbarActivity : BaseActivity() {
    protected var mToolbar: Toolbar? = null
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mToolbar = findViewById<Toolbar>(R.id.toolbar)
        initToolbar()
    }

    private fun initToolbar() {
        mToolbar?.apply {
            // 启用 Toolbar
            setSupportActionBar(this)
            setTitleTextAppearance(mActivity, R.style.TextAppearance_Title)
        }
        supportActionBar?.apply {
            // 添加默认的返回图标
            setDisplayHomeAsUpEnabled(true)
            // 设置返回键可用
            setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}