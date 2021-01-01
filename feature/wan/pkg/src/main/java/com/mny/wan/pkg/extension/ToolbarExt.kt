package com.mny.wan.pkg.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.initToolbar(toolbar: Toolbar, title: String = "") {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        // 添加默认的返回图标
        setDisplayHomeAsUpEnabled(true)
        // 设置返回键可用
        setHomeButtonEnabled(true)
    }
}