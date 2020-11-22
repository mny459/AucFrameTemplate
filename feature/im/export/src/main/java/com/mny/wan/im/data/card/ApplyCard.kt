package com.mny.wan.im.data.card

import java.util.*


/**
 * 申请请求的Card, 用于推送一个申请请求
 */
data class ApplyCard(
        // 申请Id
        var id: String,
        // 附件
        var attach: String? = null,
        // 描述
        var desc: String? = null,
        // 目标的类型
        var type: Int = 0,
        // 目标（群／人...的ID）
        var targetId: String? = null,
        // 申请人的Id
        var applicantId: String,
        // 创建时间
        var createAt: Date
)