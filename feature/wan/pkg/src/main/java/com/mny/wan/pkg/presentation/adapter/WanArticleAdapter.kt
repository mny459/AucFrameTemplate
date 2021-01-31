package com.mny.wan.pkg.presentation.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.entension.loadProjectPreview
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject

class WanArticleAdapter @Inject constructor() :
    BaseQuickAdapter<BeanArticle, WanArticleViewHolder>(R.layout.cell_article) {

    init {
        setDiffCallback(COMPARATOR)
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<BeanArticle>() {
            override fun areContentsTheSame(oldItem: BeanArticle, newItem: BeanArticle): Boolean =
                // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                newItem == oldItem || (Objects.equals(newItem.collect, oldItem.collect));

            override fun areItemsTheSame(oldItem: BeanArticle, newItem: BeanArticle): Boolean =
                // 主要关注Id即可
                newItem == oldItem || newItem.id == oldItem.id

            override fun getChangePayload(oldItem: BeanArticle, newItem: BeanArticle): Any =
                PAYLOAD_SCORE
        }
    }

    private var mCollectListener: ((item: BeanArticle, isCollect: Boolean) -> Unit)? =
        null

    fun setOnCollectClickListener(collectListener: ((item: BeanArticle, isCollect: Boolean) -> Unit)) {
        this.mCollectListener = collectListener
    }

    override fun convert(holder: WanArticleViewHolder, item: BeanArticle) {
        holder.bind(item, mCollectListener)
    }

    override fun convert(holder: WanArticleViewHolder, item: BeanArticle, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        if (payloads.isNotEmpty()) {
            val collect = payloads.first() as? Boolean
            collect?.apply {
                holder.getView<ImageView>(R.id.imgCollect).isSelected = this
            }
        }
    }
}


class WanArticleViewHolder(
    private val view: View,
) : BaseViewHolder(view) {
    private var mGroupProject: Group = view.findViewById(R.id.groupProject)
    private var mTvUserType: TextView = view.findViewById(R.id.tvUserType)
    private var mTvPublisher: TextView = view.findViewById(R.id.tvPublisher)
    private var mTvPublishTime: TextView = view.findViewById(R.id.tvPublishTime)
    private var mTvTitle: TextView = view.findViewById(R.id.tvTitle)
    private var mTvDesc: TextView = view.findViewById(R.id.tvDesc)
    private var mTvPin: TextView = view.findViewById(R.id.tvPin)
    private var mTvChapter: TextView = view.findViewById(R.id.tvChapter)
    private var mIvProject: ImageView = view.findViewById(R.id.imgProject)
    private var mIvCollect: ImageView = view.findViewById(R.id.imgCollect)
    private var mItem: BeanArticle? = null
    private var mCollectListener: ((item: BeanArticle, isCollect: Boolean) -> Unit)? =
        null

    fun bind(
        item: BeanArticle,
        collectListener: ((item: BeanArticle, isCollect: Boolean) -> Unit)?
    ) {
        mCollectListener = collectListener
        mItem = item
        item.apply {
            mTvTitle.text = Html.fromHtml(title)
            mTvPublishTime.text = Html.fromHtml(niceDate)
            mTvPublisher.text = if (author.isEmpty()) shareUser.trim() else author.trim()
            mTvUserType.text = if (author.isEmpty()) "分享人" else "作者"
            mTvChapter.text = if (superChapterName.isEmpty()) "$chapterName".trim()
            else "$chapterName·$superChapterName".trim()
            if (superChapterId == 294) {
                // 开源项目主Tab
                mIvProject.loadProjectPreview(envelopePic)
                mTvDesc.text = Html.fromHtml(desc)
                mGroupProject.visibility = View.VISIBLE
            } else {
                mGroupProject.visibility = View.GONE
            }
            view.setOnClickListener {
                WebViewActivity.show(link)
            }
            mIvCollect.isSelected = item.collect
            mIvCollect.setOnClickListener {
                if (!NetworkUtils.isConnected()) {
                    return@setOnClickListener
                }
                if (!UserHelper.isLogin()) {
                    return@setOnClickListener
                }
                mCollectListener?.invoke(item, !mIvCollect.isSelected)
            }
        }

    }
}