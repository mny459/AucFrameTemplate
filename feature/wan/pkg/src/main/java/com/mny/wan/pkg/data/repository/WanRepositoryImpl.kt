package com.mny.wan.pkg.data.repository

import com.mny.wan.pkg.base.BaseRepository
import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.data.remote.model.BeanArticleList
import com.mny.wan.pkg.data.remote.model.BeanProject
import com.mny.wan.pkg.data.remote.service.WanService
import com.mny.wan.pkg.domain.repository.WanRepository
import javax.inject.Inject

class WanRepositoryImpl @Inject constructor() : BaseRepository(), WanRepository {
    override suspend fun fetchHomeArticles(url: String): BaseResponse<BeanArticleList> {
        return mRepository.obtainRetrofitService(WanService::class.java).fetchArticleList(url)
    }

    override suspend fun fetchProjectTree(): BaseResponse<MutableList<BeanProject>> {
        return mRepository.obtainRetrofitService(WanService::class.java).fetchProjectTree()
    }
}