package com.mny.wan.pkg.domain.paging

import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.data.UrlManager
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanCoinOpDetail
import com.mny.wan.pkg.data.remote.model.BeanRanking
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.delay

/**
 * PagingSource: 负责真正的数据加载工作
 */
class HomeArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlHomeArticleList(page)
}

class SearchArticlePageSource constructor(mRepository: WanRepository, private val keyword: String) :
    ArticlePageSource(mRepository, 0) {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeanArticle> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: startPage
        return try {
            val suffixUrl = getUrlWithPage(page)
            LogUtils.d("ArticlePageSource $suffixUrl $params")
            val response = mRepository.search(suffixUrl, keyword)
            if (response.isSuccess()) {
                val data = response.data
                LoadResult.Page(
                    data = data.articles,
                    prevKey = null,
                    nextKey = if (data.isLastPage()) null else page + 1,
                    itemsBefore = 0,
                    itemsAfter = 0,
                )
            } else {
                LoadResult.Error(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(Exception(e))
        }
    }

    override fun getUrlWithPage(page: Int): String = UrlManager.urlSearchArticleList(page)
}

class ShareArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlShareArticleList(page)
}

class CoinDetailPageSource constructor(val mRepository: WanRepository) :
    PagingSource<Int, BeanCoinOpDetail>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeanCoinOpDetail> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 1
        return try {
            LogUtils.d("CoinArticlePageSource $page $params")
            val response = mRepository.fetchCoinList(page)
            if (response.isSuccess()) {
                val data = response.data
                LoadResult.Page(
                    data = data.data,
                    prevKey = null,
                    nextKey = if (data.isLastPage()) null else page + 1,
                    itemsBefore = 0,
                    itemsAfter = 0,
                )
            } else {
                LoadResult.Error(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(Exception(e))
        }
    }
}

class CoinRankPageSource constructor(val mRepository: WanRepository) :
    PagingSource<Int, BeanRanking>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeanRanking> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 1
        return try {
            LogUtils.d("CoinArticlePageSource $page $params")
            val response = mRepository.fetchCoinRankList(page)
            if (response.isSuccess()) {
                val data = response.data
                LoadResult.Page(
                    data = data.data,
                    prevKey = null,
                    nextKey = if (data.isLastPage()) null else page + 1,
                    itemsBefore = 0,
                    itemsAfter = 0,
                )
            } else {
                LoadResult.Error(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(Exception(e))
        }
    }
}

class QAArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.qaArticleList(page)
}

class CollectArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlCollectArticleList(page)
}

class MineShareArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 1) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlMineShareArticleList(page)
}

class SystemArticlePageSource constructor(mRepository: WanRepository, private val cid: Int) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlSystemArticleList(page, cid)
}

class ProjectArticlePageSource constructor(mRepository: WanRepository, private val cid: Int) :
    ArticlePageSource(mRepository, 1) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlProjectArticleList(page, cid)
}

class WeChatArticlePageSource constructor(mRepository: WanRepository, private val cid: Int) :
    ArticlePageSource(mRepository, 1) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlWeChatArticleList(page, cid)
}