package com.mny.wan.pkg.domain.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.data.UrlManager
import com.mny.wan.pkg.data.local.WanDataBase
import com.mny.wan.pkg.data.local.entity.QAArticle
import com.mny.wan.pkg.data.local.entity.RemoteKeys
import com.mny.wan.pkg.data.local.entity.UiQaArticle
import com.mny.wan.pkg.domain.repository.WanRepository
import javax.inject.Inject

/**
 * 由于使用 RemoteMediator 的目的是为了使用 Room 数据的实时更新特性
 * 来实现文章的收藏多页面同步问题，所以这里就没有不使用 RemoteKeys 了
 */
@OptIn(ExperimentalPagingApi::class)
class QAArticlePageSource @Inject constructor(
    private val mRepository: WanRepository,
    private val mDataBase: WanDataBase
) :
    RemoteMediator<Int, UiQaArticle>() {
    private val firstPage = 1
    private var curPage = firstPage

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UiQaArticle>
    ): MediatorResult {
        LogUtils.d("load $loadType ${state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.last()?.article?.title}")
        val page: Int = when (loadType) {
            LoadType.REFRESH -> firstPage
            LoadType.PREPEND -> {
                // 不支持下拉加载
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> ++curPage
        }
        val suffixUrl = UrlManager.qaArticleList(page)
        LogUtils.d("load QAArticlePageSource $suffixUrl")
        return try {
            val response = mRepository.fetchArticlesByUrl(suffixUrl)

            if (response.isSuccess()) {
                val data = response.data
                LogUtils.d("load data $data")
                val endOfPaginationReached = data.isLastPage()
                mDataBase.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        mDataBase.articleDao().clearQaArticles()
                    }
                    curPage = data.curPage
                    val qaList = data.articles.map {
                        QAArticle(articleId = it.id, qaPublishTime = it.publishTime)
                    }
                    mDataBase.articleDao().insertArticles(data.articles)
                    mDataBase.articleDao().insertQAArticles(qaList)
                }
                MediatorResult.Success(endOfPaginationReached)
            } else {
                MediatorResult.Error(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(Exception(e))
        }
    }
}