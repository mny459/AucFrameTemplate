package com.mny.wan.pkg.event

import com.mny.mojito.event.BaseEvent

/**
 * 收藏事件
 */
class CollectEvent(val collect: Boolean, val articleId: Int) : BaseEvent() {
    companion object {
        fun collect(articleId: Int) {
            CollectEvent(true, articleId).post()
        }

        fun cancelCollect(articleId: Int) {
            CollectEvent(false, articleId).post()
        }
    }
}