package com.mny.wan.annotations

@Target(AnnotationTarget.CLASS)
annotation class FragmentDestination(
    val page: String,
    val needLogin: Boolean = false,
    val asStarter: Boolean = false
)
