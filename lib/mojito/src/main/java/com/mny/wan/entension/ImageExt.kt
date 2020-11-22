package com.mny.wan.entension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @Author CaiRj
 * @Date 2019/10/17 17:10
 * @Desc
 */
fun ImageView.loadAvatar(url: String? = null, resourceId: Int? = null) {
    val imageView = this
    url?.apply {
        Glide.with(imageView)
            .applyDefaultRequestOptions(RequestOptions().circleCrop())
            .load(this)
            .into(imageView)
    }

    resourceId?.apply {
        Glide.with(imageView)
            .applyDefaultRequestOptions(RequestOptions().circleCrop())
            .load(this)
            .into(imageView)
    }

}

fun ImageView.loadProjectPreview(url: String? = null) {
    val imageView = this
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView)
            .applyDefaultRequestOptions(RequestOptions().centerCrop())
            .load(url)
            .into(imageView)
    }
}