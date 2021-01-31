package com.mny.wan.pkg.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.entension.loadProjectPreview
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.presentation.AppViewModel
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TopArticleAdapter @Inject constructor() :
    BaseQuickAdapter<BeanArticle, TopArticleViewHolder>(R.layout.cell_article) {
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

    override fun convert(holder: TopArticleViewHolder, item: BeanArticle) {
        holder.bind(item, mCollectListener)
    }

    override fun convert(holder: TopArticleViewHolder, item: BeanArticle, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        if (payloads.isNotEmpty()) {
            val collect = payloads.first() as? Boolean
            collect?.apply {
                holder.getView<ImageView>(R.id.imgCollect).isSelected = this
            }
        }
    }

}

class TopArticleViewHolder(
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
    private var mCollectListener: ((item: BeanArticle, isCollect: Boolean) -> Unit)? =
        null

    fun bind(
        item: BeanArticle,
        collectListener: ((item: BeanArticle, isCollect: Boolean) -> Unit)?
    ) {
        mCollectListener = collectListener
        item.apply {
            mTvTitle.text = Html.fromHtml(title)
            mTvPublishTime.text = Html.fromHtml(niceDate)
            mTvPin.visibility = View.VISIBLE
            mTvPublisher.text = if (author.isEmpty()) shareUser.trim() else author.trim()
            mTvUserType.text =
                if (author.isEmpty()) StringUtils.getString(R.string.wan_share_user) else StringUtils.getString(
                    R.string.wan_author
                )
            mTvChapter.text = if (superChapterName.isEmpty()) "$chapterName".trim()
            else "$chapterName·$superChapterName".trim()
            if (superChapterId == 294) {
                // 开源项目主Tab
                mIvProject.loadProjectPreview(envelopePic)
                mTvTitle.text = Html.fromHtml(desc)
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