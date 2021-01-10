package com.mny.wan.pkg.domain.usecase

import androidx.paging.PagingSource
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanCoinOpDetail
import com.mny.wan.pkg.data.remote.model.BeanRanking
import com.mny.wan.pkg.domain.paging.*
import com.mny.wan.pkg.domain.repository.WanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleUseCase @Inject constructor(
    private val mRepository: WanRepository
) {

//    fun homeArticlePageSource(): PagingSource<Int, BeanArticle> {
//        return HomeArticlePageSource(mRepository)
//    }

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

    fun qAArticlePageSource(): PagingSource<Int, BeanArticle> {
        return QAArticlePageSource(mRepository)
    }

    fun coinDetailPageSource(): PagingSource<Int, BeanCoinOpDetail> {
        return CoinDetailPageSource(mRepository)
    }

    fun coinRankPageSource(): PagingSource<Int, BeanRanking> {
        return CoinRankPageSource(mRepository)
    }

}