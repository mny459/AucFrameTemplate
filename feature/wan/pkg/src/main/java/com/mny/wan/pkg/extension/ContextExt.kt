package com.mny.wan.pkg.extension

import android.content.Context
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * Desc:
 */
fun Context.showLoading(tip: String): QMUITipDialog {
    val loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(tip)
            .create(false)
    loadingDialog.show()
    return loadingDialog
}