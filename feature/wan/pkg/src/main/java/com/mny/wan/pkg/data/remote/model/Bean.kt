package com.mny.wan.pkg.data.remote.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mny.wan.pkg.data.remote.service.WanApi
import java.io.Serializable


/**
 * @Author CaiRj
 * @Date 2019/10/17 10:24
 * @Desc
 */

data class BaseResp(val errorCode: Int, val errorMsg: String) {
    fun isSuccess(): Boolean = errorCode == WanApi.RequestSuccess
}

data class BaseListData<T>(
    @SerializedName("curPage")
    val curPage: Int = 0, // 1
    @SerializedName("datas")
    val data: MutableList<T> = mutableListOf(),
    @SerializedName("offset")
    val offset: Int = 0, // 0
    @SerializedName("over")
    val over: Boolean = false, // false
    @SerializedName("pageCount")
    val pageCount: Int = 0, // 208
    @SerializedName("size")
    val size: Int = 0, // 30
    @SerializedName("total")
    val total: Int = 0 // 6240
) {
    fun isLastPage(): Boolean = curPage >= 5
}

data class BeanArticleList(
    @SerializedName("datas")
    val articles: MutableList<BeanArticle> = mutableListOf(),
    @SerializedName("curPage")
    val curPage: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("over")
    val over: Boolean = false,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("total")
    val total: Int = 0
) {
    fun isLastPage(): Boolean = curPage >= 5

    override fun toString(): String {
        return "BeanArticleList(curPage=$curPage, offset=$offset, over=$over, pageCount=$pageCount, size=$size, total=$total)"
    }
}

@Entity(
    tableName = "article",
    indices = [Index("id", unique = true)]
)
data class BeanArticle(
    @SerializedName("apkLink")
    var apkLink: String = "",
    @SerializedName("audit")
    var audit: Int = 0,
    @SerializedName("author")
    var author: String = "",
    @SerializedName("chapterId")
    var chapterId: Int = 0,
    @SerializedName("chapterName")
    var chapterName: String = "",
    @SerializedName("collect")
    var collect: Boolean = false,
    @SerializedName("courseId")
    var courseId: Int = 0,
    @SerializedName("desc")
    var desc: String = "",
    @SerializedName("envelopePic")
    var envelopePic: String = "",
    @SerializedName("fresh")
    var fresh: Boolean = false,
    @PrimaryKey()
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("link")
    var link: String = "",
    @SerializedName("niceDate")
    var niceDate: String = "",
    @SerializedName("niceShareDate")
    var niceShareDate: String = "",
    @SerializedName("origin")
    var origin: String = "",
    @SerializedName("prefix")
    var prefix: String = "",
    @SerializedName("projectLink")
    var projectLink: String = "",
    @SerializedName("publishTime")
    var publishTime: Long = 0,
    @SerializedName("selfVisible")
    var selfVisible: Int = 0,
    @SerializedName("shareDate")
    var shareDate: Long = 0,
    @SerializedName("shareUser")
    var shareUser: String = "",
    @SerializedName("superChapterId")
    var superChapterId: Int = 0,
    @SerializedName("superChapterName")
    var superChapterName: String = "",
    @Ignore
    @SerializedName("tags")
    var tags: List<Tag> = emptyList<Tag>(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("userId")
    var userId: Int = 0,
    @SerializedName("visible")
    var visible: Int = 0,
    @SerializedName("zan")
    var zan: Int = 0
)

data class Tag(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = ""
)

// 项目
data class BeanProject(
    @SerializedName("children")
    val children: List<Any> = listOf(),
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("parentChapterId")
    val parentChapterId: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 0
)

// 体系
data class BeanSystemParent(
    @SerializedName("children")
    val children: List<BeanSystemChildren> = listOf(),
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("parentChapterId")
    val parentChapterId: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 0
) : Serializable

data class BeanSystemChildren(
    @SerializedName("children")
    val children: List<BeanSystemChildren> = listOf(),
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("parentChapterId")
    val parentChapterId: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 0
) : Serializable

data class BeanMultiType(val data: Any, val type: Int) {
    companion object {
        const val TYPE_PARENT = 0
        const val TYPE_CHILD = 1
    }
}

data class BeanNav(
    @SerializedName("articles")
    val articles: List<BeanArticle> = listOf(),
    @SerializedName("cid")
    val cid: Int = 0,
    @SerializedName("name")
    val name: String = ""
)

data class BeanUserInfo(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)

data class BeanBanner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)

// 积分信息
data class BeanCoin(
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("level")
    val level: Int = 0, // 18
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("username")
    val username: String = ""

)

// 积分明细
data class BeanCoinOpDetail(
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("date")
    val date: Long = 0,
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("userName")
    val userName: String = ""
)

data class BeanRanking(
    @SerializedName("coinCount")
    val coinCount: Int = 0, // 2334
    @SerializedName("rank")
    val rank: Int = 0, // 0
    @SerializedName("userId")
    val userId: Int = 0, // 20382
    @SerializedName("username")
    val username: String = "" // g**eii
)

data class BeanHotKey(
    @SerializedName("id")
    val id: Int = 0, // 6
    @SerializedName("link")
    val link: String = "",
    @SerializedName("name")
    val name: String = "", // 面试
    @SerializedName("order")
    val order: Int = 0, // 1
    @SerializedName("visible")
    val visible: Int = 0 // 1
)


data class BeanMineShareArticle(
    @SerializedName("coinInfo")
    val coinInfo: BeanCoin?,
    @SerializedName("shareArticles")
    val shareArticles: BeanArticleList?
)
