package com.mny.wan.pkg.domain.usecase

import com.mny.mojito.http.MojitoResult
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectUseCase @Inject constructor(
    private val mRepository: WanRepository
) {
    suspend fun collect(id: Int): Flow<MojitoResult<Nothing>> {
        return flow {
            val response = mRepository.collectArticle(id)
            if (response.isSuccess()) {
                emit(MojitoResult.Success(null))
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

    suspend fun cancelCollect(id: Int): Flow<MojitoResult<Nothing>> {
        return flow {
            val response = mRepository.cancelCollectArticle(id)
            if (response.isSuccess()) {
                emit(MojitoResult.Success(null))
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