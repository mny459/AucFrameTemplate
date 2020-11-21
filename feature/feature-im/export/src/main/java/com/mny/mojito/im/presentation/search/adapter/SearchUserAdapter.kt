package com.mny.mojito.im.presentation.search.adapter

import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.http.Result
import com.mny.mojito.im.R
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.domain.repository.UserRepository
import com.mny.mojito.im.domain.usecase.FollowUseCase
import com.mny.mojito.im.presentation.personal.PersonalActivity
import com.mny.mojito.im.widget.PortraitView
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.qiujuer.genius.ui.Ui
import net.qiujuer.genius.ui.compat.UiCompat
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable
import net.qiujuer.genius.ui.drawable.LoadingDrawable
import javax.inject.Inject

/**
 * Desc:
 */
class SearchUserAdapter @Inject constructor(private val mRepository: UserRepository) : BaseQuickAdapter<UserCard, SearchUserViewHolder>(R.layout.cell_search_list) {
    override fun convert(holder: SearchUserViewHolder, item: UserCard) {
        val ivPortrait = holder.getView<PortraitView>(R.id.iv_portrait)
        val ivFollow = holder.getView<ImageView>(R.id.iv_follow)
        ivPortrait.setup(Glide.with(ivPortrait), item)

        holder.setText(R.id.tv_name, item.name)
        holder.setEnabled(R.id.iv_follow, !item.isFollow)
        holder.getView<View>(R.id.tv_name).setOnClickListener {
            val activity = (holder.itemView.context as ViewComponentManager.FragmentContextWrapper).fragment.activity
            activity?.apply {
                PersonalActivity.go(this, item.id)
            }
        }
        holder.getView<View>(R.id.iv_portrait).setOnClickListener {
            val activity = (holder.itemView.context as ViewComponentManager.FragmentContextWrapper).fragment.activity
            activity?.apply {
                PersonalActivity.go(this, item.id)
            }
        }
        holder.getView<View>(R.id.iv_follow).setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    startFollowLoading(ivFollow)
                    val result = withContext(Dispatchers.IO) {
                        holder.mUserCaseImpl.followUser(item.id)
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
                            holder.setEnabled(R.id.iv_follow, !item.isFollow)

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

    override fun createBaseViewHolder(view: View): SearchUserViewHolder {
        return SearchUserViewHolder(view, mRepository)
    }
}

class SearchUserViewHolder(view: View, private val mRepository: UserRepository) : BaseViewHolder(view) {
    val mUserCaseImpl: FollowUseCase by lazy { FollowUseCase(mRepository) }
}