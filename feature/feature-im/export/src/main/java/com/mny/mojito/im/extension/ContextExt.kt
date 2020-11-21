package com.mny.mojito.im.extension

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mny.mojito.im.R
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import java.io.File

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

fun Activity.loadBg(@DrawableRes drawableRes: Int, view: ImageView) {
    Glide.with(this)
            .load(drawableRes)
            .centerCrop()
            .into(object : DrawableImageViewTarget(view) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    val drawable = DrawableCompat.wrap(resource)
                    // 设置着色效果为蒙版模式
                    drawable?.setColorFilter(ColorUtils.getColor(R.color.colorAccent), PorterDuff.Mode.SCREEN)
                    this.view.setImageDrawable(drawable)
                }
            })
}

fun <T : CollapsingToolbarLayout> Fragment.loadBgForCollapsingToolbarLayout(@DrawableRes drawableRes: Int, view: T) {
    Glide.with(this)
            .load(drawableRes)
            .centerCrop()
            .into(object : ViewTarget<T, Drawable>(view) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    val drawable = DrawableCompat.wrap(resource)
                    // 设置着色效果为蒙版模式
                    drawable?.setColorFilter(ColorUtils.getColor(R.color.colorAccent), PorterDuff.Mode.SCREEN)
                    this.view.contentScrim = drawable
                }
            })
}

/**
 * 获取缓存文件夹地址
 *
 * @return 当前APP的缓存文件夹地址
 */
fun getCacheDirFile(): File {
    return Utils.getApp().cacheDir
}

/**
 * 获取头像的临时存储文件地址
 *
 * @return 临时文件
 */
fun getPortraitTmpFile(): File {
    // 得到头像目录的缓存地址
    val dir = File(getCacheDirFile(), "portrait")
    // 创建所有的对应的文件夹
    dir.mkdirs()

    // 删除旧的一些缓存为文件
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            file.delete()
        }
    }

    // 返回一个当前时间戳的目录文件地址
    val path = File(dir, SystemClock.uptimeMillis().toString() + ".jpg")
    return path.absoluteFile
}