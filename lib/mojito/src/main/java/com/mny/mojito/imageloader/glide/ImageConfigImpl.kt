package com.mny.mojito.imageloader.glide

import android.widget.ImageView
import com.mny.mojito.imageloader.ImageConfig

class ImageConfigImpl(val builder: Builder) : ImageConfig() {

    val fallback: Int
    //0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT

    @CacheStrategy.Strategy
    val cacheStrategy: Int

    //图片每个圆角的大小
    val imageRadius: Int

    //高斯模糊值, 值越大模糊效果越大
    val blurValue: Int

    val imageViews: MutableList<ImageView>

    //是否使用淡入淡出过渡动画
    val isCrossFade: Boolean

    //是否将图片剪切为 CenterCrop
    val isCenterCrop: Boolean

    //是否将图片剪切为圆形
    val isCircle: Boolean

    //清理内存缓存
    val isClearMemory: Boolean

    //清理本地缓存
    val isClearDiskCache: Boolean

    init {
        url = builder.url
        imageView = builder.imageView
        placeholder = builder.placeholder
        errorPic = builder.errorPic
        this.fallback = builder.fallback
        this.cacheStrategy = builder.cacheStrategy
        this.imageRadius = builder.imageRadius
        this.blurValue = builder.blurValue
        this.imageViews = builder.imageViews
        this.isCrossFade = builder.isCrossFade
        this.isCenterCrop = builder.isCenterCrop
        this.isCircle = builder.isCircle
        this.isClearMemory = builder.isClearMemory
        this.isClearDiskCache = builder.isClearDiskCache
    }

    class Builder internal constructor() {
        internal var url: String = ""
        internal var imageView: ImageView? = null
        internal var placeholder = 0

        //请求 url 为空,则使用此图片作为占位符
        internal var errorPic = 0
        internal var fallback = 0
        //0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT

        @CacheStrategy.Strategy
        internal var cacheStrategy = 0

        //图片每个圆角的大小
        internal var imageRadius: Int = 0

        //高斯模糊值, 值越大模糊效果越大
        internal var blurValue: Int = 0

        internal var imageViews = mutableListOf<ImageView>()

        //是否使用淡入淡出过渡动画
        internal var isCrossFade: Boolean = false

        //是否将图片剪切为 CenterCrop
        internal var isCenterCrop = false

        //是否将图片剪切为圆形
        internal var isCircle = false

        //清理内存缓存
        internal var isClearMemory = false

        //清理本地缓存
        internal var isClearDiskCache = false

        fun url(url: String) = apply {
            this.url = url
        }

        fun placeholder(placeholder: Int) = apply {
            this.placeholder = placeholder
        }

        fun errorPic(errorPic: Int) {
            this.errorPic = errorPic

        }

        fun fallback(fallback: Int) {
            this.fallback = fallback

        }

        fun imageView(imageView: ImageView?) {
            this.imageView = imageView

        }

        fun cacheStrategy(@CacheStrategy.Strategy cacheStrategy: Int) {
            this.cacheStrategy = cacheStrategy

        }

        fun imageRadius(imageRadius: Int) {
            this.imageRadius = imageRadius

        }

        fun blurValue(blurValue: Int) { //blurValue 建议设置为 15
            this.blurValue = blurValue

        }

        fun imageViews(vararg imageViews: ImageView) {
            this.imageViews.addAll(imageViews.toList())
        }

        fun isCrossFade(isCrossFade: Boolean) {
            this.isCrossFade = isCrossFade
        }

        fun isCenterCrop(isCenterCrop: Boolean) {
            this.isCenterCrop = isCenterCrop
        }

        fun isCircle(isCircle: Boolean) {
            this.isCircle = isCircle
        }

        fun isClearMemory(isClearMemory: Boolean) {
            this.isClearMemory = isClearMemory
        }

        fun isClearDiskCache(isClearDiskCache: Boolean) {
            this.isClearDiskCache = isClearDiskCache
        }

        fun build(): ImageConfigImpl {
            return ImageConfigImpl(this)
        }
    }
}