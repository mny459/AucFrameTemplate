package com.mny.wan.pkg.data.repository

import com.mny.wan.pkg.base.BaseRepository
import com.mny.wan.pkg.data.remote.model.*
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

    override suspend fun fetchWeChatTree(): BaseResponse<MutableList<BeanSystemParent>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .fetchWeChatTree()
    }

    override suspend fun fetchSystemTree(): BaseResponse<MutableList<BeanSystemParent>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .fetchSystemTree()
    }

    override suspend fun fetchNavTree(): BaseResponse<MutableList<BeanNav>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .fetchNavTree()
    }
}