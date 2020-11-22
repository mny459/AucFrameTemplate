package com.mny.wan.pkg.domain.repository

import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.data.remote.model.BeanArticleList
import com.mny.wan.pkg.data.remote.model.BeanProject
import kotlinx.coroutines.flow.Flow

interface WanRepository {
    suspend fun fetchHomeArticles(url: String): BaseResponse<BeanArticleList>
    suspend fun fetchProjectTree(): BaseResponse<MutableList<BeanProject>>
}