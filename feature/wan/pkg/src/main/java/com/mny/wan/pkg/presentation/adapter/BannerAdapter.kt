package com.mny.wan.pkg.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import com.youth.banner.Banner
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.util.LogUtils
import javax.inject.Inject

class BannerAdapter @Inject constructor() : RecyclerView.Adapter<BannerViewHolder>() {
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

class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view), OnBannerListener<String> {
    private val mBanner: Banner<String, BannerImageAdapter> = view.findViewById(R.id.banner)
    lateinit var mBannerImageAdapter: BannerImageAdapter

    init {
        mBanner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
        mBanner.setOnBannerListener(this)
    }

    fun bind(banners: List<String>) {
        mBannerImageAdapter = BannerImageAdapter(banners)
        mBanner.adapter = mBannerImageAdapter
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

    override fun OnBannerClick(data: String?, position: Int) {
        com.blankj.utilcode.util.LogUtils.d("OnBannerClick $data $position")
        data?.apply {
            WebViewActivity.show(this)
        }
    }


}