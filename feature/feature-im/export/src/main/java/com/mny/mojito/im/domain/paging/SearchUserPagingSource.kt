package com.mny.mojito.im.domain.paging

import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.domain.repository.UserRepository
import com.mny.mojito.im.domain.usecase.SearchUserUseCase
import javax.inject.Inject

class SearchUserPagingSource @Inject constructor(private val mSearchUseCase: UserRepository) : PagingSource<String, UserCard>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserCard> {
        return try {
            LogUtils.d("SearchUserPagingSource ")
            val response = mSearchUseCase.searchUser(params.key!!)
            if (response.isSuccess()) {
                LoadResult.Page(data = response.result, prevKey = null, nextKey = null)
            } else {
                LoadResult.Error(Exception(response.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(Exception(e))
        }

    }
}