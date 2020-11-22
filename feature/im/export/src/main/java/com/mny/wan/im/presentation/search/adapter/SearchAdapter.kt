package com.mny.wan.im.presentation.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.domain.repository.UserRepository
import java.util.*
import javax.inject.Inject

class SearchAdapter @Inject constructor(private val mRepository: UserRepository) : PagingDataAdapter<UserCard, SearchUserViewHolder1>(SEARCH_COMPARATOR) {
    override fun onBindViewHolder(holder: SearchUserViewHolder1, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    override fun onBindViewHolder(
            holder: SearchUserViewHolder1,
            position: Int,
            payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
//            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SearchUserViewHolder1 = SearchUserViewHolder1.create(parent, mRepository)


    companion object {
        private val PAYLOAD_SCORE = Any()
        val SEARCH_COMPARATOR = object : DiffUtil.ItemCallback<UserCard>() {
            override fun areContentsTheSame(oldItem: UserCard, newItem: UserCard): Boolean =
                    // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                    newItem == oldItem || (
                            Objects.equals(newItem.name, oldItem.name)
                                    && Objects.equals(newItem.portrait, oldItem.portrait)
                                    && Objects.equals(newItem.sex, oldItem.sex)
                                    && Objects.equals(newItem.isFollow, oldItem.isFollow)
                            );

            override fun areItemsTheSame(oldItem: UserCard, newItem: UserCard): Boolean =

                    // 主要关注Id即可
                    newItem == oldItem || newItem.id == oldItem.id

            override fun getChangePayload(oldItem: UserCard, newItem: UserCard): Any? = PAYLOAD_SCORE
        }
    }
}