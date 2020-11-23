package com.mny.wan.pkg.domain.paging

import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.data.UrlManager
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * PagingSource: 负责真正的数据加载工作
 */
abstract class ArticlePageSource constructor(
    protected val mRepository: WanRepository,
    protected val startPage: Int
) :
    PagingSource<Int, BeanArticle>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeanArticle> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: startPage
        return try {
            delay(500)
            val suffixUrl = getUrlWithPage(page)
            LogUtils.d("ArticlePageSource $suffixUrl $params")
            val response = mRepository.fetchHomeArticles(suffixUrl)
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

    abstract fun getUrlWithPage(page: Int): String
}