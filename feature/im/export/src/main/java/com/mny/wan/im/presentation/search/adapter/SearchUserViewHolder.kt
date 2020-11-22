package com.mny.wan.im.presentation.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.mny.wan.im.R
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.domain.repository.UserRepository
import com.mny.wan.im.presentation.personal.PersonalActivity
import com.mny.wan.im.widget.PortraitView
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.qiujuer.genius.ui.Ui
import net.qiujuer.genius.ui.compat.UiCompat
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable
import net.qiujuer.genius.ui.drawable.LoadingDrawable
import com.mny.wan.http.Result
class SearchUserViewHolder1(private val view: View, private val mRepository: UserRepository) : RecyclerView.ViewHolder(view) {
    private val ivPortrait = view.findViewById<PortraitView>(R.id.iv_portrait)
    private val ivFollow = view.findViewById<ImageView>(R.id.iv_follow)
    private val tvName: TextView = view.findViewById(R.id.tv_name)
    private val cbFollow: ImageView = view.findViewById(R.id.iv_follow)


    fun bind(item: UserCard) {
        ivPortrait.setup(Glide.with(ivPortrait), item)
        tvName.text = item.name
        cbFollow.isEnabled = !item.isFollow
        tvName.setOnClickListener {
            val activity = (itemView.context as ViewComponentManager.FragmentContextWrapper).fragment.activity
            activity?.apply {
                PersonalActivity.go(this, item.id)
            }
        }
        ivPortrait.setOnClickListener {
            val activity = (itemView.context as ViewComponentManager.FragmentContextWrapper).fragment.activity
            activity?.apply {
                PersonalActivity.go(this, item.id)
            }
        }
        ivFollow.setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    startFollowLoading(ivFollow)
                    val result = withContext(Dispatchers.IO) {
                        mRepository.followUser(item.id)
                    }
                    when (result) {
                        is Result.Success -> {
                            // 更改当前界面状态
                            if (ivFollow.drawable is LoadingDrawable) {
                                (ivFollow.drawable as LoadingDrawable).stop()
                                // 设置为默认的
                                ivFollow.setImageResource(R.drawable.sel_opt_done_add)
                            }
                            item.isFollow = result.data?.isFollow ?: item.isFollow
                            ivFollow.setEnabled(!item.isFollow)

                        }
                        is Result.Error -> {
                            if (ivFollow.drawable is LoadingDrawable) {
                                (ivFollow.drawable as LoadingDrawable).stop()
                                // 设置为默认的
                                ivFollow.setImageResource(R.drawable.sel_opt_done_add)
                            }
                        }
                        else -> {
                        }
                    }
                } catch (e: Exception) {
                    LogUtils.e(e)
                }

            }
        }
    }

    private fun startFollowLoading(ivFollow: ImageView) {
        val minSize = Ui.dipToPx(ivFollow.resources, 22f).toInt()
        val maxSize = Ui.dipToPx(ivFollow.resources, 30f).toInt()
        // 初始化一个圆形的动画的Drawable
        // 初始化一个圆形的动画的Drawable
        val drawable: LoadingDrawable = LoadingCircleDrawable(minSize, maxSize)
        drawable.backgroundColor = 0
        val color = intArrayOf(UiCompat.getColor(ivFollow.resources, R.color.white_alpha_208))
        drawable.foregroundColor = color
        // 设置进去
        ivFollow.setImageDrawable(drawable)
        // 启动动画
        drawable.start()
    }

    companion object {
        fun create(parent: ViewGroup, mRepository: UserRepository): SearchUserViewHolder1 {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cell_search_list, parent, false)
            return SearchUserViewHolder1(view,mRepository)
        }
    }

}