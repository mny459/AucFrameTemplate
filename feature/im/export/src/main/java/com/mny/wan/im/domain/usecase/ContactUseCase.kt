package com.mny.wan.im.domain.usecase

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.mny.wan.http.Result
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.data.db.entity.UserEntity
import com.mny.wan.im.domain.repository.UserRepository
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