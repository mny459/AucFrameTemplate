package com.mny.wan.pkg.domain.usecase

import androidx.paging.PagingSource
import com.mny.mojito.http.MojitoResult
import com.mny.wan.pkg.data.remote.model.*
import com.mny.wan.pkg.domain.paging.*
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleUseCase @Inject constructor(
    private val mRepository: WanRepository
) {

//    fun homeArticlePageSource(): PagingSource<Int, BeanArticle> {
//        return HomeArticlePageSource(mRepository)
//    }

    suspend fun articlesByUrl(url: String): Flow<MojitoResult<BeanArticleList>> {
        return flow {
            val response = mRepository.fetchArticlesByUrl(url)
            if (response.isSuccess()) {
                emit(MojitoResult.Success(response.data))
            } else {
                emit(MojitoResult.Error(Exception(response.errorMsg)))
            }
        }.onStart {
            emit(MojitoResult.Loading)
        }
            .catch { error ->
                emit(MojitoResult.Error(Exception(error)))
            }.flowOn(Dispatchers.IO)
    }

    fun searchArticlePageSource(keyword: Any): PagingSource<Int, BeanArticle> {
        return SearchArticlePageSource(mRepository, keyword as String)
    }

    fun shareArticlePageSource(): PagingSource<Int, BeanArticle> {
        return ShareArticlePageSource(mRepository)
    }

    fun collectArticlePageSource(): PagingSource<Int, BeanArticle> {
        return CollectArticlePageSource(mRepository)
    }

    fun mineShareArticlePageSource(): PagingSource<Int, BeanArticle> {
        return MineShareArticlePageSource(mRepository)
    }

    fun systemArticlePageSource(cid: Int): PagingSource<Int, BeanArticle> {
        return SystemArticlePageSource(mRepository, cid)
    }

    fun projectArticlePageSource(cid: Int): PagingSource<Int, BeanArticle> {
        return ProjectArticlePageSource(mRepository, cid)
    }

    fun weChatArticlePageSource(cid: Int): PagingSource<Int, BeanArticle> {
        return WeChatArticlePageSource(mRepository, cid)
    }

    fun coinDetailPageSource(): PagingSource<Int, BeanCoinOpDetail> {
        return CoinDetailPageSource(mRepository)
    }

    fun coinRankPageSource(): PagingSource<Int, BeanRanking> {
        return CoinRankPageSource(mRepository)
    }

}