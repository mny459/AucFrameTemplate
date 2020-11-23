package com.mny.wan.pkg.domain.repository

import com.mny.wan.pkg.data.remote.model.*
import kotlinx.coroutines.flow.Flow

interface WanRepository {
    suspend fun fetchHomeArticles(url: String): BaseResponse<BeanArticleList>
    suspend fun fetchProjectTree(): BaseResponse<MutableList<BeanProject>>
    suspend fun fetchWeChatTree(): BaseResponse<MutableList<BeanSystemParent>>
    suspend fun fetchSystemTree(): BaseResponse<MutableList<BeanSystemParent>>
    suspend fun fetchNavTree(): BaseResponse<MutableList<BeanNav>>
}