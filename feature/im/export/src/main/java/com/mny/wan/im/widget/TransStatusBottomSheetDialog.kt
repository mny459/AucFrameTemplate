package com.mny.wan.im.widget

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Desc:
 */
class TransStatusBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window ?: return
        // 得到屏幕高度
        val screenHeight: Int = ScreenUtils.getScreenHeight()
        // 得到状态栏的高度
        val statusHeight: Int = BarUtils.getStatusBarHeight()
        // 计算dialog的高度并设置
        val dialogHeight = screenHeight - statusHeight
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                if (dialogHeight <= 0) ViewGroup.LayoutParams.MATCH_PARENT else dialogHeight)
    }

}