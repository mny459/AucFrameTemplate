package com.mny.wan.pkg.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.mny.wan.entension.loadProjectPreview
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import java.util.*
import javax.inject.Inject

/**
 * @Author CaiRj
 * @Date 2019/10/17 16:03
 * @Desc PagingDataAdapter: Paging 要求继承的 Adapter
 */
class ArticleAdapter @Inject constructor() :
    PagingDataAdapter<BeanArticle, ArticleViewHolder>(COMPARATOR) {
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
        ArticleViewHolder.create(parent)
}

class ArticleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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

    fun bind(item: BeanArticle?) {
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
//                mIvCollect.isSelected = if (mIsCollectArticle) !mIsCollectArticle else !item.collect
                item.collect = mIvCollect.isSelected
//                mCollectListener?.invoke(helper.adapterPosition, item, imgCollect.isSelected)
            }
        }

    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_article, parent, false)
            return ArticleViewHolder(view)
        }
    }
}
