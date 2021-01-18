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


@OptIn(ExperimentalPagingApi::class)
class QAArticlePageSource @Inject constructor(
    private val mRepository: WanRepository,
    private val mDataBase: WanDataBase
) :
    RemoteMediator<Int, UiQaArticle>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UiQaArticle>
    ): MediatorResult {
        LogUtils.d("load $loadType")
        val page: Int = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> {
                // 不支持下拉加载
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                LogUtils.d("load 下一页 $remoteKeys")
                remoteKeys?.nextKey ?: 1 // return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        // 如果key是null，那就加载第0页的数据
        return try {
            val suffixUrl = UrlManager.qaArticleList(page)
            LogUtils.d("load HomeArticlePageSource $suffixUrl")
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
                    val prevKey = data.curPage - 1
                    val nextKey = data.curPage + 1
                    val qaList = data.articles.map {
                        QAArticle(articleId = it.id, qaPublishTime = it.publishTime)
                    }
                    val keys = data.articles.map {
                        RemoteKeys(
                            articleId = it.id.toLong(),
                            prevKey = prevKey,
                            nextKey = nextKey,
                            home = false
                        )
                    }
                    mDataBase.articleDao().insertArticles(data.articles)
                    mDataBase.articleDao().insertQAArticles(qaList)
                    mDataBase.remoteKeysDao().insertAll(keys)
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

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UiQaArticle>): RemoteKeys? {
        // 获取当前数据的最后一页的最后一个数据
        val lastPage = state.pages.lastOrNull()
        val lastItem = lastPage?.data?.lastOrNull()
        LogUtils.d("最后一个 Item 是 id ${lastItem?.article?.id} title ${lastItem?.article?.title}")
        return if (lastItem != null) {
            mDataBase.remoteKeysDao()
                .remoteKeysRepoId(lastItem.article.id.toLong(), homeArticle = false)
        } else {
            null
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UiQaArticle>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                mDataBase.remoteKeysDao()
                    .remoteKeysRepoId(repo.article.id.toLong(), homeArticle = false)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UiQaArticle>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.article?.id?.let { articleId ->
                mDataBase.remoteKeysDao().remoteKeysRepoId(articleId.toLong(), homeArticle = false)
            }
        }
    }
}