package com.mny.wan.pkg.presentation.webview

import android.webkit.WebView

/**
 * @Author CaiRj
 * @Date 2019/10/21 10:33
 * @Desc
 */
// 前进
fun WebView.back() {
    if (this.canGoBack()) goBack()
}
// 后退
fun WebView.forward() {
    if (this.canGoForward()) goForward()
}