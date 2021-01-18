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
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.mny.mojito.entension.loadProjectPreview
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.local.entity.UiHomeArticle
import com.mny.wan.pkg.data.local.entity.UiQaArticle
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.event.CollectEvent
import com.mny.wan.pkg.presentation.AppViewModel
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * @Author CaiRj
 * @Date 2019/10/17 16:03
 * @Desc PagingDataAdapter: Paging 要求继承的 Adapter
 */
class ArticleAdapter @Inject constructor(private val mAppViewModel: AppViewModel) :
    PagingDataAdapter<BeanArticle, ArticleViewHolder>(COMPARATOR) {
    var viewLifecycleOwner: LifecycleOwner? = null

    companion object {
        private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<BeanArticle>() {
            override fun areContentsTheSame(oldItem: BeanArticle, newItem: BeanArticle): Boolean =
                // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                newItem == oldItem || (Objects.equals(newItem.collect, oldItem.collect));

            override fun areItemsTheSame(oldItem: BeanArticle, newItem: BeanArticle): Boolean =

                // 主要关注Id即可
                newItem == oldItem || newItem.id == oldItem.id

            override fun getChangePayload(oldItem: BeanArticle, newItem: BeanArticle): Any? =
                PAYLOAD_SCORE
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            // 更新收藏
            val item = getItem(position)
//            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder.create(parent, mAppViewModel, viewLifecycleOwner!!)

    override fun onViewDetachedFromWindow(holder: ArticleViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelObserverCollect()
    }

    fun notifyCollectStateChanged(event: CollectEvent) {

    }
}

class HomeArticleAdapter @Inject constructor(private val mAppViewModel: AppViewModel) :
    PagingDataAdapter<UiHomeArticle, ArticleViewHolder>(COMPARATOR) {
    var viewLifecycleOwner: LifecycleOwner? = null

    companion object {
        private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<UiHomeArticle>() {
            override fun areContentsTheSame(oldItem: UiHomeArticle, newItem: UiHomeArticle): Boolean =
                // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                newItem == oldItem || (Objects.equals(newItem.article.collect, oldItem.article.collect));

            override fun areItemsTheSame(oldItem: UiHomeArticle, newItem: UiHomeArticle): Boolean =

                // 主要关注Id即可
                newItem == oldItem || newItem.article.id == oldItem.article.id

            override fun getChangePayload(oldItem: UiHomeArticle, newItem: UiHomeArticle): Any? =
                PAYLOAD_SCORE
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this.article)
        }
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            // 更新收藏
            val item = getItem(position)
//            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder.create(parent, mAppViewModel, viewLifecycleOwner!!)

    override fun onViewDetachedFromWindow(holder: ArticleViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelObserverCollect()
    }

    fun notifyCollectStateChanged(event: CollectEvent) {

    }
}


class QAArticleAdapter @Inject constructor(private val mAppViewModel: AppViewModel) :
    PagingDataAdapter<UiQaArticle, ArticleViewHolder>(COMPARATOR) {
    var viewLifecycleOwner: LifecycleOwner? = null

    companion object {
        private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<UiQaArticle>() {
            override fun areContentsTheSame(oldItem: UiQaArticle, newItem: UiQaArticle): Boolean =
                // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                newItem == oldItem || (Objects.equals(newItem.article.collect, oldItem.article.collect))

            override fun areItemsTheSame(oldItem: UiQaArticle, newItem: UiQaArticle): Boolean =

                // 主要关注Id即可
                newItem == oldItem || newItem.article.id == oldItem.article.id

            override fun getChangePayload(oldItem: UiQaArticle, newItem: UiQaArticle): Any? =
                PAYLOAD_SCORE
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this.article)
        }
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            // 更新收藏
            val item = getItem(position)
//            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder.create(parent, mAppViewModel, viewLifecycleOwner!!)

    override fun onViewDetachedFromWindow(holder: ArticleViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelObserverCollect()
    }

    fun notifyCollectStateChanged(event: CollectEvent) {

    }
}

class ArticleViewHolder(
    private val view: View,
    val viewModel: AppViewModel,
    val viewLifecycleOwner: LifecycleOwner
) :
    RecyclerView.ViewHolder(view) {
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
    private var mCollectJob: Job? = null
    private var mCancelCollectJob: Job? = null
    private var mItem: BeanArticle? = null
    fun bind(item: BeanArticle?) {
        mItem = item
        item?.apply {
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
                if (item.collect) {
                    viewModel.cancelCollect(id)
                } else {
                    viewModel.collect(id)
                }
            }
            observerCollect(this)
        }

    }

    private fun observerCollect(item: BeanArticle) {
        viewLifecycleOwner.apply {
            mCollectJob = lifecycleScope.launch {
                viewModel.collectIds.collect {
//                    LogUtils.d("收藏的id $it 当前文章 ${item.id}")
                    if (it != 0 && it == item.id) {
                        item.collect = true
                        mIvCollect.isSelected = item.collect
                    }
                }
            }
            mCancelCollectJob = lifecycleScope.launch {
                viewModel.cancelCollectIds.collect {
//                    LogUtils.d("取消收藏的id $it 当前文章 ${item.id}")
                    if (it != 0 && it == item.id) {
                        item.collect = false
                        mIvCollect.isSelected = item.collect
                    }
                }
            }

        }
    }

    fun cancelObserverCollect() {
        mCollectJob?.cancel()
        mCancelCollectJob?.cancel()
    }

    companion object {
        fun create(
            parent: ViewGroup,
            viewModel: AppViewModel,
            viewLifecycleOwner: LifecycleOwner
        ): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_article, parent, false)
            return ArticleViewHolder(view, viewModel, viewLifecycleOwner)
        }
    }
}
