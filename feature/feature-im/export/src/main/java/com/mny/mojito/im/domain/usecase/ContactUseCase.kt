package com.mny.mojito.im.domain.usecase

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.domain.repository.UserRepository
import javax.inject.Inject


class ContactUseCase @Inject constructor(private val mRepository: UserRepository) {
    @WorkerThread
    fun getContactsFromLocal(): LiveData<List<UserEntity>> {
        return mRepository.getContactsFromLocal()
    }
    @WorkerThread
    suspend fun getContacts(): Result<List<UserCard>> {
        return mRepository.getContacts()
    }
}