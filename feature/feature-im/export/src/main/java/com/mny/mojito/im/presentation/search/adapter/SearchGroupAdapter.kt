package com.mny.mojito.im.presentation.search.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.domain.repository.UserRepository
import com.mny.mojito.im.domain.usecase.FollowUseCase
import com.mny.mojito.im.presentation.personal.PersonalActivity
import com.mny.mojito.im.widget.PortraitView
import dagger.hilt.android.internal.managers.ViewComponentManager
import dagger.hilt.android.qualifiers.ActivityContext
import net.qiujuer.genius.ui.Ui
import net.qiujuer.genius.ui.compat.UiCompat
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable
import net.qiujuer.genius.ui.drawable.LoadingDrawable
import javax.inject.Inject

/**
 * Desc:
 */
class SearchGroupAdapter @Inject constructor(@ActivityContext private val mContext: Context, private val mRepository: UserRepository) : BaseQuickAdapter<GroupCard, SearchGroupViewHolder>(R.layout.cell_search_group_list) {
    override fun convert(holder: SearchGroupViewHolder, item: GroupCard) {
        val ivPortrait = holder.getView<PortraitView>(R.id.iv_portrait)
        ivPortrait.setup(Glide.with(ivPortrait), item.picture)
        holder.setText(R.id.tv_name, item.name)
        holder.setEnabled(R.id.iv_join, item.joinAt == null)
        holder.getView<View>(R.id.iv_join).setOnClickListener {
            val activity = (holder.itemView.context as ViewComponentManager.FragmentContextWrapper).fragment.activity
            activity?.apply {
                PersonalActivity.go(this, item.ownerId)
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

    override fun createBaseViewHolder(view: View): SearchGroupViewHolder {
        return SearchGroupViewHolder(view, mRepository)
    }
}

class SearchGroupViewHolder(view: View, private val mRepository: UserRepository) : BaseViewHolder(view) {
    val mUserCaseImpl: FollowUseCase by lazy { FollowUseCase(mRepository) }
}