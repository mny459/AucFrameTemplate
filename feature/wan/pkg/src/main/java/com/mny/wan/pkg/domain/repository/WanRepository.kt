package com.mny.wan.pkg.domain.repository

import androidx.paging.PagingSource
import com.mny.wan.pkg.data.local.entity.HomeArticle
import com.mny.wan.pkg.data.local.entity.UiHomeArticle
import com.mny.wan.pkg.data.remote.model.*
import kotlinx.coroutines.flow.Flow

interface WanRepository {
    suspend fun fetchArticlesByUrl(url: String): BaseResponse<BeanArticleList>
    suspend fun fetchTopArticles(): BaseResponse<MutableList<BeanArticle>>
    suspend fun fetchProjectTree(): BaseResponse<MutableList<BeanProject>>
    suspend fun fetchWeChatTree(): BaseResponse<MutableList<BeanSystemParent>>
    suspend fun fetchSystemTree(): BaseResponse<MutableList<BeanSystemParent>>
    suspend fun fetchNavTree(): BaseResponse<MutableList<BeanNav>>
    suspend fun fetchHotKey(): BaseResponse<MutableList<BeanHotKey>>
    suspend fun search(url: String, keyword: String): BaseResponse<BeanArticleList>
    suspend fun fetchBannerList(): BaseResponse<MutableList<BeanBanner>>
    suspend fun fetchCoinList(page: Int): BaseResponse<BaseListData<BeanCoinOpDetail>>
    suspend fun fetchCoinRankList(page: Int): BaseResponse<BaseListData<BeanRanking>>
    suspend fun collectArticle(id: Int): BaseResp
    suspend fun cancelCollectArticle(id: Int): BaseResp
    fun fetchHomeArticles(): PagingSource<Int, UiHomeArticle>
}