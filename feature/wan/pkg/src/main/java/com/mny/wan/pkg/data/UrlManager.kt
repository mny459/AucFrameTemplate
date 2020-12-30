package com.mny.wan.pkg.data

/**
 * @Author Mny459
 * @Date 2019/10/17 18:12
 * @Desc: WANAndroid 大部分 api
 */
object UrlManager {

    fun urlHomeArticleList(page: Int): String = "/article/list/${page}/json"

    fun urlSystemArticleList(page: Int, cid: Int): String = "/article/list/${page}/json?cid=${cid}"

    fun urlProjectArticleList(page: Int, cid: Int): String =
        "/project/list/${page}/json?cid=${cid}"

    fun urlWeChatArticleList(page: Int, id: Int): String =
        "/wxarticle/list/${id}/${page}/json"

    fun urlSearchArticleList(page: Int): String =
        "/article/query/$page/json"

    fun urlShareArticleList(page: Int): String =
        "/user_article/list/$page/json"

    fun urlCollectArticleList(page: Int): String =
        "/lg/collect/list/${page}/json"

    fun urlMineShareArticleList(page: Int): String =
        "/user/lg/private_articles/${page}/json"

    fun qaArticleList(page: Int): String =
        "/wenda/list/${page}/json"

    fun coinArticleList(page: Int): String =
        "/lg/coin/list/${page}/json"

}