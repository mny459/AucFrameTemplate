package com.mny.wan.pkg.domain.usecase

import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.data.remote.model.BeanProject
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val mRepository: WanRepository) {
    suspend fun fetchHotKey(): Flow<MojitoResult<MutableList<BeanHotKey>>> {
        return flow {
            val response = mRepository.fetchHotKey()
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