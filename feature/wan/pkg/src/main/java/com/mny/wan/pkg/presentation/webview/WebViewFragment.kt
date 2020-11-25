package com.mny.wan.pkg.presentation.webview

import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.web_view_fragment.*

/**
 * 原生WebView的使用
 */
@AndroidEntryPoint
class WebViewFragment : BaseFragment(R.layout.web_view_fragment) {
    companion object {
        const val ALPHA_DISABLE = 0.5F
        const val ALPHA_ENABLE = 1F
        fun newInstance(url: String): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    private val mViewModel: WebViewViewModel by activityViewModels()

    // 当前标题
    private var mWebView: WebView? = null

    override fun initView(view: View) {
        super.initView(view)
//        topBar?.addLeftBackImageButton()?.onClick {
//            mActivity?.finish()
//        }
//        topBar?.addRightImageButton(
//            R.drawable.ic_browser,
//            R.id.top_bar_right_browser
//        )?.onClick {
//            mActivity.checkOverlayPermission {
//                showOrRemoveFW()
//            }
////            val uri = Uri.parse(mUrl)
////            val intent = Intent(Intent.ACTION_VIEW, uri)
////            // intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
////            startActivity(intent)
//        }
//        imgBack?.onClick { mWebView?.back() }
//        imgForward?.onClick { mWebView?.forward() }
//        imgRefresh?.onClick { loadContent(mCurrentUrl) }
//        imgCollect?.onClick { imgCollect?.apply { isSelected = !isSelected } }
        initWebView(view)
        initSettings()
        initClient()
        loadContent(mViewModel.stateLiveData.value?.url ?: "")
    }

    //<editor-fold desc="初始化WebView">
    private fun initWebView(view: View) {
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // 为了避免内存泄漏，采用 new 的方式来构建 WebView
        mWebView = WebView(mActivity?.applicationContext)
        mWebView?.layoutParams = layoutParams
        view.findViewById<FrameLayout>(R.id.webViewContainer).addView(mWebView)
    }
    //</editor-fold>

    //<editor-fold desc="初始化WebViewClient">
    private fun initClient() {
        mWebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtils.v("onPageFinished $url")
                view?.apply { changGoForwardButton(this) }
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // 拦截 url 本地加载，不在浏览器中打开
                view?.loadUrl(url)
                return true
            }

            // 处理 https
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
//                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                LogUtils.d("errorOnLoadWebUrl errorCode = $errorCode, description = $description failingUrl = $failingUrl")
            }
        }

        mWebView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                // TODO 进度
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.apply {
                    mViewModel.updateTitle(this.trim())
                }
            }
        }

    }
    //</editor-fold>

    //<editor-fold desc="初始化WebViewSetting">
    private fun initSettings() {
        mWebView?.settings?.apply {
            // 设置支持 JS
            javaScriptEnabled = true

            // 设置自适应屏幕，两者合用
            // 1. 将图片调整到适合 webview 的大小
            useWideViewPort = true
            //2. 缩放至屏幕的大小
            loadWithOverviewMode = true

            // 缩放操作
            // 1. 设置支持缩放，默认为true。是下面那个的前提。
            setSupportZoom(true)
            // 设置内置的缩放控件。若为false，则该WebView不可缩放
            builtInZoomControls = true
            // 隐藏原生的缩放控件
            displayZoomControls = true
            // 设置 webview 中的缓存
            // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
            // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
            // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
            // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
            cacheMode = if (NetworkUtils.isConnected())
                WebSettings.LOAD_DEFAULT
            else
                WebSettings.LOAD_CACHE_ELSE_NETWORK
            // 设置可以访问文件
            allowFileAccess = true
            // 支持通过 JS 打开新窗口
            javaScriptCanOpenWindowsAutomatically = true
            //支持自动加载图片
            loadsImagesAutomatically = true
            //设置编码格式
            defaultTextEncodingName = "utf-8"
            // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }
    //</editor-fold>

    private fun loadContent(url: String) {
        // 参数说明：
        // 参数1：内容路径
        // 内容里不能出现 ’#’, ‘%’, ‘\’ , ‘?’ 这四个字符，若出现了需用 %23, %25, %27, %3f 对应来替代，否则会出现异常
        // 参数2：展示内容的类型
        // 参数3：字节码
        mWebView?.loadUrl(url)
    }

    private fun changGoForwardButton(view: WebView) {
        imgBack?.alpha = if (view.canGoBack()) ALPHA_ENABLE else ALPHA_DISABLE
        imgForward?.alpha = if (view.canGoForward()) ALPHA_ENABLE else ALPHA_DISABLE
    }

    override fun onDestroy() {
        // 避免内存泄漏
        mWebView?.apply {
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            clearHistory()
            (parent as ViewGroup).removeView(this)
            destroy()
        }
        super.onDestroy()
    }
    //</editor-fold>
}
