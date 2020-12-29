package com.mny.mojito.event

import com.jeremyliao.liveeventbus.LiveEventBus
import java.io.Serializable

abstract class BaseEvent : Serializable {
    companion object {
        private const val serialVersionUID: Long = -8985446122829543654
    }

    fun post() {
        LiveEventBus.get(this.javaClass.simpleName)
            .post(this)
    }
}