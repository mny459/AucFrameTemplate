package com.mny.wan.pkg.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.mny.wan.pkg.data.remote.model.BeanArticle

data class UiHomeArticle(
    @Embedded
    var home: HomeArticle,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "id",
    )
    var article: BeanArticle,
)

data class UiQaArticle(
    @Embedded
    var qa: QAArticle,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "id",
    )
    var article: BeanArticle,
)