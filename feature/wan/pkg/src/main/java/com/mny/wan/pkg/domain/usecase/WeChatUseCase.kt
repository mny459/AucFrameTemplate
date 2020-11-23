package com.mny.wan.pkg.domain.usecase

import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.remote.model.BeanProject
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.domain.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class WeChatUseCase @Inject constructor(private val mRepository: WanRepository) {
    suspend fun fetchProjectTree(): Flow<MojitoResult<MutableList<BeanSystemParent>>> {
        return flow<MojitoResult<MutableList<BeanSystemParent>>> {
            val response = mRepository.fetchWeChatTree()
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