package com.mny.wan.pkg.domain.usecase

import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val mRepository: WanRepository) {

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