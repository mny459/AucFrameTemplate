package com.mny.wan.pkg.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.blankj.utilcode.util.LogUtils

fun AppCompatActivity.initToolbar(toolbar: Toolbar, title: String = "") {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        // 添加默认的返回图标
        setDisplayHomeAsUpEnabled(true)
        // 设置返回键可用
        setHomeButtonEnabled(true)
        if (title.isNotEmpty()) {
            LogUtils.d("initToolbar title: $title")
            // 一个小坑：标题需要使用 supportActionBar 进行设置
            // 而不能使用 toolbar
            this.title = title
        }
    }

}