package com.mny.wan.pkg.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanCoinOpDetail
import java.util.*
import javax.inject.Inject

/**
 * @Author CaiRj
 * @Date 2019/10/17 16:03
 * @Desc PagingDataAdapter: Paging 要求继承的 Adapter
 */
class CoinDetailAdapter @Inject constructor() :
    PagingDataAdapter<BeanCoinOpDetail, CoinDetailViewHolder>(COMPARATOR) {
    companion object {
        private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<BeanCoinOpDetail>() {
            override fun areContentsTheSame(
                oldItem: BeanCoinOpDetail,
                newItem: BeanCoinOpDetail
            ): Boolean =
                // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
                newItem == oldItem || (Objects.equals(newItem.id, oldItem.id));

            override fun areItemsTheSame(
                oldItem: BeanCoinOpDetail,
                newItem: BeanCoinOpDetail
            ): Boolean =

                // 主要关注Id即可
                newItem == oldItem || newItem.id == oldItem.id

            override fun getChangePayload(
                oldItem: BeanCoinOpDetail,
                newItem: BeanCoinOpDetail
            ): Any? =
                PAYLOAD_SCORE
        }
    }

    override fun onBindViewHolder(holder: CoinDetailViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    override fun onBindViewHolder(
        holder: CoinDetailViewHolder,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinDetailViewHolder =
        CoinDetailViewHolder.create(parent)
}

class CoinDetailViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private var mTvType: TextView = view.findViewById(R.id.tvType)
    private var mTvTime: TextView = view.findViewById(R.id.tvTime)
    private var mTvCoin: TextView = view.findViewById(R.id.tvCoin)

    fun bind(item: BeanCoinOpDetail?) {
        item?.apply {
            mTvType.text = reason
            mTvTime.text = TimeUtils.millis2String(date)
            try {
                mTvCoin.text = desc.subSequence(desc.indexOfLast { it == '：' } + 1, desc.length)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    companion object {
        fun create(parent: ViewGroup): CoinDetailViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_coin_detail, parent, false)
            return CoinDetailViewHolder(view)
        }
    }
}
