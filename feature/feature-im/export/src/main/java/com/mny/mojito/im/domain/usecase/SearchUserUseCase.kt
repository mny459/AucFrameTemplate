package com.mny.mojito.im.domain.usecase

import androidx.annotation.WorkerThread
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class SearchUserUseCase @Inject constructor(private val mRepository: UserRepository) {

    @WorkerThread
    suspend fun searchUser(username: String): Flow<Result<List<UserCard>>> {
        return flow<Result<List<UserCard>>> {
            val response = mRepository.searchUser(username)
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }.onStart {
            emit(Result.Loading)
        }.catch { error ->
            emit(Result.Error(Exception(error)))
        }.flowOn(Dispatchers.IO)
    }
}