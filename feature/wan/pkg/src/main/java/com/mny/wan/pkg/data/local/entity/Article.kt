package com.mny.wan.pkg.data.local.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.mny.wan.pkg.data.remote.model.BeanArticle

/**
 * Article table
 */
//@Entity(
//    tableName = "article",
//    indices = [Index("id", unique = true)]
//)
//data class Article(
//    @PrimaryKey()
//    val id: Int,
//    @SerializedName("apkLink")
//    val apkLink: String = "",
//    @SerializedName("audit")
//    val audit: Int = 0,
//    @SerializedName("author")
//    val author: String = "",
//    @SerializedName("chapterId")
//    val chapterId: Int = 0,
//    @SerializedName("chapterName")
//    val chapterName: String = "",
//    @SerializedName("collect")
//    var collect: Boolean = false,
//    @SerializedName("courseId")
//    val courseId: Int = 0,
//    @SerializedName("desc")
//    val desc: String = "",
//    @SerializedName("envelopePic")
//    val envelopePic: String = "",
//    @SerializedName("fresh")
//    val fresh: Boolean = false,
//    @SerializedName("link")
//    val link: String = "",
//    @SerializedName("niceDate")
//    val niceDate: String = "",
//    @SerializedName("niceShareDate")
//    val niceShareDate: String = "",
//    @SerializedName("origin")
//    val origin: String = "",
//    @SerializedName("prefix")
//    val prefix: String = "",
//    @SerializedName("projectLink")
//    val projectLink: String = "",
//    @SerializedName("publishTime")
//    val publishTime: Long = 0,
//    @SerializedName("selfVisible")
//    val selfVisible: Int = 0,
//    @SerializedName("shareDate")
//    val shareDate: Long = 0,
//    @SerializedName("shareUser")
//    val shareUser: String = "",
//    @SerializedName("superChapterId")
//    val superChapterId: Int = 0,
//    @SerializedName("superChapterName")
//    val superChapterName: String = "",
//    @SerializedName("title")
//    val title: String = "",
//    @SerializedName("type")
//    val type: Int = 0,
//    @SerializedName("userId")
//    val userId: Int = 0,
//    @SerializedName("visible")
//    val visible: Int = 0,
//    @SerializedName("zan")
//    val zan: Int = 0
//)

/**
 * 首页文章
 */
@Entity(
    tableName = "home_article",
    foreignKeys = [ForeignKey(
        entity = BeanArticle::class,
        parentColumns = ["id"],
        childColumns = ["articleId"]
    )],
    indices = [Index("articleId", unique = true)]
)
data class HomeArticle(
    @PrimaryKey()
    var articleId: Int,
    var homePublishTime: Long
)

/**
 * QA文章
 */
@Entity(
    tableName = "qa_article",
    foreignKeys = [ForeignKey(
        entity = BeanArticle::class,
        parentColumns = ["id"],
        childColumns = ["articleId"]
    )],
    indices = [Index("articleId", unique = true)]
)
data class QAArticle(
    @PrimaryKey()
    var articleId: Int,
    var qaPublishTime: Long,
)

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    var articleId: Long,
    var home: Boolean = true,
    var prevKey: Int?,
    var nextKey: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}