package com.mny.wan.pkg.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanRanking
import javax.inject.Inject

/**
 * @Author CaiRj
 * @Date 2019/10/17 16:03
 * @Desc PagingDataAdapter: Paging 要求继承的 Adapter
 */
class CoinRankAdapter @Inject constructor() :
    PagingDataAdapter<BeanRanking, CoinRankViewHolder>(COMPARATOR) {
    companion object {
        private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<BeanRanking>() {
            override fun areContentsTheSame(
                oldItem: BeanRanking,
                newItem: BeanRanking
            ): Boolean =
                // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                newItem == oldItem || (newItem.coinCount == oldItem.coinCount && newItem.rank == oldItem.rank)

            override fun areItemsTheSame(
                oldItem: BeanRanking,
                newItem: BeanRanking
            ): Boolean =

                // 主要关注Id即可
                newItem == oldItem || newItem.userId == oldItem.userId

            override fun getChangePayload(
                oldItem: BeanRanking,
                newItem: BeanRanking
            ): Any? =
                PAYLOAD_SCORE
        }
    }

    override fun onBindViewHolder(holder: CoinRankViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this, position)
        }
    }

    override fun onBindViewHolder(
        holder: CoinRankViewHolder,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinRankViewHolder =
        CoinRankViewHolder.create(parent)
}

class CoinRankViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private var mTvNo: TextView = view.findViewById(R.id.tvNo)
    private var mTvNam: TextView = view.findViewById(R.id.tvName)
    private var mTvCoin: TextView = view.findViewById(R.id.tvCoin)

    fun bind(item: BeanRanking?, position: Int) {
        item?.apply {
            mTvNo.text = "${position + 1}"
            mTvNam.text = username
            mTvCoin.text = "$coinCount"
        }

    }

    companion object {
        fun create(parent: ViewGroup): CoinRankViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_coin_ranking, parent, false)
            return CoinRankViewHolder(view)
        }
    }
}
