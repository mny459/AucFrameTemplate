package com.mny.wan.pkg.data.repository

import com.mny.wan.pkg.base.BaseRepository
import com.mny.wan.pkg.data.remote.model.*
import com.mny.wan.pkg.data.remote.service.WanService
import com.mny.wan.pkg.domain.repository.WanRepository
import javax.inject.Inject

class WanRepositoryImpl @Inject constructor() : BaseRepository(), WanRepository {
    override suspend fun fetchArticlesByUrl(url: String): BaseResponse<BeanArticleList> {
        return mRepository.obtainRetrofitService(WanService::class.java).fetchArticleList(url)
    }

    override suspend fun fetchTopArticles(): BaseResponse<MutableList<BeanArticle>> {
        return mRepository.obtainRetrofitService(WanService::class.java).fetchArticleTopList()
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

    override suspend fun fetchHotKey(): BaseResponse<MutableList<BeanHotKey>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .hotKey()
    }

    override suspend fun search(url: String, keyword: String): BaseResponse<BeanArticleList> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .search(url, keyword)
    }

    override suspend fun fetchBannerList(): BaseResponse<MutableList<BeanBanner>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .fetchBannerList()
    }

    override suspend fun fetchCoinList(page: Int): BaseResponse<BaseListData<BeanCoinOpDetail>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .coinList(page)
    }

    override suspend fun fetchCoinRankList(page: Int):  BaseResponse<BaseListData<BeanRanking>> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .rankList(page)
    }
}