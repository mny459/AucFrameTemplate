package com.mny.wan.http

/**
 * 密封类：子类可以是任意的类: 数据类、Kotlin 对象、普通的类，甚至也可以是另一个密封类。
 * 但不同于抽象类的是，您必须把层级声明在同一文件中，或者嵌套在类的内部。
 */
sealed class MojitoResult<out T : Any> {

    data class Success<out T : Any>(val data: T?) : MojitoResult<T>()
    data class Error(val exception: Exception) : MojitoResult<Nothing>()
    object Loading : MojitoResult<Nothing>()
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}
/**
 * `true` if [MojitoResult] is of type [Success] & holds non-null [Success.data].
 */
val MojitoResult<*>.succeeded
    get() = this is MojitoResult.Success && data != null