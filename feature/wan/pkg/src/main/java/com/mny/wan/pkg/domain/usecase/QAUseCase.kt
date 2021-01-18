package com.mny.wan.pkg.domain.usecase

import androidx.paging.PagingSource
import com.mny.mojito.http.MojitoResult
import com.mny.wan.pkg.data.local.entity.HomeArticle
import com.mny.wan.pkg.data.local.entity.UiHomeArticle
import com.mny.wan.pkg.data.local.entity.UiQaArticle
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.domain.paging.HomeArticlePageSource
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class QAUseCase @Inject constructor(private val mRepository: WanRepository) {

    fun qaArticlePageSource(): PagingSource<Int, UiQaArticle> {
        return mRepository.fetchQAArticles()
    }

}