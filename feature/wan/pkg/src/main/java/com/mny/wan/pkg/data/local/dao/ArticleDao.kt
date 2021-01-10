package com.mny.wan.pkg.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.mny.wan.pkg.data.local.entity.HomeArticle
import com.mny.wan.pkg.data.local.entity.UiHomeArticle
import com.mny.wan.pkg.data.local.entity.UiQaArticle
import com.mny.wan.pkg.data.remote.model.BeanArticle

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: BeanArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(article: List<BeanArticle>)

    /**
     */
    @Transaction
    @Query("select * from home_article ORDER BY homePublishTime")
    fun queryHomeArticles(): PagingSource<Int, UiHomeArticle>

    /**
     */
    @Transaction
    @Query("select * from qa_article order by qaPublishTime")
    fun queryQAArticles(): PagingSource<Int, UiQaArticle>

    @Update(entity = BeanArticle::class)
    fun collect(article: BeanArticle)

    @Update(entity = BeanArticle::class)
    fun cancelCollect(article: BeanArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHomeArticles(homes: List<HomeArticle>)
}