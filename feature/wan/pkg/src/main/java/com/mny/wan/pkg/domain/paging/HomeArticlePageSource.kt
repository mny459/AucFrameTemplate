package com.mny.wan.pkg.domain.paging

import com.mny.wan.pkg.data.UrlManager
import com.mny.wan.pkg.domain.repository.WanRepository

/**
 * PagingSource: 负责真正的数据加载工作
 */
class HomeArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlHomeArticleList(page)
}

class SearchArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlSearchArticleList(page)
}

class ShareArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlShareArticleList(page)
}

class CollectArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlCollectArticleList(page)
}

class MineShareArticlePageSource constructor(mRepository: WanRepository) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlMineShareArticleList(page)
}

class SystemArticlePageSource constructor(mRepository: WanRepository, private val cid: Int) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlSystemArticleList(page, cid)
}

class ProjectArticlePageSource constructor(mRepository: WanRepository, private val cid: Int) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlProjectArticleList(page, cid)
}

class WeChatArticlePageSource constructor(mRepository: WanRepository, private val cid: Int) :
    ArticlePageSource(mRepository, 0) {
    override fun getUrlWithPage(page: Int): String = UrlManager.urlWeChatArticleList(page, cid)
}