package com.mny.wan.pkg.domain.repository

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
}