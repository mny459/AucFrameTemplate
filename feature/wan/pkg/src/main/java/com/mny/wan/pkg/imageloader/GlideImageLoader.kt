package com.mny.wan.pkg.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 *@author mny on 2019-10-20.
 *        Email：mny9@outlook.com
 *        Desc:
 */
class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);
        //用fresco加载图片简单用法，记得要写下面的createImageView方法
        val uri = Uri.parse(path as String)
        imageView.setImageURI(uri)
    }
}