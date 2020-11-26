package com.mny.wan.pkg.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.imageloader.GlideImageLoader
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener

class BannerAdapter : RecyclerView.Adapter<BannerViewHolder>() {
    private val mBanners = mutableListOf<BeanBanner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        //设置图片集合
        holder.bind(mBanners.map { it.imagePath }.toList())

    }

    override fun getItemCount(): Int = 1

    fun replaceBanners(newBanners: List<BeanBanner>) {
        mBanners.clear()
        mBanners.addAll(newBanners)
        notifyDataSetChanged()
    }
}

class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view), OnBannerListener {
    val mBanner: Banner = view.findViewById<Banner>(R.id.banner)
    var mBanners: List<String>? = null

    init {
        mBanner.setImageLoader(GlideImageLoader())
        mBanner.setIndicatorGravity(BannerConfig.RIGHT)
        mBanner.setOnBannerListener(this)
    }

    fun bind(banners: List<String>) {
        mBanners = banners
        mBanner.setImages(banners)
        //banner设置方法全部调用完毕时最后调用
        mBanner.start()
    }

    companion object {
        fun create(parent: ViewGroup): BannerViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.cell_banner, parent, false)
            return BannerViewHolder(view)
        }
    }

    override fun OnBannerClick(position: Int) {
        mBanners?.apply {
            WebViewActivity.show(get(position))
        }
    }
}