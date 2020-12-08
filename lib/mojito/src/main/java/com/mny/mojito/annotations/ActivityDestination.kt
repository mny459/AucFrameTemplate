package com.mny.mojito.annotations

/**
 * Kotlin 注解的使用
 */
@Target(AnnotationTarget.CLASS)
annotation class ActivityDestination(
    val page: String,
    val needLogin: Boolean = false,
    val asStarter: Boolean = false
)