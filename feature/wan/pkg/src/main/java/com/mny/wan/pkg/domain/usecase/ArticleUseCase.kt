package com.mny.wan.pkg.domain.usecase

import androidx.paging.PagingSource
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.domain.paging.*
import com.mny.wan.pkg.domain.repository.WanRepository
import javax.inject.Inject

class ArticleUseCase @Inject constructor(
    val mRepository: WanRepository
) {

    fun homeArticlePageSource(): PagingSource<Int, BeanArticle> {
        return HomeArticlePageSource(mRepository)
    }

    fun searchArticlePageSource(): PagingSource<Int, BeanArticle> {
        return SearchArticlePageSource(mRepository)
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

}