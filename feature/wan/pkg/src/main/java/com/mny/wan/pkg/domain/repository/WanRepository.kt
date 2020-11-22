package com.mny.wan.pkg.domain.repository

import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.data.remote.model.BeanArticleList

interface WanRepository {
    suspend fun fetchHomeArticles(url: String): BaseResponse<BeanArticleList>
}