package com.mny.wan.pkg.widget.loadstate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CoinDetailAdapter
import com.mny.wan.pkg.presentation.adapter.CoinRankAdapter

class ArticleLoadStateAdapter(private val mRetry: () -> Unit) :
    LoadStateAdapter<LoadStateFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateFooterViewHolder {
        return LoadStateFooterViewHolder.create(parent) { mRetry.invoke() }
    }
}

class CoinDetailLoadStateAdapter(private val mCoinDetailAdapter: CoinDetailAdapter) :
    LoadStateAdapter<LoadStateFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateFooterViewHolder {
        return LoadStateFooterViewHolder.create(parent) { mCoinDetailAdapter.retry() }
    }
}

class CoinRankLoadStateAdapter(private val mCoinDetailAdapter: CoinRankAdapter) :
    LoadStateAdapter<LoadStateFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateFooterViewHolder {
        return LoadStateFooterViewHolder.create(parent) { mCoinDetailAdapter.retry() }
    }
}

class LoadStateFooterViewHolder(view: View, private val retryCallback: () -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)
    private val retry = view.findViewById<Button>(R.id.retry_button)
        .also {
            it?.setOnClickListener { retryCallback.invoke() }
        }

    fun bindTo(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        errorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): LoadStateFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_footer_state, parent, false)
            return LoadStateFooterViewHolder(view, retryCallback)
        }
    }
}