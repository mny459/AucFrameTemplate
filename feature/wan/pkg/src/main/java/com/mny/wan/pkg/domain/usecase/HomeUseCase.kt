package com.mny.wan.pkg.domain.usecase

import androidx.paging.PagingSource
import com.mny.mojito.http.MojitoResult
import com.mny.wan.pkg.data.local.entity.HomeArticle
import com.mny.wan.pkg.data.local.entity.UiHomeArticle
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.domain.paging.HomeArticlePageSource
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val mRepository: WanRepository) {

    fun homeArticlePageSource(): PagingSource<Int, UiHomeArticle> {
        return mRepository.fetchHomeArticles()
    }

    fun fetchTopArticles(): Flow<MojitoResult<List<BeanArticle>>> {
        return flow {
            val response = mRepository.fetchTopArticles()
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

    fun fetchBannerList(): Flow<MojitoResult<List<BeanBanner>>> {
        return flow {
            val response = mRepository.fetchBannerList()
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
}