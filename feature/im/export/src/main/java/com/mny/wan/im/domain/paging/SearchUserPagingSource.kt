package com.mny.wan.im.domain.paging

import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.domain.repository.UserRepository
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